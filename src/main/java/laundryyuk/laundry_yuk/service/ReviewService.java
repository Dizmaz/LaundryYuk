package laundryyuk.laundry_yuk.service;

import jakarta.transaction.Transactional;
import java.util.List;
import laundryyuk.laundry_yuk.domain.Customer;
import laundryyuk.laundry_yuk.domain.Order;
import laundryyuk.laundry_yuk.domain.Review;
import laundryyuk.laundry_yuk.model.ReviewDTO;
import laundryyuk.laundry_yuk.repos.CustomerRepository;
import laundryyuk.laundry_yuk.repos.OrderRepository;
import laundryyuk.laundry_yuk.repos.ReportReviewRepository;
import laundryyuk.laundry_yuk.repos.ReviewRepository;
import laundryyuk.laundry_yuk.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final ReportReviewRepository reportReviewRepository;

    public ReviewService(final ReviewRepository reviewRepository,
            final CustomerRepository customerRepository, final OrderRepository orderRepository,
            final ReportReviewRepository reportReviewRepository) {
        this.reviewRepository = reviewRepository;
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.reportReviewRepository = reportReviewRepository;
    }

    public List<ReviewDTO> findAll() {
        final List<Review> reviews = reviewRepository.findAll(Sort.by("id"));
        return reviews.stream()
                .map(review -> mapToDTO(review, new ReviewDTO()))
                .toList();
    }

    public ReviewDTO get(final Long id) {
        return reviewRepository.findById(id)
                .map(review -> mapToDTO(review, new ReviewDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ReviewDTO reviewDTO) {
        final Review review = new Review();
        mapToEntity(reviewDTO, review);
        return reviewRepository.save(review).getId();
    }

    public void update(final Long id, final ReviewDTO reviewDTO) {
        final Review review = reviewRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(reviewDTO, review);
        reviewRepository.save(review);
    }

    public void delete(final Long id) {
        final Review review = reviewRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        // remove many-to-many relations at owning side
        reportReviewRepository.findAllByReviews(review)
                .forEach(reportReview -> reportReview.getReviews().remove(review));
        reviewRepository.delete(review);
    }

    private ReviewDTO mapToDTO(final Review review, final ReviewDTO reviewDTO) {
        reviewDTO.setId(review.getId());
        reviewDTO.setKonten(review.getKonten());
        reviewDTO.setCustomer(review.getCustomer() == null ? null : review.getCustomer().getId());
        reviewDTO.setOrder(review.getOrder() == null ? null : review.getOrder().getId());
        return reviewDTO;
    }

    private Review mapToEntity(final ReviewDTO reviewDTO, final Review review) {
        review.setKonten(reviewDTO.getKonten());
        final Customer customer = reviewDTO.getCustomer() == null ? null : customerRepository.findById(reviewDTO.getCustomer())
                .orElseThrow(() -> new NotFoundException("customer not found"));
        review.setCustomer(customer);
        final Order order = reviewDTO.getOrder() == null ? null : orderRepository.findById(reviewDTO.getOrder())
                .orElseThrow(() -> new NotFoundException("order not found"));
        review.setOrder(order);
        return review;
    }

    public boolean orderExists(final Long id) {
        return reviewRepository.existsByOrderId(id);
    }

}
