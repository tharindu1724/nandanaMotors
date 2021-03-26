package lk.nandana_motors.asset.payment.service;


import lk.nandana_motors.asset.common_asset.model.Enum.LiveDead;
import lk.nandana_motors.asset.payment.dao.PaymentDao;
import lk.nandana_motors.asset.payment.entity.Payment;
import lk.nandana_motors.util.interfaces.AbstractService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@CacheConfig( cacheNames = "payment" )
public class PaymentService implements AbstractService< Payment, Integer > {
  private final PaymentDao paymentDao;

  public PaymentService(PaymentDao paymentDao) {
    this.paymentDao = paymentDao;
  }


  public List< Payment > findAll() {
    return paymentDao.findAll().stream().filter(x->x.getLiveDead().equals(LiveDead.ACTIVE)).collect(Collectors.toList());
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

  public List<Payment> findByCreatedAtIsBetweenAndUpdatedBy(LocalDateTime startDateTime, LocalDateTime endDateTime, String username) {
  return paymentDao.findByCreatedAtIsBetweenAndUpdatedBy(startDateTime,endDateTime,username);
  }
}
