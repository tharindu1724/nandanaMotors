package lk.nandanaMotors.asset.serviceType.controller;


import lk.nandanaMotors.asset.serviceType.entity.ServiceType;
import lk.nandanaMotors.asset.serviceType.service.ServiceTypeService;
import lk.nandanaMotors.asset.serviceTypeParameter.service.ServiceTypeParameterService;
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
@RequestMapping("/serviceType")
public class ServiceTypeController {
    private final ServiceTypeService serviceTypeService;
    private final ServiceTypeParameterService serviceTypeParameterService;

    public ServiceTypeController(ServiceTypeService serviceTypeService, ServiceTypeParameterService serviceTypeParameterService) {
        this.serviceTypeService = serviceTypeService;
        this.serviceTypeParameterService = serviceTypeParameterService;
    }

    private String commonThing(Model model, Boolean booleanValue, ServiceType serviceType) {
        model.addAttribute("addStatus", booleanValue);
        model.addAttribute("serviceType", serviceType);
        model.addAttribute("serviceTypeParameters", serviceTypeParameterService.findAll());
        return "serviceType/addServiceType";
    }


    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("serviceTypes", serviceTypeService.findAll());
        return "serviceType/serviceType";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        return commonThing(model, false, new ServiceType());
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Integer id, Model model) {
        model.addAttribute("serviceTypeDetail", serviceTypeService.findById(id));
        return "serviceType/serviceType-detail";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        return commonThing(model, true, serviceTypeService.findById(id));
    }

    @PostMapping(value = {"/save", "/update"})
    public String persist(@Valid ServiceType serviceType, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) throws Exception {
        if (bindingResult.hasErrors()) {
            return commonThing(model, true, serviceType);
        }
        redirectAttributes.addFlashAttribute("serviceTypeDetail", serviceTypeService.persist(serviceType));
        return "redirect:/serviceType";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, Model model) {
        serviceTypeService.delete(id);
        return "redirect:/serviceType";
    }
}
