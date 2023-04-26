package com.lastminute.payment.domain.garage;

import com.lastminute.payment.domain.Payment;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "bid")
public class Bid { // TODO: Bid 멀티모듈 분리하고 Bid 엔티티 완성하기

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

}
