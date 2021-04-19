package com.lattechiffon.coinapi.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private Long id;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    @Setter
    private User user;

    @Column(length = 100, nullable = false, unique = true)
    @Setter
    private String token;

    @Column(nullable = false)
    @Setter
    private Boolean isTokenNotExpired;
}
