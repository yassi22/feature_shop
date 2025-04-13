package com.example.todoappdeel3.services;

import com.example.todoappdeel3.dao.GiftCardRepository;
import com.example.todoappdeel3.dao.OrderRepository;
import com.example.todoappdeel3.models.GiftCard;
import com.example.todoappdeel3.models.Order;
import com.example.todoappdeel3.models.OrderProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GiftCardService {
    @Autowired
    private GiftCardRepository giftCardRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OrderRepository orderRepository;

    public List<GiftCard> returnAllGiftCards() {
        return this.giftCardRepository.findAll();
    }

    public GiftCard createGiftCard(double amount, String recipientEmail) {
        GiftCard giftCard = new GiftCard();
        giftCard.setCode(generateUniqueCode());
        giftCard.setAmount(amount);
        giftCard.setBalance(amount);
        giftCard.setExpiryDate(LocalDate.now().plusYears(1));
        giftCardRepository.save(giftCard);

        emailService.sendGiftCardEmail(recipientEmail, giftCard.getCode());

        return giftCard;
    }

    public GiftCard findByCode(String code) {
        return giftCardRepository.findByCode(code).orElse(null);
    }

    private String generateUniqueCode() {
        return UUID.randomUUID().toString();
    }

    public boolean redeemGiftCard(String code, double amountToRedeem) {
        GiftCard giftCard = findByCode(code);
        if (giftCard != null && giftCard.getBalance() >= amountToRedeem) {
            giftCard.setBalance(giftCard.getBalance() - amountToRedeem);
            giftCardRepository.save(giftCard);
            return true;
        }
        return false;
    }

    public boolean deductBalance(String code, double amount) {
        GiftCard giftCard = giftCardRepository.findByCode(code).orElse(null);
        if (giftCard != null && giftCard.getBalance() >= amount) {
            giftCard.setBalance(giftCard.getBalance() - amount);
            giftCardRepository.save(giftCard);
            return true;
        } else {
            giftCard.setBalance(0);
            giftCardRepository.save(giftCard);
            return true;
        }
    }

    public boolean addToBalance(String code, double amount)
    {
        GiftCard giftCard = giftCardRepository.findByCode(code).orElse(null);
        if (giftCard != null ) {
            giftCard.setBalance(giftCard.getBalance() + amount);
            giftCardRepository.save(giftCard);
            return true;
        }
        return false;
    }


    public double deductAsMuchBalanceAsPossible(String code,double totalOrderAmount) {
        GiftCard giftCard = giftCardRepository.findByCode(code).orElse(null);
        if (giftCard != null && giftCard.getBalance() >= 0) {
            if ( giftCard.getBalance() <= totalOrderAmount )
            {
                totalOrderAmount = totalOrderAmount - giftCard.getBalance();
                giftCard.setBalance(0);
                giftCardRepository.save(giftCard);
                return totalOrderAmount < 0 ? 0 : totalOrderAmount;
            }else{
                giftCard.setBalance(giftCard.getBalance() - totalOrderAmount);
                giftCardRepository.save(giftCard);
                return 0;
            }
        }
        return totalOrderAmount < 0 ? 0 : totalOrderAmount;
    }

    public boolean deleteGiftCard(String code) {
        Optional<GiftCard> giftCardOptional = giftCardRepository.findByCode(code);
        if (giftCardOptional.isPresent()) {
            GiftCard giftCard = giftCardOptional.get();
            for (Order placedOrder : orderRepository.findAll()) {
                for (OrderProduct orderProduct : placedOrder.getOrderProducts()) {
                    if (orderProduct.getGiftCards().remove(giftCard)) {
                        orderRepository.save(placedOrder);
                    }
                }
            }
            giftCardRepository.delete(giftCard);
            return true;
        }
        return false;
    }

}
