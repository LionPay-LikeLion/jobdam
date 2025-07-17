package com.jobdam.payment.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@Table(name = "payment")
public class Payment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long paymentId;

  @Column(nullable = false)
  private Integer userId;

  @Column(nullable = false)
  private Integer point;

  @Column(nullable = false)
  private Integer amount;

  @Column(nullable = false)
  private Integer paymentTypeCodeId;

  @Column(nullable = false)
  private Integer paymentStatusCodeId;

  @Column(nullable = false)
  private String method;

  @Column(unique = true)
  private String impUid;

  @Column(unique = true)
  private String merchantUid;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
  }
}
