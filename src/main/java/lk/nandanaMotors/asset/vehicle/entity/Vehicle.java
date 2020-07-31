package lk.nandanaMotors.asset.vehicle.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.nandanaMotors.asset.vehicle.entity.Enum.VehicleModel;
import lk.nandanaMotors.asset.vehicle.entity.Enum.VehicleType;
import lk.nandanaMotors.util.audit.AuditEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonFilter("Vehicle")
public class Vehicle extends AuditEntity {

    @Column(unique = true)
    private String number; //wp NA 09238

    @Column(unique = true, nullable = false)
    private String registrationNumber;// nn09089

    @Column(unique = true, nullable = false)
    private String engineNumber;

    @Column(unique = true, nullable = false)
    private String chassisNumber;

    private String manufacturedYear;

    private String gearNumber;

    private String model;

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType; //heavy vehicle or....

    @Enumerated(EnumType.STRING)
    private VehicleModel vehicleModel;//van car or ...
}
