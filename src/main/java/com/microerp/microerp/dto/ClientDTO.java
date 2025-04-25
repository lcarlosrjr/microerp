package com.microerp.microerp.dto;

import com.microerp.microerp.enums.ClientTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {

    private Long id;
    private String fullName;
    private String documentNumber;
    private ClientTypeEnum type;
    private LocalDate birthDate;
    private String email;
    private String phone;

    private String address;
    private String district;
    private String city;
    private String state;
    private String postalCode;
    private String country;

    private String companyName;
    private String stateRegistration;
    private String municipalRegistration;
    private String notes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
