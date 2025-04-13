package com.example.todoappdeel3.controller;

import com.example.todoappdeel3.dao.RestituteDAO;
import com.example.todoappdeel3.dto.RestituteDTO;
import com.example.todoappdeel3.enums.RestituteStatus;
import com.example.todoappdeel3.models.Order;
import com.example.todoappdeel3.models.Restitute;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/restitutes")
public class RestituteController {

    private final RestituteDAO restituteDao;

    public RestituteController(
            RestituteDAO restituteDao
    ) {
        this.restituteDao = restituteDao;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Restitute>> getRestitutes() {
        List<Restitute> restitutes = restituteDao.getRestitutes();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(restitutes);
    }

    @GetMapping("/me")
    public ResponseEntity<List<Restitute>> getUserRestituteHistory() {
        List<Restitute> restitutes = restituteDao.getUserRestitutes();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(restitutes);
    }

    @GetMapping("/me/unrestituted")
    public ResponseEntity<List<Order>> getUserUnrestitutedHistory() {
        List<Order> orders = restituteDao.getUserUnrestitutedOrders();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orders);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Restitute> createRestitute(@RequestBody List<RestituteDTO> request) {
        Restitute restitute = restituteDao.createRestitute(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(restitute);
    }

    @PutMapping("/{restituteId}/status")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Restitute> updateRestituteStatus(
            @PathVariable Long restituteId,
            @RequestBody RestituteStatus status
    ) {
        Restitute restitute = restituteDao.updateRestituteStatus(restituteId, status);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(restitute);
    }
}
