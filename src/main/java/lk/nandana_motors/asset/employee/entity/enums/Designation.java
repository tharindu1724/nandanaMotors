package lk.nandana_motors.asset.employee.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Designation {
  ADMIN("Admin"),
    MANAGER("Manager"),
    IN_OFFICER("In_officer"),
    TECH("Technician"),
    CASHIER("Cashier");

    private final String designation;
}
