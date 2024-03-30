package com.shop.core.address.presentation.dto;

import com.shop.core.address.domain.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {

    private Long id;

    private String address;

    private String detailedAddress;

    private String description;

    private Boolean isDefault;

    public static AddressResponse of(Long id, String address, String detailedAddress, String description, boolean isDefault) {
        return new AddressResponse(id, address, detailedAddress, description, isDefault);
    }

    public static AddressResponse of(Address address) {
        return new AddressResponse(address.getId(), address.getAddress(), address.getDetailedAddress(), address.getDescription(), address.isDefault());
    }
}
