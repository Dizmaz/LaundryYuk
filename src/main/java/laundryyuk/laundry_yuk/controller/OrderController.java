package laundryyuk.laundry_yuk.controller;

import jakarta.validation.Valid;
import laundryyuk.laundry_yuk.domain.Customer;
import laundryyuk.laundry_yuk.domain.Payment;
import laundryyuk.laundry_yuk.model.OrderDTO;
import laundryyuk.laundry_yuk.model.OrderStatus;
import laundryyuk.laundry_yuk.model.PaymentStatus;
import laundryyuk.laundry_yuk.repos.CustomerRepository;
import laundryyuk.laundry_yuk.repos.PaymentRepository;
import laundryyuk.laundry_yuk.service.OrderService;
import laundryyuk.laundry_yuk.util.CustomCollectors;
import laundryyuk.laundry_yuk.util.ReferencedWarning;
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
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final CustomerRepository customerRepository;
    private final PaymentRepository paymentRepository;

    public OrderController(final OrderService orderService,
            final CustomerRepository customerRepository,
            final PaymentRepository paymentRepository) {
        this.orderService = orderService;
        this.customerRepository = customerRepository;
        this.paymentRepository = paymentRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("orderStatusValues", OrderStatus.values());
        model.addAttribute("paymentStatusValues", PaymentStatus.values());
        model.addAttribute("customerValues", customerRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Customer::getId, Customer::getNama)));
        model.addAttribute("paymentValues", paymentRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Payment::getId, Payment::getId)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("orders", orderService.findAll());
        return "order/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("order") final OrderDTO orderDTO) {
        orderDTO.setOrderStatus(OrderStatus.MENUNGGU);
        return "order/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("order") @Valid final OrderDTO orderDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        orderDTO.setOrderStatus(OrderStatus.MENUNGGU);

        if (bindingResult.hasErrors()) {
            return "order/add";
        }
        orderService.create(orderDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("order.create.success"));
        return "redirect:/orders";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("order", orderService.get(id));
        return "order/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("order") @Valid final OrderDTO orderDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "order/edit";
        }
        orderService.update(id, orderDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("order.update.success"));
        return "redirect:/orders";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = orderService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            orderService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("order.delete.success"));
        }
        return "redirect:/orders";
    }

}
