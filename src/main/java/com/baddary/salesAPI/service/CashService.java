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

    public void depositCashToSafe(Long userId, BigDecimal amount) {
        int retries = 3;
        while (retries > 0) {
            try {
                doDeposit(userId, amount);
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
    public void doReceiveCashFromUser(Long userFromId, Long userToId, BigDecimal amount) {
        CashRegister registerFrom = cashRegisterRepository.findByUserId(userFromId)
                .orElseThrow(() -> new ResourceNotFoundException("Cash register not found for user that will deliver"));
        CashRegister registerTo = cashRegisterRepository.findByUserId(userToId)
                .orElseThrow(() -> new ResourceNotFoundException("Cash register not found for user that will receive"));
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("No cash to deposit.");
        }
        if (amount.compareTo(registerFrom.getCurrentAmount()) > 0) { // error
            throw new RuntimeException("amount mustn't be bigger than user cash");

        }
        registerTo.setCurrentAmount(amount.add(registerTo.getCurrentAmount()));
        cashRegisterRepository.save(registerTo);
        registerFrom.setCurrentAmount(registerFrom.getCurrentAmount().subtract(amount));
        cashRegisterRepository.save(registerFrom);
    }

    @Transactional
    public void doReceiveCashFromSafe(Long userId, BigDecimal amount) {
        CashRegister register = cashRegisterRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cash register not found for user"));
        Safe safe = safeRepository.findAll().getFirst();

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("amount must be greater than zero");
        }
        if (amount.compareTo(safe.getTotalAmount()) > 0) {
            throw new RuntimeException("Demanded amount is greater than safe amount");
        }
        register.setCurrentAmount(register.getCurrentAmount().add(amount));
        safe.setTotalAmount(safe.getTotalAmount().subtract(amount));
        cashRegisterRepository.save(register);
        safeRepository.save(safe);
    }

    public void receiveCashFromSafe(Long userId, BigDecimal amount) {
        int retries = 3;
        while (retries > 0) {
            try {
                doReceiveCashFromSafe(userId, amount);
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
    public void doDeposit(Long userId, BigDecimal amount) {
        // 1. Fetch cash register for the user (with lock)
        CashRegister register = cashRegisterRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cash register not found for user"));

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("amount must be greater than zero");
        }

        if (amount.compareTo(register.getCurrentAmount()) > 0) {
            throw new RuntimeException("amount mustn't be greater than user cash");
        }

        // 2. Fetch safe (lock it)
        Safe safe = safeRepository.findAll().getFirst();

        // 3. Update safe
        safe.setTotalAmount(safe.getTotalAmount().add(amount));
        safeRepository.save(safe);

        // 4. Reset cash register
        register.setCurrentAmount(register.getCurrentAmount().subtract(amount));
        cashRegisterRepository.save(register);
    }

    @Transactional
    public void doIncreaseUserCash(Long userId, BigDecimal amount) {
        CashRegister register = cashRegisterRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cash register not found for user"));
        register.setCurrentAmount(register.getCurrentAmount().add(amount));
        cashRegisterRepository.save(register);
    }

    @Transactional
    public void doDecreaseUserCash(Long userId, BigDecimal amount) {
        CashRegister register = cashRegisterRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cash register not found for user"));
        
        if (amount.compareTo(register.getCurrentAmount()) > 0) {
            throw new RuntimeException("User Cash is not enough");
        }
        register.setCurrentAmount(register.getCurrentAmount().subtract(amount));
        cashRegisterRepository.save(register);
    }

    public void decreaseUserCash(Long userId, BigDecimal amount) {
        int retries = 3;
        while (retries > 0) {
            try {
                doDecreaseUserCash(userId, amount);
                return;
            } catch (OptimisticLockException | OptimisticLockingFailureException e) {
                retries--;
                if (retries == 0) {
                    throw new RuntimeException("Failed to update cash due to concurrent update. Please try again.");
                }
            }
        }
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
