package laundryyuk.laundry_yuk.controller;

import jakarta.validation.Valid;
import laundryyuk.laundry_yuk.domain.Admin;
import laundryyuk.laundry_yuk.domain.Payment;
import laundryyuk.laundry_yuk.model.ReportIncomeDTO;
import laundryyuk.laundry_yuk.repos.AdminRepository;
import laundryyuk.laundry_yuk.repos.PaymentRepository;
import laundryyuk.laundry_yuk.service.ReportIncomeService;
import laundryyuk.laundry_yuk.util.CustomCollectors;
import laundryyuk.laundry_yuk.util.WebUtils;
import org.springframework.data.domain.Sort;
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
@RequestMapping("/reportIncomes")
public class ReportIncomeController {

    private final ReportIncomeService reportIncomeService;
    private final AdminRepository adminRepository;
    private final PaymentRepository paymentRepository;

    public ReportIncomeController(final ReportIncomeService reportIncomeService,
            final AdminRepository adminRepository, final PaymentRepository paymentRepository) {
        this.reportIncomeService = reportIncomeService;
        this.adminRepository = adminRepository;
        this.paymentRepository = paymentRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("adminValues", adminRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Admin::getId, Admin::getId)));
        model.addAttribute("paymentsValues", paymentRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Payment::getId, Payment::getId)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("reportIncomes", reportIncomeService.findAll());
        return "reportIncome/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("reportIncome") final ReportIncomeDTO reportIncomeDTO) {
        return "reportIncome/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("reportIncome") @Valid final ReportIncomeDTO reportIncomeDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "reportIncome/add";
        }
        reportIncomeService.create(reportIncomeDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("reportIncome.create.success"));
        return "redirect:/reportIncomes";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("reportIncome", reportIncomeService.get(id));
        return "reportIncome/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("reportIncome") @Valid final ReportIncomeDTO reportIncomeDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "reportIncome/edit";
        }
        reportIncomeService.update(id, reportIncomeDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("reportIncome.update.success"));
        return "redirect:/reportIncomes";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        reportIncomeService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("reportIncome.delete.success"));
        return "redirect:/reportIncomes";
    }

}
