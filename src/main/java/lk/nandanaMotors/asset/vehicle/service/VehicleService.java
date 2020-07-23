package lk.nandanaMotors.asset.vehicle.service;

import lk.nandanaMotors.asset.employee.entity.Employee;
import lk.nandanaMotors.asset.vehicle.dao.VehicleDao;
import lk.nandanaMotors.asset.vehicle.entity.Vehicle;
import lk.nandanaMotors.util.interfaces.AbstractService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService implements AbstractService<Vehicle, Integer> {
    private final VehicleDao vehicleDao;

    public VehicleService(VehicleDao vehicleDao) {
        this.vehicleDao = vehicleDao;
    }

    public List<Vehicle> findAll() {
        return vehicleDao.findAll();
    }

    public Vehicle findById(Integer id) {
        return vehicleDao.getOne(id);
    }

    public Vehicle persist(Vehicle vehicle) {
        return vehicleDao.save(vehicle);
    }

    public boolean delete(Integer id) {
        vehicleDao.deleteById(id);
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
}
