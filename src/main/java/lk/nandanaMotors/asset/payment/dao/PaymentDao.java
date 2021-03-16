package lk.nandanaMotors.asset.payment.dao;

import lk.nandanaMotors.asset.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentDao extends JpaRepository< Payment, Integer > {
  Payment findFirstByOrderByIdDesc();

  List< Payment> findByCreatedAtIsBetween(LocalDateTime form, LocalDateTime to);
}
