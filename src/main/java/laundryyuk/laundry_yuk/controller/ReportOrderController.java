package laundryyuk.laundry_yuk.controller;

import jakarta.validation.Valid;
import laundryyuk.laundry_yuk.domain.Admin;
import laundryyuk.laundry_yuk.domain.Order;
import laundryyuk.laundry_yuk.model.ReportOrderDTO;
import laundryyuk.laundry_yuk.repos.AdminRepository;
import laundryyuk.laundry_yuk.repos.OrderRepository;
import laundryyuk.laundry_yuk.service.ReportOrderService;
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
@RequestMapping("/reportOrders")
public class ReportOrderController {

    private final ReportOrderService reportOrderService;
    private final AdminRepository adminRepository;
    private final OrderRepository orderRepository;

    public ReportOrderController(final ReportOrderService reportOrderService,
            final AdminRepository adminRepository, final OrderRepository orderRepository) {
        this.reportOrderService = reportOrderService;
        this.adminRepository = adminRepository;
        this.orderRepository = orderRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("adminValues", adminRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Admin::getId, Admin::getId)));
        model.addAttribute("ordersValues", orderRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Order::getId, Order::getId)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("reportOrders", reportOrderService.findAll());
        return "reportOrder/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("reportOrder") final ReportOrderDTO reportOrderDTO) {
        return "reportOrder/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("reportOrder") @Valid final ReportOrderDTO reportOrderDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "reportOrder/add";
        }
        reportOrderService.create(reportOrderDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("reportOrder.create.success"));
        return "redirect:/reportOrders";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("reportOrder", reportOrderService.get(id));
        return "reportOrder/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("reportOrder") @Valid final ReportOrderDTO reportOrderDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "reportOrder/edit";
        }
        reportOrderService.update(id, reportOrderDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("reportOrder.update.success"));
        return "redirect:/reportOrders";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        reportOrderService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("reportOrder.delete.success"));
        return "redirect:/reportOrders";
    }

}
