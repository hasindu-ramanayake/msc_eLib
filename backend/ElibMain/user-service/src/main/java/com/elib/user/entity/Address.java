package com.elib.user.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {
    private String addressLine1;
    private String addressLine2;
    private String eircode;
    private String county;
}
