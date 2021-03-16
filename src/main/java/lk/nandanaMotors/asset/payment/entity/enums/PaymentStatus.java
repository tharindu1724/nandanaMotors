package lk.nandanaMotors.asset.payment.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentStatus {

  PAID("Payment Done"),
  NOTPAID("Not Paid"),
  CANCEL("Cancel ");

  private final String paymentStatus;

}
