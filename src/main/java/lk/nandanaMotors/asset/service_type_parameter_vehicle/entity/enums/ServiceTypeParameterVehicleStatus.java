package lk.nandanaMotors.asset.service_type_parameter_vehicle.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServiceTypeParameterVehicleStatus {
  CHK("Checked"),
  DONE("Done"),
  PEND("Pending"),
  PAID("Paid");

  private final String serviceTypeParameterVehicleStatus;
}
