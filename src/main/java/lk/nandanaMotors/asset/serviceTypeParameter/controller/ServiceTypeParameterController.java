package lk.nandanaMotors.asset.serviceTypeParameter.controller;



import lk.nandanaMotors.asset.serviceTypeParameter.entity.ServiceTypeParameter;
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
@RequestMapping("/serviceTypeParameter")
public class ServiceTypeParameterController {
    private final ServiceTypeParameterService serviceTypeParameterService;

    public ServiceTypeParameterController(ServiceTypeParameterService serviceTypeParameterService) {
        this.serviceTypeParameterService = serviceTypeParameterService;
    }

    private String commonThing(Model model, Boolean booleanValue, ServiceTypeParameter serviceTypeParameter) {
        model.addAttribute("addStatus", booleanValue);
        model.addAttribute("serviceTypeParameter", serviceTypeParameter);
        return "serviceTypeParameter/addServiceTypeParameter";
    }


    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("serviceTypeParameters", serviceTypeParameterService.findAll());
        return "serviceTypeParameter/serviceTypeParameter";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        return commonThing(model, false, new ServiceTypeParameter());
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Integer id, Model model) {
        model.addAttribute("serviceTypeParameterDetail", serviceTypeParameterService.findById(id));
        return "serviceTypeParameter/serviceTypeParameter-detail";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        return commonThing(model, true, serviceTypeParameterService.findById(id));
    }

    @PostMapping(value = {"/save", "/update"})
    public String persist(@Valid ServiceTypeParameter serviceTypeParameter, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) throws Exception {
        if (bindingResult.hasErrors()) {
            return commonThing(model, true, serviceTypeParameter);
        }
        redirectAttributes.addFlashAttribute("serviceTypeParameterDetail", serviceTypeParameterService.persist(serviceTypeParameter));
        return "redirect:/serviceTypeParameter";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, Model model) {
        serviceTypeParameterService.delete(id);
        return "redirect:/serviceTypeParameter";
    }
}
