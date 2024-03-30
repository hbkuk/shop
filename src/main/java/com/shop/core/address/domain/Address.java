package com.shop.core.address.domain;

import com.shop.core.member.domain.Member;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;

    private String detailedAddress;

    private String description;

    private boolean isDefault;

    @JoinColumn(name = "MEMBER_ID")
    private Long memberId;

    protected Address() {
    }

    public Address(String address, String detailedAddress, String description, boolean isDefault, Long memberId) {
        this.address = address;
        this.detailedAddress = detailedAddress;
        this.description = description;
        this.isDefault = isDefault;
        this.memberId = memberId;
    }


    public Address update(String address, String detailedAddress, String description, Boolean isDefault) {
        this.address = address;
        this.detailedAddress = detailedAddress;
        this.description = description;
        this.isDefault = isDefault;

        return this;
    }

    public void updateDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public boolean isOwn(Member member) {
        return memberId.equals(member.getId());
    }
}
