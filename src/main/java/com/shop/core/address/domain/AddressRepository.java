package com.shop.core.address.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("select a from Address a where a.isDefault = true and a.memberId = :memberId")
    Address findDefaultAddressByMemberId(@Param("memberId") Long memberId);

    @Query("select a from Address a where a.memberId = :memberId")
    List<Address> findAllByMemberId(@Param("memberId") Long memberId);
}
