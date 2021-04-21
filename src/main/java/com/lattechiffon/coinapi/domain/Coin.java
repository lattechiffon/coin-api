package com.lattechiffon.coinapi.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "coin")
public class Coin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private Long id;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_idx")
    @Setter
    private User user;

    @ManyToOne(targetEntity = Market.class)
    @Setter
    private Market market;

    @Column(nullable = false)
    @Setter
    private Double breakEvenPrice;

    @Column(nullable = false)
    @Setter
    private Double targetPrice;

    @Column(nullable = false)
    @Setter
    private Boolean isCoinNotExpired;
}
