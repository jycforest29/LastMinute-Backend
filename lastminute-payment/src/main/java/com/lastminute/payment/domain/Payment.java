package com.lastminute.payment.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long pgId; // PG사의 결제 정보와 연결되는 ID

    @NotNull
    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    PaymentMethod paymentMethod;

    @NotNull
    private Integer originalPrice;

    @NotNull
    private Integer fee;

    @NotNull
    private Integer finalPrice;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime acceptedAt;

    @NotNull
    private Boolean cancelAvailable = true;

    public void setCancelUnAvailable(){
        this.cancelAvailable = false;
    }

    @Builder
    public Payment(Long pgId, PaymentMethod paymentMethod, Integer originalPrice, Integer fee, Integer finalPrice){
        this.pgId = pgId;
        this.paymentMethod = paymentMethod;
        this.originalPrice = originalPrice;
        this.fee = fee;
        this.finalPrice = finalPrice;
    }
}
