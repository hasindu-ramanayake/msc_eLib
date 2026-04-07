package com.example.borrowservice.service;

import com.example.borrowservice.client.ItemClient;
import com.example.borrowservice.client.UserClient;
import com.example.borrowservice.dto.BorrowDto;
import com.example.borrowservice.dto.NewBorrowDto;
import com.example.borrowservice.entity.Borrow;
import com.example.borrowservice.exception.ResourceNotFoundException;
import com.example.borrowservice.mapper.BorrowMapper;
import com.example.borrowservice.mapper.NewBorrowMapper;
import com.example.borrowservice.repository.BorrowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BorrowService {
    private final BorrowRepository borrowRepository;
    private final BorrowMapper borrowMapper;
    private final NewBorrowMapper newBorrowMapper;
    private final ItemService itemService;
    private final UserService userService;

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
        Borrow borrow = newBorrowMapper.toEntity(newBorrowDto);

        var item = itemService.getItemById(borrow.getItemId());

        var user = userService.getUserById(borrow.getUserId());

        borrow.setCheckOutDate(Date.from(Instant.now()));

        return borrowMapper.toDto(borrowRepository.save(borrow));
    }

    public List<BorrowDto> getBorrowByUserId(UUID id){
        return borrowRepository
                .findBorrowsByUserId(id)
                .stream()
                .map(borrowMapper::toDto)
                .toList();
    }
}