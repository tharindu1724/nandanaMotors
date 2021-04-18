package lk.nandana_motors.asset.service_type_parameter.service;


import lk.nandana_motors.asset.common_asset.model.Enum.LiveDead;
import lk.nandana_motors.asset.service_type_parameter.dao.ServiceTypeParameterDao;
import lk.nandana_motors.asset.service_type_parameter.entity.ServiceTypeParameter;
import lk.nandana_motors.asset.vehicle.entity.enums.VehicleModel;
import lk.nandana_motors.util.interfaces.AbstractService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceTypeParameterService implements AbstractService< ServiceTypeParameter, Integer> {
private final ServiceTypeParameterDao serviceTypeParameterDao;

    public ServiceTypeParameterService(ServiceTypeParameterDao serviceTypeParameterDao) {
        this.serviceTypeParameterDao = serviceTypeParameterDao;
    }

    public List<ServiceTypeParameter> findAll() {
        return serviceTypeParameterDao.findAll().stream().filter(x->x.getLiveDead().equals(LiveDead.ACTIVE)).collect(Collectors.toList());
    }

    public ServiceTypeParameter findById(Integer id) {
        return serviceTypeParameterDao.getOne(id);
    }

    public ServiceTypeParameter persist(ServiceTypeParameter serviceTypeParameter) {
        if ( serviceTypeParameter.getId() == null ) {
            serviceTypeParameter.setLiveDead(LiveDead.ACTIVE);
        }
        return serviceTypeParameterDao.save(serviceTypeParameter);
    }

    public boolean delete(Integer id) {
        ServiceTypeParameter serviceTypeParameter = serviceTypeParameterDao.getOne(id);
        serviceTypeParameter.setLiveDead(LiveDead.STOP);
        serviceTypeParameterDao.save(serviceTypeParameter);
        return true;
    }

    public List<ServiceTypeParameter> search(ServiceTypeParameter serviceTypeParameter) {

        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<ServiceTypeParameter> labTestParameterExample = Example.of(serviceTypeParameter, matcher);
        return serviceTypeParameterDao.findAll(labTestParameterExample);
    }

  public List<ServiceTypeParameter> findByVehicleModel(VehicleModel vehicleModel) {
        return serviceTypeParameterDao.findByVehicleModel(vehicleModel);
  }
}
