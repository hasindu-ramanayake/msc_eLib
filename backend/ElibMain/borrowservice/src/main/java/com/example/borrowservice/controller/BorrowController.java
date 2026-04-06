package com.example.borrowservice.controller;

import com.example.borrowservice.dto.BorrowDto;
import com.example.borrowservice.dto.NewBorrowDto;
import com.example.borrowservice.exception.NoContentException;
import com.example.borrowservice.service.BorrowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/borrow")
public class BorrowController {
    private final BorrowService borrowService;

    @GetMapping
    public ResponseEntity<List<BorrowDto>> getAllBorrow() {
        return ResponseEntity.ok(borrowService.getAllBorrows());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowDto> getBorrowById(@PathVariable UUID id){
        return ResponseEntity.ok(borrowService.getBorrowById(id));
    }

    @PostMapping
    public ResponseEntity<BorrowDto> createBorrow(@Valid @RequestBody NewBorrowDto newBorrow) {
        return ResponseEntity.ok(borrowService.createBorrow(newBorrow));
    }
}
