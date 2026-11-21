package com.baddary.salesAPI.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baddary.salesAPI.entity.CashRegister;
import com.baddary.salesAPI.entity.Safe;
import com.baddary.salesAPI.exception.ResourceNotFoundException;
import com.baddary.salesAPI.repository.CashRegisterRepository;
import com.baddary.salesAPI.repository.SafeRepository;

import jakarta.persistence.OptimisticLockException;

@Service
public class CashService {

    private final SafeRepository safeRepository;
    private final CashRegisterRepository cashRegisterRepository;

    public CashService(SafeRepository safeRepository, CashRegisterRepository cashRegisterRepository) {
        this.safeRepository = safeRepository;
        this.cashRegisterRepository = cashRegisterRepository;
    }

    // ==========================================
    // 1. RECEIVE CASH (With Retry)
    // ==========================================
    public void receiveCashFromSafe(Long userId, BigDecimal amount) {
        int retries = 3;
        while (retries > 0) {
            try {
                doReceiveCashFromSafe(userId, amount);
                return; // Success! Exit the loop.
            } catch (OptimisticLockException | OptimisticLockingFailureException e) {
                retries--;
                if (retries == 0) {
                    throw new RuntimeException("The safe was updated by another user. Please try again.");
                }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void doReceiveCashFromSafe(Long userId, BigDecimal amount) {
        CashRegister register = cashRegisterRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cash register for user", userId));
        
        Safe safe = safeRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Safe is not initialized"));

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (amount.compareTo(safe.getTotalAmount()) > 0) {
            throw new IllegalArgumentException("Demanded amount is greater than safe amount");
        }
        
        register.setCurrentAmount(register.getCurrentAmount().add(amount));
        safe.setTotalAmount(safe.getTotalAmount().subtract(amount));
        cashRegisterRepository.save(register);
        safeRepository.save(safe);
    }

    // ==========================================
    // 2. DEPOSIT CASH (With Retry)
    // ==========================================
    public void depositCashToSafe(Long userId, BigDecimal amount) {
        int retries = 3;
        while (retries > 0) {
            try {
                doDepositCashToSafe(userId, amount);
                return; // Success! Exit the loop.
            } catch (OptimisticLockException | OptimisticLockingFailureException e) {
                retries--;
                if (retries == 0) {
                    throw new RuntimeException("The safe was updated by another user. Please try again.");
                }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void doDepositCashToSafe(Long userId, BigDecimal amount) {
        CashRegister register = cashRegisterRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cash register for user", userId));

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (amount.compareTo(register.getCurrentAmount()) > 0) {
            throw new IllegalArgumentException("Amount must not be greater than user cash");
        }

        Safe safe = safeRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Safe is not initialized"));

        safe.setTotalAmount(safe.getTotalAmount().add(amount));
        safeRepository.save(safe);

        register.setCurrentAmount(register.getCurrentAmount().subtract(amount));
        cashRegisterRepository.save(register);
    }

    // ==========================================
    // 3. USER CASH (No retry needed here usually, but fixed findById)
    // ==========================================
    @Transactional(rollbackFor = Exception.class)
    public void increaseUserCash(Long userId, BigDecimal amount) {
        CashRegister register = cashRegisterRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cash register for user", userId));
        
        register.setCurrentAmount(register.getCurrentAmount().add(amount));
        cashRegisterRepository.save(register);
    }

    @Transactional(rollbackFor = Exception.class)
    public void decreaseUserCash(Long userId, BigDecimal amount) {
        CashRegister register = cashRegisterRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cash register for user", userId));
        
        if (amount.compareTo(register.getCurrentAmount()) > 0) {
            throw new IllegalArgumentException("User Cash is not enough");
        }
        register.setCurrentAmount(register.getCurrentAmount().subtract(amount));
        cashRegisterRepository.save(register);
    }

    @Transactional(readOnly = true)
    public Optional<BigDecimal> currentAmount(Long userId) {
        return cashRegisterRepository.findCurrentAmountByUserId(userId);
    }
}