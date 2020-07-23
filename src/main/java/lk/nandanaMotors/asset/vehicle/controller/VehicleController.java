package lk.nandanaMotors.asset.vehicle.controller;

import lk.nandanaMotors.asset.vehicle.entity.Vehicle;
import lk.nandanaMotors.asset.vehicle.service.VehicleService;
import lk.nandanaMotors.util.interfaces.AbstractController;
import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/vehicle")
public class VehicleController implements AbstractController<Vehicle, Integer> {
    //ORDER FOLLOW
    //================//
    //1. Entity -> Relevant parameter
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
    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("vehicles", vehicleService.findAll());
        return "vehicle/vehicle";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Integer id, Model model) {
        model.addAttribute("vehicle", vehicleService.findById(id));
        return "vehicle/vehicle-detail";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        model.addAttribute("vehicle", vehicleService.findById(id));
        return "vehicle/addVehicle";
    }

    @PostMapping("/save")
    public String persist(@Valid Vehicle vehicle, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("vehicle", vehicle);
            return "vehicle/addVehicle";
        }
        redirectAttributes.addFlashAttribute("vehicleDetails", vehicleService.persist(vehicle));
        return "redirect:/vehicle";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("vehicle", new Vehicle());
        return "vehicle/addVehicle";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, Model model) {
        vehicleService.delete(id);
        return "redirect:/vehicle";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Integer id, Model model) {
        model.addAttribute("vehicleDetails", vehicleService.findById(id));
        return "vehicle/vehicle-detail";
    }


}
