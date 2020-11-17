package lk.nandanaMotors.asset.vehicle.controller;

import lk.nandanaMotors.asset.customer.service.CustomerService;
import lk.nandanaMotors.asset.vehicle.entity.Enum.VehicleModel;
import lk.nandanaMotors.asset.vehicle.entity.Vehicle;
import lk.nandanaMotors.asset.vehicle.service.VehicleService;
import lk.nandanaMotors.util.interfaces.AbstractController;
import lk.nandanaMotors.util.service.MakeAutoGenerateNumberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

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
        model.addAttribute("vehicles", vehicleService.findAll());
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
            return "vehicle/addVehicle";
        }
        //if customer has id that customer is not a new customer
        if (vehicle.getId() == null) {
            //if there is not customer in db
            if (customerService.lastCustomer() == null) {
                System.out.println("last customer null");
                //need to generate new one customer
                vehicle.setRegistrationNumber("GRI"+makeAutoGenerateNumberService.numberAutoGen(null).toString());
            } else {
                System.out.println("last customer not null");
                //if there is customer in db need to get that customer's code and increase its value
                String previousCode = customerService.lastCustomer().getCode().substring(3);
                vehicle.setRegistrationNumber("GRI"+makeAutoGenerateNumberService.numberAutoGen(previousCode).toString());
            }
            //send welcome message and email
            if (vehicle.getCustomer().getEmail() != null) {
                //  emailService.sendEmail(customer.getEmail(), "Welcome Message", "Welcome to Kmart Super...");
            }
            if (vehicle.getCustomer().getMobile() != null) {
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
        return "vehicle/addVehicle";
    }

    @GetMapping( "/delete/{id}" )
    public String delete(@PathVariable Integer id, Model model) {
        vehicleService.delete(id);
        return "redirect:/vehicle";
    }

    @GetMapping( "/view/{id}" )
    public String view(@PathVariable Integer id, Model model) {
        model.addAttribute("vehicleDetails", vehicleService.findById(id));
        return "vehicle/vehicle-detail";
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
