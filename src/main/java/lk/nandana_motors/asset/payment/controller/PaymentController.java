package lk.nandana_motors.asset.payment.controller;

import lk.nandana_motors.asset.common_asset.model.Enum.LiveDead;
import lk.nandana_motors.asset.common_asset.model.TwoDate;
import lk.nandana_motors.asset.customer.entity.Customer;
import lk.nandana_motors.asset.customer.service.CustomerService;
import lk.nandana_motors.asset.discount_ratio.service.DiscountRatioService;
import lk.nandana_motors.asset.payment.entity.Payment;
import lk.nandana_motors.asset.payment.entity.enums.PaymentMethod;
import lk.nandana_motors.asset.payment.entity.enums.PaymentStatus;
import lk.nandana_motors.asset.payment.service.PaymentService;
import lk.nandana_motors.asset.service_type_parameter_vehicle.entity.enums.ServiceTypeParameterVehicleStatus;
import lk.nandana_motors.asset.service_type_parameter_vehicle.service.ServiceTypeParameterVehicleService;
import lk.nandana_motors.asset.vehicle.service.VehicleService;
import lk.nandana_motors.util.service.DateTimeAgeService;
import lk.nandana_motors.util.service.EmailService;
import lk.nandana_motors.util.service.TwilioMessageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Controller
@RequestMapping( "/payment" )
public class PaymentController {
  private final PaymentService paymentService;
  private final CustomerService customerService;
  private final VehicleService vehicleService;
  private final DateTimeAgeService dateTimeAgeService;
  private final EmailService emailService;
  private final TwilioMessageService twilioMessageService;
  private final DiscountRatioService discountRatioService;
  private final ServiceTypeParameterVehicleService serviceTypeParameterVehicleService;

  public PaymentController(PaymentService paymentService, CustomerService customerService,
                           VehicleService vehicleService, DateTimeAgeService dateTimeAgeService,
                           EmailService emailService, TwilioMessageService twilioMessageService, DiscountRatioService discountRatioService,
                           ServiceTypeParameterVehicleService serviceTypeParameterVehicleService) {
    this.paymentService = paymentService;
    this.customerService = customerService;
    this.vehicleService = vehicleService;
    this.dateTimeAgeService = dateTimeAgeService;
    this.emailService = emailService;
    this.twilioMessageService = twilioMessageService;
    this.discountRatioService = discountRatioService;
    this.serviceTypeParameterVehicleService = serviceTypeParameterVehicleService;
  }

  @GetMapping
  public String allPayment(Model model) {
    model.addAttribute("action", "/payment/allCustom");
    model.addAttribute("payments",
                       paymentService.findAll()
                           .stream()
                           .filter(x -> x.getLiveDead().equals(LiveDead.ACTIVE))
                           .collect(Collectors.toList()));
    return "payment/payment";
  }

  @PostMapping( "/allCustom" )
  public String allPaymentCustom(@ModelAttribute TwoDate twoDate, Model model) {
    model.addAttribute("action", "/payment/allCustom");
    LocalDateTime form = dateTimeAgeService.dateTimeToLocalDateStartInDay(twoDate.getStartDate());
    LocalDateTime to = dateTimeAgeService.dateTimeToLocalDateEndInDay(twoDate.getEndDate());
    model.addAttribute("payments",
                       paymentService.findByCreatedAtIsBetween(form, to)
                           .stream()
                           .filter(x -> x.getLiveDead().equals(LiveDead.ACTIVE))
                           .collect(Collectors.toList()));
    return "payment/payment";
  }

  @GetMapping( "/notPaid" )
  public String allNotPayment(Model model) {
    model.addAttribute("action", "/payment/notPaidCustom");
    model.addAttribute("payments",
                       paymentService.findAll()
                           .stream()
                           .filter(x -> x.getPaymentStatus().equals(PaymentStatus.NOTPAID))
                           .collect(Collectors.toList()));
    return "payment/payment";
  }

  @PostMapping( "/notPaidCustom" )
  public String allNotPaymentCustom(@ModelAttribute TwoDate twoDate, Model model) {
    model.addAttribute("action", "/payment/notPaidCustom");
    LocalDateTime form = dateTimeAgeService.dateTimeToLocalDateStartInDay(twoDate.getStartDate());
    LocalDateTime to = dateTimeAgeService.dateTimeToLocalDateEndInDay(twoDate.getEndDate());
    model.addAttribute("payments",
                       paymentService.findByCreatedAtIsBetween(form, to)
                           .stream()
                           .filter(x -> x.getLiveDead().equals(LiveDead.ACTIVE) || x.getPaymentStatus().equals(PaymentStatus.NOTPAID))
                           .collect(Collectors.toList()));
    return "payment/payment";
  }

  @GetMapping( "/cancel" )
  public String cancelPayments(Model model) {
    model.addAttribute("action", "/payment/cancelCustom");
    model.addAttribute("payments",
                       paymentService.findAll()
                           .stream()
                           .filter(x -> x.getPaymentStatus().equals(PaymentStatus.CANCEL))
                           .collect(Collectors.toList()));
    return "payment/payment";
  }

  @PostMapping( "/cancelCustom" )
  public String cancelPaymentCustome(@ModelAttribute TwoDate twoDate, Model model) {
    model.addAttribute("action", "/payment/cancelCustom");
    LocalDateTime form = dateTimeAgeService.dateTimeToLocalDateStartInDay(twoDate.getStartDate());
    LocalDateTime to = dateTimeAgeService.dateTimeToLocalDateEndInDay(twoDate.getEndDate());
    model.addAttribute("payments",
                       paymentService.findByCreatedAtIsBetween(form, to)
                           .stream()
                           .filter(x -> x.getLiveDead().equals(LiveDead.ACTIVE) || x.getPaymentStatus().equals(PaymentStatus.CANCEL))
                           .collect(Collectors.toList()));
    return "payment/payment";
  }

  @GetMapping( "/cancel/{id}" )
  public String cancelPayment(@PathVariable( "id" ) Integer id) {
    Payment payment = paymentService.findById(id);
    payment.setPaymentStatus(PaymentStatus.CANCEL);
    paymentService.persist(payment);
    return "redirect:/payment";
  }

  @GetMapping( "/pay/{id}" )
  public String pay(@PathVariable( "id" ) Integer id, Model model) {
    Payment payment = paymentService.findById(id);
    model.addAttribute("paymentMethods", PaymentMethod.values());
    model.addAttribute("discountRatios", discountRatioService.findAll());
    model.addAttribute("payment", payment);
    model.addAttribute("customerDetail", customerService.findById(payment.getCustomer().getId()));
    model.addAttribute("vehicleDetail", vehicleService.findById(payment.getVehicle().getId()));
    return "payment/addPayment";
  }

  @PostMapping( "/add" )
  public String persistPayment(@ModelAttribute Payment payment, BindingResult bindingResult) {
    if ( bindingResult.hasErrors() ) {
      return "redirect:/payment/pay/" + payment.getId();
    }
    Payment paymentDb = paymentService.persist(payment);
    Customer customer = customerService.findById(paymentDb.getCustomer().getId());
//todo email and message
    if (customer.getEmail() != null){
      String message = "Thanks for our payment";
      //emailService.sendEmail();
    }
    if (customer.getMobile() != null){
      String message = "Thanks for our payment";
     // twilioMessageService.sendSMS();
    }


    serviceTypeParameterVehicleService
        .findByVehicleAndServiceTypeAndServiceTypeParameterVehicleStatus(paymentDb.getVehicle(),
                                                                         paymentDb.getServiceType(),
                                                                         ServiceTypeParameterVehicleStatus.DONE)
        .forEach(x -> {
          x.setServiceTypeParameterVehicleStatus(ServiceTypeParameterVehicleStatus.PAID);
          serviceTypeParameterVehicleService.persist(x);
        });

    return "redirect:/payment/notPaid";
  }

}
