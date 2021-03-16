package lk.nandanaMotors.asset.serviceType.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.nandanaMotors.asset.common_asset.model.Enum.LiveDead;
import lk.nandanaMotors.asset.payment.entity.Payment;
import lk.nandanaMotors.asset.service_type_parameter.entity.ServiceTypeParameter;
import lk.nandanaMotors.asset.vehicle.entity.Enum.VehicleModel;
import lk.nandanaMotors.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter("ServiceType")
public class ServiceType extends AuditEntity {

    private String name;

    @Column( nullable = false, precision = 10, scale = 2 )
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private VehicleModel vehicleModel;

    @Enumerated( EnumType.STRING)
    private LiveDead liveDead;

    @ManyToMany
    @JoinTable(name = "service_type_service_type_parameter",
            joinColumns = @JoinColumn(name = "service_type_id"),
            inverseJoinColumns = @JoinColumn(name = "service_type_parameter_id"))
    private List< ServiceTypeParameter > serviceTypeParameters;

    @OneToMany(mappedBy = "serviceType")
    private List< Payment > payments;
}
