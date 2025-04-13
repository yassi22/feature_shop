package com.example.todoappdeel3.services;

import com.example.todoappdeel3.dao.PromoCodeRepository;
import com.example.todoappdeel3.models.PromoCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PromoCodeService {

    private final PromoCodeRepository promoCodeRepository;

    public PromoCodeService(PromoCodeRepository promoCodeRepository) {
        this.promoCodeRepository = promoCodeRepository;
    }

    @Transactional
    public void usePromoCode(String code) {
        PromoCode promoCode = promoCodeRepository.findByCode(code);
        if (promoCode != null) {
            promoCode.setUsageCount(promoCode.getUsageCount() + 1);
            System.out.println("Promo code used " + promoCode.getUsageCount() + " times");
            promoCodeRepository.save(promoCode);
        } else {
            System.out.println("Promo code not found");
        }
    }

    @Transactional
    public void deletePromoCode(String code) {
        PromoCode promoCode = promoCodeRepository.findByCode(code);
        if (promoCode != null) {
            promoCodeRepository.delete(promoCode);
            System.out.println("Promo code deleted");
        } else {
            System.out.println("Promo code not found");
        }
    }
}

