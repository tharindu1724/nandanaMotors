package lk.nandana_motors.asset.service_type_parameter.dao;


import lk.nandana_motors.asset.service_type_parameter.entity.ServiceTypeParameter;
import lk.nandana_motors.asset.vehicle.entity.enums.VehicleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceTypeParameterDao extends JpaRepository< ServiceTypeParameter, Integer> {

  List< ServiceTypeParameter> findByVehicleModel(VehicleModel vehicleModel);
}
