package lk.nandanaMotors.asset.service_type_parameter.dao;


import lk.nandanaMotors.asset.service_type_parameter.entity.ServiceTypeParameter;
import lk.nandanaMotors.asset.vehicle.entity.Enum.VehicleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceTypeParameterDao extends JpaRepository< ServiceTypeParameter, Integer> {

  List< ServiceTypeParameter> findByVehicleModel(VehicleModel vehicleModel);
}
