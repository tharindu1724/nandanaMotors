package lk.nandana_motors.asset.common_asset.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccordingToUserAllDetail {
  private String employeeName;
  private List< NameCount > paidServiceTypeAndCounts;
  private List< ParameterCountPrice > paymentTypeCountAndTotalAmounts;
  private List< NameCount > allNotPaidServiceTypeAndCounts;
  private List< ParameterCountPrice > allNotPaidTypeCountAndTotalAmounts;
  private List< NameCount > allCancelPaidServiceTypeAndCounts;
  private List< ParameterCountPrice > allCancelTypeCountAndTotalAmounts;
}
