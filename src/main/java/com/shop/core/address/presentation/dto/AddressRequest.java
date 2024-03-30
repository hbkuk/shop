package com.shop.core.address.presentation.dto;

import com.shop.core.address.domain.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequest {

    private Long id;

    private String address;

    private String detailedAddress;

    private String description;

    private Boolean isDefault;

    public AddressRequest(String address, String detailedAddress, String description, Boolean isDefault) {
        this.address = address;
        this.detailedAddress = detailedAddress;
        this.description = description;
        this.isDefault = isDefault;
    }

    public static AddressRequest of(String address, String detailedAddress, String description, boolean isDefault) {
        return new AddressRequest(address, detailedAddress, description, isDefault);
    }

    public static AddressRequest mergeAddressId(Long id, AddressRequest request) {
        return new AddressRequest(id, request.getAddress(), request.getDetailedAddress(), request.getDescription(), request.getIsDefault());
    }

    public Address toEntity(Long memberId) {
        return new Address(address, detailedAddress, description, isDefault, memberId);
    }
}
