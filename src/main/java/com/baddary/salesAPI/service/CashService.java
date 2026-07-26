package com.baddary.salesAPI.service;

import java.math.BigDecimal;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baddary.salesAPI.entity.CashRegister;
import com.baddary.salesAPI.entity.Safe;
import com.baddary.salesAPI.repository.CashRegisterRepository;
import com.baddary.salesAPI.repository.SafeRepository;

import jakarta.persistence.OptimisticLockException;

@Service
@Transactional
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

    private void doDeposit(Long userId) {
        // 1. Fetch cash register for the user (with lock)
        CashRegister register = cashRegisterRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cash register not found for user"));

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
}
