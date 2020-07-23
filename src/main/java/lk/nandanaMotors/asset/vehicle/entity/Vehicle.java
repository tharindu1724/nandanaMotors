package lk.nandanaMotors.asset.vehicle.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.nandanaMotors.asset.vehicle.entity.Enum.VehicleModel;
import lk.nandanaMotors.asset.vehicle.entity.Enum.VehicleType;
import lk.nandanaMotors.util.audit.AuditEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@JsonFilter("Vehicle")
public class Vehicle extends AuditEntity {
    private String registrationNumber;
    private String number;
    private String engineNumber;
    private String chassisNumber;
    private String manufacturedYear;
    private String gearNumber;
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType; //heavy vehicle or....
    @Enumerated(EnumType.STRING)
    private VehicleModel vehicleModel;//van car or ...
}
