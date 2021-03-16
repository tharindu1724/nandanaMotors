package lk.nandanaMotors.asset.payment.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.nandanaMotors.asset.common_asset.model.Enum.LiveDead;
import lk.nandanaMotors.asset.customer.entity.Customer;
import lk.nandanaMotors.asset.discount_ratio.entity.DiscountRatio;
import lk.nandanaMotors.asset.payment.entity.enums.PaymentMethod;
import lk.nandanaMotors.asset.payment.entity.enums.PaymentStatus;
import lk.nandanaMotors.asset.serviceType.entity.ServiceType;
import lk.nandanaMotors.asset.vehicle.entity.Vehicle;
import lk.nandanaMotors.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter("Payment")
public class Payment extends AuditEntity {

  private String bankName;

  private String cardNumber;

  private String remarks;

  @Column(nullable = false, unique = true)
  private String code;

  @Column( nullable = false, precision = 10, scale = 2 )
  private BigDecimal amount;

  @Column( nullable = false, precision = 10, scale = 2 )
  private BigDecimal totalAmount;

  @Column( nullable = false, precision = 10, scale = 2 )
  private BigDecimal discountAmount;

  @Column( nullable = false, precision = 10, scale = 2 )
  private BigDecimal amountTendered;

  @Column( nullable = false, precision = 10, scale = 2 )
  private BigDecimal balance;

  @Enumerated( EnumType.STRING )
  private PaymentStatus paymentStatus;

  @Enumerated(EnumType.STRING)
  private PaymentMethod paymentMethod;

  @Enumerated( EnumType.STRING)
  private LiveDead liveDead;

  @ManyToOne
  private Customer customer;

  @ManyToOne
  private Vehicle vehicle;

  @ManyToOne
  private DiscountRatio discountRatio;

  @ManyToOne
  private ServiceType serviceType;


}
