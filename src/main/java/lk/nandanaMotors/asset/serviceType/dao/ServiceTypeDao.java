package lk.nandanaMotors.asset.serviceType.dao;


import lk.nandanaMotors.asset.serviceType.entity.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceTypeDao extends JpaRepository<ServiceType, Integer> {

}