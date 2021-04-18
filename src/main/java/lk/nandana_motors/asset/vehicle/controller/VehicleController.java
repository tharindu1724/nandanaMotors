package lk.nandana_motors.asset.vehicle.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lk.nandana_motors.asset.common_asset.model.Enum.LiveDead;
import lk.nandana_motors.asset.customer.service.CustomerService;
import lk.nandana_motors.asset.vehicle.entity.Vehicle;
import lk.nandana_motors.asset.vehicle.entity.enums.GearType;
import lk.nandana_motors.asset.vehicle.entity.enums.VehicleModel;
import lk.nandana_motors.asset.vehicle.service.VehicleService;
import lk.nandana_motors.util.interfaces.AbstractController;
import lk.nandana_motors.util.service.MakeAutoGenerateNumberService;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
@RequestMapping( "/vehicle" )
public class VehicleController implements AbstractController< Vehicle, Integer > {

  private final VehicleService vehicleService;
  private final CustomerService customerService;
  private final MakeAutoGenerateNumberService makeAutoGenerateNumberService;

  public VehicleController(VehicleService vehicleService, CustomerService customerService,
                           MakeAutoGenerateNumberService makeAutoGenerateNumberService) {
    this.vehicleService = vehicleService;
    this.customerService = customerService;
    this.makeAutoGenerateNumberService = makeAutoGenerateNumberService;
  }

  @GetMapping
  public String findAll(Model model) {
    model.addAttribute("vehicles", vehicleService.findAll()
        .stream()
        .filter(x -> LiveDead.ACTIVE.equals(x.getLiveDead()))
        .collect(Collectors.toList()));
    return "vehicle/vehicle";
  }

  @GetMapping( "/{id}" )
  public String findById(@PathVariable Integer id, Model model) {
    model.addAttribute("vehicle", vehicleService.findById(id));
    return "vehicle/vehicle-detail";
  }

  @GetMapping( "/edit/{id}" )
  public String edit(@PathVariable Integer id, Model model) {
    model.addAttribute("customers", customerService.findAll());
    model.addAttribute("vehicleModels", VehicleModel.values());
    model.addAttribute("addStatus", true);
    model.addAttribute("gearTypes", GearType.values());
    model.addAttribute("vehicle", vehicleService.findById(id));
    return "vehicle/addVehicle";
  }

  @PostMapping( value = {"/save", "/update"} )
  public String persist(@Valid @ModelAttribute Vehicle vehicle, BindingResult bindingResult,
                        RedirectAttributes redirectAttributes, Model model) {
    if ( bindingResult.hasErrors() ) {
      model.addAttribute("customers", customerService.findAll());
      model.addAttribute("vehicleModels", VehicleModel.values());
      model.addAttribute("addStatus", false);
      model.addAttribute("vehicle", vehicle);
      model.addAttribute("gearTypes", GearType.values());
      return "vehicle/addVehicle";
    }
    vehicle.setNumber(vehicle.getNumber().toUpperCase());
    //if customer has id that customer is not a new customer
    if ( vehicle.getId() == null ) {
      //if there is not customer in db
      if ( vehicleService.lastVehicle() == null ) {
        System.out.println("last customer null");
        //need to generate new one customer
        vehicle.setRegistrationNumber("SAV" + makeAutoGenerateNumberService.numberAutoGen(null).toString());
      } else {
        System.out.println("last customer not null");
        //if there is customer in db need to get that customer's code and increase its value
        String previousCode = vehicleService.lastVehicle().getRegistrationNumber().substring(3);
        vehicle.setRegistrationNumber("SAV" + makeAutoGenerateNumberService.numberAutoGen(previousCode).toString());
      }
      //send welcome message and email
      if ( vehicle.getCustomer().getEmail() != null ) {
        //  emailService.sendEmail(customer.getEmail(), "Welcome Message", "Welcome to Kmart Super...");
      }
      if ( vehicle.getCustomer().getMobile() != null ) {
        //    twilioMessageService.sendSMS(customer.getMobile(), "Welcome to Kmart Super");
      }
    }
    redirectAttributes.addFlashAttribute("vehicleDetails", vehicleService.persist(vehicle));
    return "redirect:/vehicle";
  }

  @GetMapping( "/add" )
  public String addForm(Model model) {
    model.addAttribute("customers", customerService.findAll());
    model.addAttribute("vehicleModels", VehicleModel.values());
    model.addAttribute("addStatus", false);
    model.addAttribute("vehicle", new Vehicle());
    model.addAttribute("gearTypes", GearType.values());
    return "vehicle/addVehicle";
  }

  @GetMapping( "/delete/{id}" )
  public String delete(@PathVariable Integer id, Model model) {
    vehicleService.delete(id);
    return "redirect:/vehicle";
  }

  @GetMapping( "/view/{id}" )
  public String view(@PathVariable Integer id, Model model) {
    model.addAttribute("vehicleDetail", vehicleService.findById(id));
    return "vehicle/vehicle-detail";
  }


  @GetMapping( "find/{number}" )
  @ResponseBody
  public MappingJacksonValue findByNumber(@PathVariable( "number" ) String number) {
    MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(vehicleService.findByNumber(number));
    SimpleBeanPropertyFilter simpleBeanPropertyFilterForVehicle = SimpleBeanPropertyFilter
        .filterOutAllExcept("id", "number", "registrationNumber", "chassisNumber", "engineNumber");

    SimpleBeanPropertyFilter simpleBeanPropertyFilterCustomer = SimpleBeanPropertyFilter
        .filterOutAllExcept("id", "name", "mobile");

    FilterProvider filters = new SimpleFilterProvider()
        .addFilter("Customer", simpleBeanPropertyFilterCustomer)
        .addFilter("Vehicle", simpleBeanPropertyFilterForVehicle);
    mappingJacksonValue.setFilters(filters);
    return mappingJacksonValue;
  }


//ORDER FOLLOW
  //================//
  //1. Entity -> Relevant parameter (instance)
  //2. Dao/Repository -> database managing
  //3. Service -> controller and repository management
  //1.findAll
  //2.findById
  //3.persist
  //4.delete
  //5.search
  //4. Controller -> view manage (HTML)
  //1.findAll
  //2.findById
  //3.edit
  //4.persist
  //5.delete
  //6.form
  //5. Html / Views -> data collect from frontend to backend and data display backend to frontend
  //1. relevant entity name html
  //2. addForm
  //3. details show

}
