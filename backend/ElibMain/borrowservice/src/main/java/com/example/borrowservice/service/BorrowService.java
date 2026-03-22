package com.example.borrowservice.service;

import com.example.borrowservice.dto.BorrowDto;
import com.example.borrowservice.dto.NewBorrowDto;
import com.example.borrowservice.exception.ResourceNotFoundException;
import com.example.borrowservice.mapper.BorrowMapper;
import com.example.borrowservice.mapper.NewBorrowMapper;
import com.example.borrowservice.repository.BorrowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BorrowService {
    private final BorrowRepository borrowRepository;
    private final BorrowMapper borrowMapper;
    private final NewBorrowMapper newBorrowMapper;

    public List<BorrowDto> getAllBorrows() {
        return borrowRepository
                .findAll()
                .stream()
                .map(borrowMapper::toDto)
                .toList();
    }

    public BorrowDto getBorrowById(UUID id){
        return borrowRepository
                .findById(id)
                .map(borrowMapper::toDto)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Borrow not found")
                );
    }

    public BorrowDto createBorrow(NewBorrowDto newBorrowDto){
        return borrowMapper.toDto(
                borrowRepository.save(
                        newBorrowMapper.toEntity(newBorrowDto)
                )
        );
    }
}