package lk.nandanaMotors.asset.serviceTypeParameter.dao;


import lk.nandanaMotors.asset.serviceTypeParameter.entity.ServiceTypeParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceTypeParameterDao extends JpaRepository<ServiceTypeParameter, Integer> {

}