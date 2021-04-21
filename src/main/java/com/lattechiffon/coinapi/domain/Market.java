package com.lattechiffon.coinapi.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "market")
public class Market {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private Long id;

    @Column(length = 30, nullable = false, unique = true)
    @Setter
    private String marketname;

    @Column(length = 30, nullable = false)
    @Setter
    private String coinname;

    @OneToMany(mappedBy = "market", fetch = FetchType.LAZY)
    private List<Coin> coins;

    @Column(nullable = false)
    @Setter
    private Boolean isMarketNotExpired;
}
