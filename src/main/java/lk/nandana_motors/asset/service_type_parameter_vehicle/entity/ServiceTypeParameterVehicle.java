package lk.nandana_motors.asset.service_type_parameter_vehicle.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.nandana_motors.asset.serviceType.entity.ServiceType;
import lk.nandana_motors.asset.service_type_parameter.entity.ServiceTypeParameter;
import lk.nandana_motors.asset.service_type_parameter_vehicle.entity.enums.ServiceTypeParameterVehicleStatus;
import lk.nandana_motors.asset.vehicle.entity.Vehicle;
import lk.nandana_motors.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter( "ServiceTypeParameterVehicle" )
public class ServiceTypeParameterVehicle extends AuditEntity {
  @Column( nullable = false )
  private int meterValue;

  @Enumerated( EnumType.STRING )
  private ServiceTypeParameterVehicleStatus serviceTypeParameterVehicleStatus;

  @ManyToOne
  private ServiceTypeParameter serviceTypeParameter;

  @ManyToOne
  private Vehicle vehicle;

  @ManyToOne
  private ServiceType serviceType;

  @Transient
  private List< ServiceType > serviceTypes;
}
