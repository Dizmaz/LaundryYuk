package laundryyuk.laundry_yuk.controller;

import jakarta.validation.Valid;
import laundryyuk.laundry_yuk.model.AdminDTO;
import laundryyuk.laundry_yuk.model.Role;
import laundryyuk.laundry_yuk.service.AdminService;
import laundryyuk.laundry_yuk.util.ReferencedWarning;
import laundryyuk.laundry_yuk.util.WebUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/admins")
public class AdminController {

    private final AdminService adminService;

    public AdminController(final AdminService adminService) {
        this.adminService = adminService;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("roleValues", Role.values());
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("admins", adminService.findAll());
        return "admin/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("admin") final AdminDTO adminDTO) {
        return "admin/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("admin") @Valid final AdminDTO adminDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "admin/add";
        }
        adminService.create(adminDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("admin.create.success"));
        return "redirect:/admins";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("admin", adminService.get(id));
        return "admin/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("admin") @Valid final AdminDTO adminDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "admin/edit";
        }
        adminService.update(id, adminDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("admin.update.success"));
        return "redirect:/admins";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = adminService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            adminService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("admin.delete.success"));
        }
        return "redirect:/admins";
    }

}
