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

    public void depositAllCashToSafe(Long userId) {
        int retries = 3;
        while (retries > 0) {
            try {
                doDeposit(userId);
                return;
            } catch (OptimisticLockException | OptimisticLockingFailureException e) {
                retries--;
                if (retries == 0) {
                    throw new RuntimeException("Deposit failed due to concurrent update. Please try again.");
                }
            }
        }
    }

    @Transactional
    public void doDeposit(Long userId) {
        // 1. Fetch cash register for the user (with lock)
        CashRegister register = cashRegisterRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cash register not found for user"));

        BigDecimal amountToDeposit = register.getCurrentAmount();
        if (amountToDeposit.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("No cash to deposit.");
        }

        // 2. Fetch safe (lock it)
        Safe safe = safeRepository.findAll().getFirst();

        // 3. Update safe
        safe.setTotalAmount(safe.getTotalAmount().add(amountToDeposit));
        safeRepository.save(safe);

        // 4. Reset cash register
        register.setCurrentAmount(BigDecimal.ZERO);
        cashRegisterRepository.save(register);
    }

    @Transactional
    public void doIncreaseUserCash(Long userId, BigDecimal amount) {
        CashRegister register = cashRegisterRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cash register not found for user"));
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Amount must be a positive or zero number");
        }
        register.getCurrentAmount().add(amount);
        cashRegisterRepository.save(register);
    }

    public void increaseUserCash(Long userId, BigDecimal amount) {
        int retries = 3;
        while (retries > 0) {
            try {
                doIncreaseUserCash(userId, amount);
                return;
            } catch (OptimisticLockException | OptimisticLockingFailureException e) {
                retries--;
                if (retries == 0) {
                    throw new RuntimeException("Failed to update cash due to concurrent update. Please try again.");
                }
            }
        }
    }

    public Optional<BigDecimal> currentAmount(Long userId) {
        return cashRegisterRepository.currentAmount(userId);
    }
}
