package com.example.borrowservice.controller;

import com.example.borrowservice.dto.BorrowDto;
import com.example.borrowservice.dto.NewBorrowDto;
import com.example.borrowservice.exception.NoContentException;
import com.example.borrowservice.service.BorrowService;
import com.example.borrowservice.service.CreditService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/borrows")
public class BorrowController {
    private final BorrowService borrowService;
    private final CreditService creditService;

    @GetMapping
    public ResponseEntity<List<BorrowDto>> getAllBorrow() {
        return ResponseEntity.ok(borrowService.getAllBorrows());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowDto> getBorrowById(@PathVariable UUID id){
        return ResponseEntity.ok(borrowService.getBorrowById(id));
    }

    @GetMapping("/users/{user_id}")
    public ResponseEntity<List<BorrowDto>> getBorrowByUserId(@PathVariable UUID user_id){
        return ResponseEntity.ok(borrowService.getBorrowByUserId(user_id));
    }

    @GetMapping("/users/{user_id}/overdue")
    public ResponseEntity<List<BorrowDto>> getBorrowOverDueByUserId(@PathVariable UUID user_id){
        return ResponseEntity.ok(borrowService.getOverDueBorrow(user_id));
    }

    @GetMapping("/users/{user_id}/underdue")
    public ResponseEntity<List<BorrowDto>> getBorrowUnderDueByUserId(@PathVariable UUID user_id){
        return ResponseEntity.ok(borrowService.getUnderDueBorrow(user_id));
    }

    @GetMapping("/users/{user_id}/credit")
    public ResponseEntity<?> getCreditScore(@PathVariable UUID user_id){
        return ResponseEntity.ok(creditService.getCreditScore(user_id));
    }

    @PostMapping
    public ResponseEntity<BorrowDto> createBorrow(@Valid @RequestBody NewBorrowDto newBorrow) {
        return ResponseEntity.ok(borrowService.createBorrow(newBorrow));
    }
}
