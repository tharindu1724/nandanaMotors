package lk.nandanaMotors.asset.service_type_parameter_vehicle.service;


import lk.nandanaMotors.asset.serviceType.entity.ServiceType;
import lk.nandanaMotors.asset.service_type_parameter_vehicle.dao.ServiceTypeParameterVehicleDao;
import lk.nandanaMotors.asset.service_type_parameter_vehicle.entity.ServiceTypeParameterVehicle;
import lk.nandanaMotors.asset.service_type_parameter_vehicle.entity.enums.ServiceTypeParameterVehicleStatus;
import lk.nandanaMotors.asset.vehicle.entity.Vehicle;
import lk.nandanaMotors.util.interfaces.AbstractService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ServiceTypeParameterVehicleService implements AbstractService< ServiceTypeParameterVehicle, Integer > {
  private final ServiceTypeParameterVehicleDao serviceTypeParameterVehicleDao;

  public ServiceTypeParameterVehicleService(ServiceTypeParameterVehicleDao serviceTypeParameterVehicleDao) {
    this.serviceTypeParameterVehicleDao = serviceTypeParameterVehicleDao;
  }


  public List< ServiceTypeParameterVehicle > findAll() {
    return serviceTypeParameterVehicleDao.findAll();
  }

  public ServiceTypeParameterVehicle findById(Integer id) {
    return serviceTypeParameterVehicleDao.getOne(id);
  }

  public ServiceTypeParameterVehicle persist(ServiceTypeParameterVehicle serviceTypeParameterVehicle) {

    return serviceTypeParameterVehicleDao.save(serviceTypeParameterVehicle);
  }

  public boolean delete(Integer id) {
    ServiceTypeParameterVehicle serviceTypeParameterVehicle = serviceTypeParameterVehicleDao.getOne(id);

    serviceTypeParameterVehicleDao.save(serviceTypeParameterVehicle);
    return true;
  }

  public List< ServiceTypeParameterVehicle > search(ServiceTypeParameterVehicle serviceTypeParameterVehicle) {

    ExampleMatcher matcher = ExampleMatcher
        .matching()
        .withIgnoreCase()
        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    Example< ServiceTypeParameterVehicle > labTestParameterExample = Example.of(serviceTypeParameterVehicle, matcher);
    return serviceTypeParameterVehicleDao.findAll(labTestParameterExample);
  }

  public List< ServiceTypeParameterVehicle > findByCreatedAtIsBetween(LocalDateTime form, LocalDateTime to) {
    return serviceTypeParameterVehicleDao.findByCreatedAtIsBetween(form, to);
  }

  public List< ServiceTypeParameterVehicle > findByCreatedAtIsBetweenAndVehicle(LocalDateTime form, LocalDateTime to,
                                                                                Vehicle vehicle) {
    return serviceTypeParameterVehicleDao.findByCreatedAtIsBetweenAndVehicle(form, to, vehicle);
  }

  public List< ServiceTypeParameterVehicle > findByVehicleAndServiceTypeAndServiceTypeParameterVehicleStatus(Vehicle vehicle, ServiceType serviceType, ServiceTypeParameterVehicleStatus serviceTypeParameterVehicleStatus) {
    return serviceTypeParameterVehicleDao.findByVehicleAndServiceTypeAndServiceTypeParameterVehicleStatus(vehicle,
                                                                                                          serviceType
        , serviceTypeParameterVehicleStatus);
  }
}
