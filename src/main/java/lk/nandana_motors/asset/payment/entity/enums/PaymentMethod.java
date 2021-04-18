package lk.nandana_motors.asset.payment.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentMethod {
  CASH("Cash"),
  CREDIT("Credit card");
  private final String paymentMethod;
}

