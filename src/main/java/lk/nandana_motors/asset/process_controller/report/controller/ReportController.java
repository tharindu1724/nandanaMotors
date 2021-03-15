package lk.nandana_motors.asset.process_controller.report.controller;

import lk.nandana_motors.asset.common_asset.model.AccordingToUserAllDetail;
import lk.nandana_motors.asset.common_asset.model.Enum.LiveDead;
import lk.nandana_motors.asset.common_asset.model.NameCount;
import lk.nandana_motors.asset.common_asset.model.ParameterCountPrice;
import lk.nandana_motors.asset.common_asset.model.TwoDate;
import lk.nandana_motors.asset.employee.entity.Employee;
import lk.nandana_motors.asset.employee.service.EmployeeService;
import lk.nandana_motors.asset.payment.entity.Payment;
import lk.nandana_motors.asset.payment.entity.enums.PaymentMethod;
import lk.nandana_motors.asset.payment.entity.enums.PaymentStatus;
import lk.nandana_motors.asset.payment.service.PaymentService;
import lk.nandana_motors.asset.serviceType.entity.ServiceType;
import lk.nandana_motors.asset.serviceType.service.ServiceTypeService;
import lk.nandana_motors.asset.user.service.UserService;
import lk.nandana_motors.asset.vehicle.entity.Enum.VehicleModel;
import lk.nandana_motors.util.service.DateTimeAgeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping( "/report" )
public class ReportController {
  private final PaymentService paymentService;
  private final DateTimeAgeService dateTimeAgeService;
  private final ServiceTypeService serviceTypeService;
  private final UserService userService;
  private final EmployeeService employeeService;

  public ReportController(PaymentService paymentService, DateTimeAgeService dateTimeAgeService,
                          ServiceTypeService serviceTypeService, UserService userService,
                          EmployeeService employeeService) {
    this.paymentService = paymentService;
    this.dateTimeAgeService = dateTimeAgeService;
    this.serviceTypeService = serviceTypeService;
    this.userService = userService;
    this.employeeService = employeeService;
  }

  private String common(List< Payment > payments, Model model) {
    List< Payment > allPaid = paymentsLiveDeadAndPaymentStatus(payments, LiveDead.ACTIVE, PaymentStatus.PAID);
    List< Payment > allNotPaid = paymentsLiveDeadAndPaymentStatus(payments, LiveDead.ACTIVE, PaymentStatus.NOTPAID);
    List< Payment > allCancel = paymentsLiveDeadAndPaymentStatus(payments, LiveDead.ACTIVE, PaymentStatus.CANCEL);
//allPaid vehicle models name and a count and PaymentTypes, count and prices
    if ( !allPaid.isEmpty() ) {
      model.addAttribute("paidServiceTypeAndCounts", vehicleModelAndServiceTypeCount(allPaid));
      model.addAttribute("paymentTypeCountAndTotalAmounts", paymentStatusAndValues(allPaid));
    }

//allNotPaid vehicle models name and a count and PaymentTypes, count and prices
    if ( !allNotPaid.isEmpty() ) {
      model.addAttribute("allNotPaidServiceTypeAndCounts", vehicleModelAndServiceTypeCount(allNotPaid));
      model.addAttribute("allNotPaidTypeCountAndTotalAmounts", paymentStatusAndValues(allNotPaid));
    }

//allCancel vehicle models name and a count and PaymentTypes, count and prices
    if ( !allCancel.isEmpty() ) {
      model.addAttribute("allCancelPaidServiceTypeAndCounts", vehicleModelAndServiceTypeCount(allCancel));
      model.addAttribute("allCancelTypeCountAndTotalAmounts", paymentStatusAndValues(allCancel));
    }

    //according to user,
    model.addAttribute("accordingToUserAllDetails", accordingToUserAllDetail(payments));

    return "report/allReport";
  }

  //paymentsLiveDeadAndPaymentStatus
  private List< Payment > paymentsLiveDeadAndPaymentStatus(List< Payment > payments, LiveDead liveDead,
                                                           PaymentStatus paymentStatus) {
    return payments.stream()
        .filter(x -> x.getLiveDead().equals(liveDead) && x.getPaymentStatus().equals(paymentStatus))
        .collect(Collectors.toList());
  }

  //a vehicle models name and a count
  private List< NameCount > vehicleModelAndServiceTypeCount(List< Payment > payments) {
    List< NameCount > serviceTypeAndCounts = new ArrayList<>();
    for ( VehicleModel value : VehicleModel.values() ) {
      NameCount serviceTypeAndCount = new NameCount();
      List< ServiceType > serviceTypes = new ArrayList<>();
      payments.forEach(x -> {
        ServiceType serviceType = serviceTypeService.findById(x.getServiceType().getId());
        if (serviceType.getVehicleModel().equals(value) ) {
          serviceTypes.add(serviceType);
        }
      });
      serviceTypeAndCount.setName(value.getVehicleModel());
      serviceTypeAndCount.setCount(serviceTypes.size());
      serviceTypeAndCounts.add(serviceTypeAndCount);

    }
    return serviceTypeAndCounts;
  }

  //payment Status and values
  private List< ParameterCountPrice > paymentStatusAndValues(List< Payment > payments) {
    List< ParameterCountPrice > paymentTypeCountAndTotalAmounts = new ArrayList<>();
    for ( PaymentMethod value : PaymentMethod.values() ) {
      ParameterCountPrice paymentTypeCountAndTotalAmount = new ParameterCountPrice();
      List< Payment > accordingToPaymentMethod = payments
          .stream()
          .filter(x -> x.getPaymentMethod().equals(value))
          .collect(Collectors.toList());
      List< BigDecimal > amounts = new LinkedList<>();
      List< BigDecimal > totalDiscountAmounts = new LinkedList<>();
      List< BigDecimal > totalAmounts = new LinkedList<>();
      accordingToPaymentMethod.forEach(x -> {
        amounts.add(x.getAmount());
        totalDiscountAmounts.add(x.getDiscountAmount());
        totalAmounts.add(x.getAmount());
      });

      paymentTypeCountAndTotalAmount.setName(value.getPaymentMethod());
      paymentTypeCountAndTotalAmount.setCount(accordingToPaymentMethod.size());
      paymentTypeCountAndTotalAmount.setAmount(amounts.stream().reduce(BigDecimal.ZERO, BigDecimal::add));
      paymentTypeCountAndTotalAmount.setDiscountAmount(totalDiscountAmounts.stream().reduce(BigDecimal.ZERO,
                                                                                            BigDecimal::add));
      paymentTypeCountAndTotalAmount.setTotalAmount(totalAmounts.stream().reduce(BigDecimal.ZERO, BigDecimal::add));
      paymentTypeCountAndTotalAmounts.add(paymentTypeCountAndTotalAmount);
    }
    return paymentTypeCountAndTotalAmounts;
  }

  //according to user detail
  private List< AccordingToUserAllDetail > accordingToUserAllDetail(List< Payment > payments) {
    List< AccordingToUserAllDetail > accordingToUserAllDetails = new ArrayList<>();
    for ( String userName : userNames(payments) ) {
      if ( userName != null ) {
        AccordingToUserAllDetail accordingToUserAllDetail = new AccordingToUserAllDetail();

        Employee employee = employeeService.findById(userService.findByUserName(userName).getEmployee().getId());
        String employeeName = "Code : " + employee.getCode() + " Name : " + employee.getName();
        List< Payment > paymentAccordingToUser =
            payments.stream().filter(y -> y.getUpdatedBy().equals(userName)).collect(Collectors.toList());

        accordingToUserAllDetail.setEmployeeName(employeeName);

        List< Payment > allPaid = paymentsLiveDeadAndPaymentStatus(paymentAccordingToUser, LiveDead.ACTIVE,
                                                                   PaymentStatus.PAID);
        List< Payment > allNotPaid = paymentsLiveDeadAndPaymentStatus(paymentAccordingToUser, LiveDead.ACTIVE,
                                                                      PaymentStatus.NOTPAID);
        List< Payment > allCancel = paymentsLiveDeadAndPaymentStatus(paymentAccordingToUser, LiveDead.ACTIVE,
                                                                     PaymentStatus.CANCEL);
//allPaid vehicle models name and a count and PaymentTypes, count and prices
        if ( !allPaid.isEmpty() ) {
          accordingToUserAllDetail.setPaidServiceTypeAndCounts(vehicleModelAndServiceTypeCount(allPaid));
          accordingToUserAllDetail.setPaymentTypeCountAndTotalAmounts(paymentStatusAndValues(allPaid));
        }

//allNotPaid vehicle models name and a count and PaymentTypes, count and prices
        if ( !allNotPaid.isEmpty() ) {
          accordingToUserAllDetail.setAllNotPaidServiceTypeAndCounts(vehicleModelAndServiceTypeCount(allNotPaid));
          accordingToUserAllDetail.setAllNotPaidTypeCountAndTotalAmounts(paymentStatusAndValues(allNotPaid));
        }

//allCancel vehicle models name and a count and PaymentTypes, count and prices
        if ( !allCancel.isEmpty() ) {
          accordingToUserAllDetail.setAllCancelPaidServiceTypeAndCounts(vehicleModelAndServiceTypeCount(allCancel));
          accordingToUserAllDetail.setAllCancelTypeCountAndTotalAmounts(paymentStatusAndValues(allCancel));
        }

        accordingToUserAllDetails.add(accordingToUserAllDetail);
      }
    }
    return accordingToUserAllDetails;
  }

  //all user name interactions with payment
  private HashSet< String > userNames(List< Payment > payments) {
    HashSet< String > userNames = new HashSet<>();
    payments.forEach(x -> userNames.add(x.getUpdatedBy()));
    return userNames;
  }


  @GetMapping
  public String allTogether(Model model) {
    LocalDate today = LocalDate.now();
    LocalDateTime form = dateTimeAgeService.dateTimeToLocalDateStartInDay(today);
    LocalDateTime to = dateTimeAgeService.dateTimeToLocalDateEndInDay(today);

    List< Payment > payments = paymentService.findByCreatedAtIsBetween(form, to);
    return common(payments, model);
  }

  @PostMapping
  public String allTogetherCustom(@ModelAttribute TwoDate twoDate, Model model) {
    LocalDateTime form = dateTimeAgeService.dateTimeToLocalDateStartInDay(twoDate.getStartDate());
    LocalDateTime to = dateTimeAgeService.dateTimeToLocalDateEndInDay(twoDate.getEndDate());

    List< Payment > payments = paymentService.findByCreatedAtIsBetween(form, to);
    return common(payments, model);
  }



}
