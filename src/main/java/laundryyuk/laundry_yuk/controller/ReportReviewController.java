package laundryyuk.laundry_yuk.controller;

import jakarta.validation.Valid;
import laundryyuk.laundry_yuk.domain.Admin;
import laundryyuk.laundry_yuk.domain.Review;
import laundryyuk.laundry_yuk.model.ReportReviewDTO;
import laundryyuk.laundry_yuk.repos.AdminRepository;
import laundryyuk.laundry_yuk.repos.ReviewRepository;
import laundryyuk.laundry_yuk.service.ReportReviewService;
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
@RequestMapping("/reportReviews")
public class ReportReviewController {

    private final ReportReviewService reportReviewService;
    private final AdminRepository adminRepository;
    private final ReviewRepository reviewRepository;

    public ReportReviewController(final ReportReviewService reportReviewService,
            final AdminRepository adminRepository, final ReviewRepository reviewRepository) {
        this.reportReviewService = reportReviewService;
        this.adminRepository = adminRepository;
        this.reviewRepository = reviewRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("adminValues", adminRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Admin::getId, Admin::getId)));
        model.addAttribute("reviewsValues", reviewRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Review::getId, Review::getId)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("reportReviews", reportReviewService.findAll());
        return "reportReview/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("reportReview") final ReportReviewDTO reportReviewDTO) {
        return "reportReview/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("reportReview") @Valid final ReportReviewDTO reportReviewDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "reportReview/add";
        }
        reportReviewService.create(reportReviewDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("reportReview.create.success"));
        return "redirect:/reportReviews";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("reportReview", reportReviewService.get(id));
        return "reportReview/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("reportReview") @Valid final ReportReviewDTO reportReviewDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "reportReview/edit";
        }
        reportReviewService.update(id, reportReviewDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("reportReview.update.success"));
        return "redirect:/reportReviews";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        reportReviewService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("reportReview.delete.success"));
        return "redirect:/reportReviews";
    }

}
