package com.baddary.salesAPI.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baddary.salesAPI.repository.SafeRepository;
@Service
public class SafeService {
    private final SafeRepository safeRepository;

    public SafeService(SafeRepository safeRepository) {
        this.safeRepository = safeRepository;
    }
    @Transactional(readOnly = true)
    public Optional<BigDecimal> totalAmount(){
        return safeRepository.totalAmount();
    }
     
}
