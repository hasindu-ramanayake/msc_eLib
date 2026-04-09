package com.elib.user.dto;

import com.elib.user.entity.Address;
import com.elib.user.entity.User;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserMapper {

    public AddressDTO toAddressDTO(Address address) {
        log.trace("Mapping Address to AddressDTO");
        if (address == null) return null;
        return new AddressDTO(
                address.getAddressLine1(),
                address.getAddressLine2(),
                address.getEircode(),
                address.getCounty()
        );
    }

    public Address toAddressEntity(AddressDTO dto) {
        if (dto == null) return null;
        return Address.builder()
                .addressLine1(dto.addressLine1())
                .addressLine2(dto.addressLine2())
                .eircode(dto.eircode())
                .county(dto.county())
                .build();
    }

    public UserDTO toUserDTO(User user) {
        if (user == null) return null;
        return new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
                user.getNotificationPreferences(),
                toAddressDTO(user.getAddress())
        );
    }

    public User toUserEntity(UserDTO dto) {
        if (dto == null) return null;
        return User.builder()
                .id(dto.id())
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .email(dto.email())
                .role(dto.role())
                .notificationPreferences(dto.notificationPreferences())
                .address(toAddressEntity(dto.address()))
                .build();
    }

    public User toUserEntity(UserRegistrationDTO dto) {
        if (dto == null) return null;
        return User.builder()
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .email(dto.email())
                .password(dto.password())
                .address(toAddressEntity(dto.address()))
                .build();
    }
}
