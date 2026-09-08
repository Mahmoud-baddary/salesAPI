package com.baddary.salesAPI.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.baddary.salesAPI.repository.SafeRepository;
@Service
public class SafeService {
    private final SafeRepository safeRepository;

    public SafeService(SafeRepository safeRepository) {
        this.safeRepository = safeRepository;
    }
    public Optional<BigDecimal> totalAmount(){
        return safeRepository.totalAmount();
    }
     
}
