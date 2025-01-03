package laundryyuk.laundry_yuk.controller;

import jakarta.validation.Valid;
import laundryyuk.laundry_yuk.domain.Customer;
import laundryyuk.laundry_yuk.domain.Order;
import laundryyuk.laundry_yuk.model.ReviewDTO;
import laundryyuk.laundry_yuk.repos.CustomerRepository;
import laundryyuk.laundry_yuk.repos.OrderRepository;
import laundryyuk.laundry_yuk.service.ReviewService;
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
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    public ReviewController(final ReviewService reviewService,
                            final CustomerRepository customerRepository, final OrderRepository orderRepository) {
        this.reviewService = reviewService;
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("customerValues", customerRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Customer::getId, Customer::getId)));
        model.addAttribute("orderValues", orderRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Order::getId, Order::getId)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("reviews", reviewService.findAll());
        return "review/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("review") final ReviewDTO reviewDTO) {
        return "review/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("review") @Valid final ReviewDTO reviewDTO,
                      final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "review/add";
        }
        reviewService.create(reviewDTO); // Tidak perlu mengatur customer dan order
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("review.create.success"));
        return "redirect:/reviews";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("review", reviewService.get(id));
        return "review/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
                       @ModelAttribute("review") @Valid final ReviewDTO reviewDTO,
                       final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "review/edit";
        }
        reviewService.update(id, reviewDTO); // Tidak perlu mengatur customer dan order
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("review.update.success"));
        return "redirect:/reviews";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
                         final RedirectAttributes redirectAttributes) {
        reviewService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("review.delete.success"));
        return "redirect:/reviews";
    }

}
