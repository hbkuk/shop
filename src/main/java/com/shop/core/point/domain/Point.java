package com.shop.core.point.domain;

import com.shop.common.domain.base.BaseEntity;
import com.shop.core.member.domain.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "point")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int amount;

    private PointStatus pointStatus;

    private PointType pointType;

    @JoinColumn(name = "MEMBER_EMAIL")
    private String memberEmail;

    public Point(int amount, PointStatus pointStatus, PointType pointType, String memberEmail) {
        this.amount = amount;
        this.pointStatus = pointStatus;
        this.pointType = pointType;
        this.memberEmail = memberEmail;
    }

    public boolean isOwn(Member member) {
        return memberEmail.equals(member.getEmail());
    }
}
