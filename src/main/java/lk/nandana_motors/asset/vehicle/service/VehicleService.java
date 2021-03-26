package lk.nandana_motors.asset.vehicle.service;


import lk.nandana_motors.asset.common_asset.model.Enum.LiveDead;
import lk.nandana_motors.asset.vehicle.dao.VehicleDao;
import lk.nandana_motors.asset.vehicle.entity.Vehicle;
import lk.nandana_motors.util.interfaces.AbstractService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleService implements AbstractService< Vehicle, Integer> {
    private final VehicleDao vehicleDao;

    public VehicleService(VehicleDao vehicleDao) {
        this.vehicleDao = vehicleDao;
    }

    public List<Vehicle> findAll() {
        return vehicleDao.findAll().stream().filter(x->x.getLiveDead().equals(LiveDead.ACTIVE)).collect(Collectors.toList());
    }

    public Vehicle findById(Integer id) {
        return vehicleDao.getOne(id);
    }

    public Vehicle persist(Vehicle vehicle) {
        if ( vehicle.getId() == null ) {
            vehicle.setLiveDead(LiveDead.ACTIVE);
        }
        return vehicleDao.save(vehicle);
    }

    public boolean delete(Integer id) {
        Vehicle customer = vehicleDao.getOne(id);
        customer.setLiveDead(LiveDead.STOP);
        vehicleDao.save(customer);
        return false;
    }

    public List<Vehicle> search(Vehicle vehicle) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Vehicle> vehicleExample = Example.of(vehicle, matcher);
        return vehicleDao.findAll(vehicleExample);
    }

  public Vehicle findByNumber(String number) {
  return vehicleDao.findByNumber(number);
    }

  public Vehicle lastVehicle() {
  return vehicleDao.findFirstByOrderByIdDesc();
    }
}
