package laundryyuk.laundry_yuk.controller;

import jakarta.validation.Valid;
import laundryyuk.laundry_yuk.model.CustomerDTO;
import laundryyuk.laundry_yuk.model.Role;
import laundryyuk.laundry_yuk.service.AdminService;
import laundryyuk.laundry_yuk.service.CustomerService;
import laundryyuk.laundry_yuk.util.WebUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final CustomerService customerService;
    private final AdminService adminService;

    public AuthController(final CustomerService customerService, final AdminService adminService) {
        this.customerService = customerService;
        this.adminService = adminService;
    }

    @GetMapping("/register")
    public String showRegisterForm(@ModelAttribute("customer") final CustomerDTO customerDTO) {
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("customer") @Valid final CustomerDTO customerDTO,
                           final BindingResult bindingResult,
                           final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        if (customerService.emailExists(customerDTO.getEmail())) {
            bindingResult.rejectValue("email", "error.customer", "Email sudah terdaftar.");
            return "auth/register";
        }

        customerDTO.setRole(Role.CUSTOMER);

        customerDTO.setPassword(customerDTO.getPassword());
        customerService.register(customerDTO);

        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, "Registrasi berhasil. Silakan login.");
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("customer") final CustomerDTO customerDTO,
                        final RedirectAttributes redirectAttributes) {
        String email = customerDTO.getEmail();
        String password = customerDTO.getPassword();

        if (adminService.authenticate(email, password)) {
            return "redirect:/";
        }

        if (customerService.authenticate(email, password)) {
            return "redirect:/";
        }

        redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, "Email atau password salah.");
        return "redirect:/login";
    }

    
}
