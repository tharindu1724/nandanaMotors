package lk.nandana_motors.asset.process_controller;

import lk.nandana_motors.asset.common_asset.model.Enum.LiveDead;
import lk.nandana_motors.asset.serviceType.controller.ServiceTypeController;
import lk.nandana_motors.asset.serviceType.entity.ServiceType;
import lk.nandana_motors.asset.serviceType.service.ServiceTypeService;
import lk.nandana_motors.asset.service_type_parameter_vehicle.entity.ServiceTypeParameterVehicle;
import lk.nandana_motors.asset.service_type_parameter_vehicle.entity.enums.ServiceTypeParameterVehicleStatus;
import lk.nandana_motors.asset.service_type_parameter_vehicle.service.ServiceTypeParameterVehicleService;
import lk.nandana_motors.asset.vehicle.entity.Vehicle;
import lk.nandana_motors.asset.vehicle.service.VehicleService;
import lk.nandana_motors.util.service.DateTimeAgeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping( "/inspection" )
public class InspectionController {
  private final VehicleService vehicleService;
  private final ServiceTypeService serviceTypeService;
  private final ServiceTypeParameterVehicleService serviceTypeParameterVehicleService;
  private final DateTimeAgeService dateTimeAgeService;

  public InspectionController(VehicleService vehicleService,
                              ServiceTypeService serviceTypeService,
                              ServiceTypeParameterVehicleService serviceTypeParameterVehicleService,
                              DateTimeAgeService dateTimeAgeService) {
    this.vehicleService = vehicleService;
    this.serviceTypeService = serviceTypeService;
    this.serviceTypeParameterVehicleService = serviceTypeParameterVehicleService;
    this.dateTimeAgeService = dateTimeAgeService;
  }

  @GetMapping
  public String getInspectionForm(Model model) {
    model.addAttribute("vehicleSearch", true);
    model.addAttribute("vehicle", new Vehicle());
    return "inspection/inspectionForm";
  }

  @PostMapping( value = "/searchAll" )
  public String addUserEmployeeDetails(@ModelAttribute( "vehicle" ) Vehicle vehicle, Model model) {

    List< Vehicle > vehicles = vehicleService.search(vehicle);

    if ( vehicles.size() == 1 ) {
      Vehicle vehicleDB = vehicles.get(0);
      model.addAttribute("vehicleDetail", vehicleDB);
      model.addAttribute("customerDetail", vehicleDB.getCustomer());
      model.addAttribute("serviceTypeParameterVehicle", new ServiceTypeParameterVehicle());
      model.addAttribute("addStatus", true);
      model.addAttribute("vehicleSearch", false);
      model.addAttribute("serviceTypes", serviceTypeService.findAll()
          .stream()
          .filter(x -> x.getVehicleModel().equals(vehicleDB.getVehicleModel()) && x.getLiveDead().equals(LiveDead.ACTIVE))
          .collect(Collectors.toList()));
      model.addAttribute("serviceParameterUrl", MvcUriComponentsBuilder
          .fromMethodName(ServiceTypeController.class, "findId", "")
          .build()
          .toString());
      return "inspection/inspectionForm";
    }

    model.addAttribute("vehicleSearch", true);
    model.addAttribute("vehicle", new Vehicle());
    model.addAttribute("vehicles", vehicles);
    model.addAttribute("employeeDetailShow", false);
    model.addAttribute("employeeNotFoundShow", true);
    model.addAttribute("employeeNotFound", "There is not vehicle in the system according to the provided details" +
        " \n Could you please search again !!");

    return "inspection/inspectionForm";
  }

  @GetMapping( "/select/{id}" )
  public String vehicleDetail(@PathVariable( "id" ) Integer id, Model model) {
    Vehicle vehicle = vehicleService.findById(id);
    model.addAttribute("vehicleDetail", vehicle);
    model.addAttribute("customerDetail", vehicle.getCustomer());
    model.addAttribute("serviceTypeParameterVehicle", new ServiceTypeParameterVehicle());
    return "inspection/inspectionForm";
  }

  @PostMapping( "/save" )
  public String save(@Valid @ModelAttribute( "serviceTypeParameterVehicle" ) ServiceTypeParameterVehicle serviceTypeParameterVehicle, BindingResult bindingResult, Model model) {

    if ( bindingResult.hasErrors() ) {
      return "redirect:/inspection";
    }

    for ( ServiceType serviceType : serviceTypeParameterVehicle.getServiceTypes() ) {
      serviceType.getServiceTypeParameters().forEach(x -> {
        ServiceTypeParameterVehicle serviceTypeParameterVehicleDB = new ServiceTypeParameterVehicle();
        serviceTypeParameterVehicleDB.setServiceType(serviceType);
        serviceTypeParameterVehicleDB.setServiceTypeParameter(x);
        serviceTypeParameterVehicleDB.setVehicle(vehicleService.findById(serviceTypeParameterVehicle.getVehicle().getId()));
        serviceTypeParameterVehicleDB.setMeterValue(serviceTypeParameterVehicle.getMeterValue());
        //this service type parameter already save or not in db
        if ( serviceTypeParameterVehicleService.search(serviceTypeParameterVehicleDB)
            .stream().noneMatch(y -> y.getCreatedAt().toLocalDate().equals(LocalDate.now())) ) {
          serviceTypeParameterVehicleDB.setServiceTypeParameterVehicleStatus(ServiceTypeParameterVehicleStatus.CHK);
          serviceTypeParameterVehicleService.persist(serviceTypeParameterVehicleDB);
        }
      });
    }

    return "redirect:/inspection";
  }
}
