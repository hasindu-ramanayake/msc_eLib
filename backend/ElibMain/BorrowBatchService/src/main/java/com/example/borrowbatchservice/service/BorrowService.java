package com.example.borrowbatchservice.service;

import com.example.borrowbatchservice.entity.Borrow;
import com.example.borrowbatchservice.repository.BorrowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowService {
    private final BorrowRepository borrowRepository;

    public void processOverDueBorrows(){
        borrowRepository
                .findOverdueBooks();
    }
}
