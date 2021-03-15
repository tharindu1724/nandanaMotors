package lk.nandana_motors.asset.common_asset.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParameterCountPrice {
    private String name;
    private Integer count;
    private BigDecimal amount;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
}
