package lk.nandana_motors.asset.vehicle.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.nandana_motors.asset.common_asset.model.Enum.LiveDead;
import lk.nandana_motors.asset.customer.entity.Customer;
import lk.nandana_motors.asset.service_type_parameter_vehicle.entity.ServiceTypeParameterVehicle;
import lk.nandana_motors.asset.vehicle.entity.enums.GearType;
import lk.nandana_motors.asset.vehicle.entity.enums.VehicleModel;
import lk.nandana_motors.util.audit.AuditEntity;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonFilter("Vehicle")
public class Vehicle extends AuditEntity {

    private String  code;

    @Column(unique = true)
    private String number;

    @Column(unique = true, nullable = false)
    private String registrationNumber;// nn09089

    @Column(unique = true, nullable = false)
    private String engineNumber;

    @Column(unique = true, nullable = false)
    private String chassisNumber;

    private String manufacturedYear;

    @Enumerated(EnumType.STRING)
    private GearType gearType;

    @Enumerated(EnumType.STRING)
    private LiveDead liveDead;

    @Enumerated(EnumType.STRING)
    private VehicleModel vehicleModel;//van car or ...

    @ManyToOne(fetch = FetchType.EAGER)
    private Customer customer;

    @OneToMany(mappedBy = "vehicle")
    private List< ServiceTypeParameterVehicle > serviceTypeParameterVehicles;

    @Transient
    @DateTimeFormat( pattern = "yyyy-MM-dd" )
    private LocalDate to, form;

}
