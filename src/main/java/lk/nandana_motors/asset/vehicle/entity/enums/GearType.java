package lk.nandana_motors.asset.vehicle.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GearType {
    AUTO("Auto"),
    MANUAL("Manual");

    private final String gearType;
}
