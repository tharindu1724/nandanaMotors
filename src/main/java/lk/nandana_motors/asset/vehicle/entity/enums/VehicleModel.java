package lk.nandana_motors.asset.vehicle.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VehicleModel {
    VAN("Van"),
    CAR("Car"),
    BIKE("Bike"),
    LORRY("Lorry"),
    DOZOR("Dozor"),
    TRACTOR("Tractor"),
    BUS("Bus");

    private final String vehicleModel;

}
