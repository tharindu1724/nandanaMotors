package lk.nandanaMotors.asset.payment.service;


import lk.nandanaMotors.asset.common_asset.model.Enum.LiveDead;
import lk.nandanaMotors.asset.payment.dao.PaymentDao;
import lk.nandanaMotors.asset.payment.entity.Payment;
import lk.nandanaMotors.util.interfaces.AbstractService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@CacheConfig( cacheNames = "payment" )
public class PaymentService implements AbstractService< Payment, Integer > {
  private final PaymentDao paymentDao;

  public PaymentService(PaymentDao paymentDao) {
    this.paymentDao = paymentDao;
  }


  public List< Payment > findAll() {
    return paymentDao.findAll();
  }

  public Payment findById(Integer id) {
    return paymentDao.getOne(id);
  }

  public Payment persist(Payment payment) {
    if ( payment.getId() == null ) {
      payment.setLiveDead(LiveDead.ACTIVE);
    }
    return paymentDao.save(payment);
  }

  public boolean delete(Integer id) {
    Payment payment = paymentDao.getOne(id);
    payment.setLiveDead(LiveDead.STOP);
    paymentDao.save(payment);
    return false;
  }

  public List< Payment > search(Payment payment) {
    ExampleMatcher matcher = ExampleMatcher
        .matching()
        .withIgnoreCase()
        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    Example< Payment > customerExample = Example.of(payment, matcher);
    return paymentDao.findAll(customerExample);
  }

  public Payment lastPayment() {
    return paymentDao.findFirstByOrderByIdDesc();
  }

  public List< Payment > findByCreatedAtIsBetween(LocalDateTime form, LocalDateTime to) {
    return paymentDao.findByCreatedAtIsBetween(form, to);
  }
}
