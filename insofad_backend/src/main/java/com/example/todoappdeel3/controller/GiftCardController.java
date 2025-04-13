package com.example.todoappdeel3.controller;

import com.example.todoappdeel3.dto.GiftCardDTO;
import com.example.todoappdeel3.models.GiftCard;
import com.example.todoappdeel3.services.GiftCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/giftcards")
public class GiftCardController {

    @Autowired
    private GiftCardService giftCardService;

    @PostMapping
    public GiftCard createGiftCard(@RequestParam String recipientEmail, @RequestBody GiftCardDTO giftCardDTO) {
        return giftCardService.createGiftCard(giftCardDTO.getAmount(), recipientEmail);
    }

    @PostMapping("/redeem")
    public void redeemGiftCard(@RequestParam String code, @RequestParam double amount) {
        giftCardService.redeemGiftCard(code, amount);
    }

    @GetMapping("/{code}")
    public ResponseEntity<GiftCard> getGiftCardByCode(@PathVariable String code) {
        System.out.println("Getting gift card with code " + code);
        GiftCard giftCard = giftCardService.findByCode(code);
        return ResponseEntity.ok(giftCard);
    }

    @GetMapping("/all")
    public List<GiftCard> returnAllGiftCards() { return giftCardService.returnAllGiftCards();}

    @PutMapping("/deduct/{code}")
    public ResponseEntity<String> deductBalance(@PathVariable String code, @RequestParam double amount) {
        boolean success = giftCardService.deductBalance(code, amount);
        if (success) {
            return ResponseEntity.ok("{\n" +
                    "  \"message\": \"Balance deducted successfully\"\n" +
                    "}");
        } else {
            return ResponseEntity.badRequest().body("Insufficient balance or invalid gift card code");
        }
    }

    @PutMapping("/add/{code}")
    public ResponseEntity<String> addBalance(@PathVariable String code, @RequestParam double amount) {
        boolean success = giftCardService.addToBalance(code, amount);
        if (success) {
            return ResponseEntity.ok("{\n" +
                    "  \"message\": \"Balance deducted successfully\"\n" +
                    "}");
        } else {
            return ResponseEntity.badRequest().body("Insufficient balance or invalid gift card code");
        }
    }

    @DeleteMapping("/delete/{code}")
    public ResponseEntity<String> deleteBalance(@PathVariable String code) {
        boolean success = giftCardService.deleteGiftCard(code);
        if (success) {
            return ResponseEntity.ok("{\n" +
                    "  \"message\": \"Card successfully deleted\"\n" +
                    "}");
        } else {
            return ResponseEntity.badRequest().body("Error deleting card");
        }
    }
}
