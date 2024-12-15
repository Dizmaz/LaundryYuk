package laundryyuk.laundry_yuk.service;

import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import laundryyuk.laundry_yuk.domain.Admin;
import laundryyuk.laundry_yuk.domain.ReportReview;
import laundryyuk.laundry_yuk.domain.Review;
import laundryyuk.laundry_yuk.model.ReportReviewDTO;
import laundryyuk.laundry_yuk.repos.AdminRepository;
import laundryyuk.laundry_yuk.repos.ReportReviewRepository;
import laundryyuk.laundry_yuk.repos.ReviewRepository;
import laundryyuk.laundry_yuk.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class ReportReviewService {

    private final ReportReviewRepository reportReviewRepository;
    private final AdminRepository adminRepository;
    private final ReviewRepository reviewRepository;

    public ReportReviewService(final ReportReviewRepository reportReviewRepository,
            final AdminRepository adminRepository, final ReviewRepository reviewRepository) {
        this.reportReviewRepository = reportReviewRepository;
        this.adminRepository = adminRepository;
        this.reviewRepository = reviewRepository;
    }

    public List<ReportReviewDTO> findAll() {
        final List<ReportReview> reportReviews = reportReviewRepository.findAll(Sort.by("id"));
        return reportReviews.stream()
                .map(reportReview -> mapToDTO(reportReview, new ReportReviewDTO()))
                .toList();
    }

    public ReportReviewDTO get(final Long id) {
        return reportReviewRepository.findById(id)
                .map(reportReview -> mapToDTO(reportReview, new ReportReviewDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ReportReviewDTO reportReviewDTO) {
        final ReportReview reportReview = new ReportReview();
        mapToEntity(reportReviewDTO, reportReview);
        return reportReviewRepository.save(reportReview).getId();
    }

    public void update(final Long id, final ReportReviewDTO reportReviewDTO) {
        final ReportReview reportReview = reportReviewRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(reportReviewDTO, reportReview);
        reportReviewRepository.save(reportReview);
    }

    public void delete(final Long id) {
        reportReviewRepository.deleteById(id);
    }

    private ReportReviewDTO mapToDTO(final ReportReview reportReview,
            final ReportReviewDTO reportReviewDTO) {
        reportReviewDTO.setId(reportReview.getId());
        reportReviewDTO.setAdmin(reportReview.getAdmin() == null ? null : reportReview.getAdmin().getId());
        reportReviewDTO.setReviews(reportReview.getReviews().stream()
                .map(review -> review.getId())
                .toList());
        return reportReviewDTO;
    }

    private ReportReview mapToEntity(final ReportReviewDTO reportReviewDTO,
            final ReportReview reportReview) {
        final Admin admin = reportReviewDTO.getAdmin() == null ? null : adminRepository.findById(reportReviewDTO.getAdmin())
                .orElseThrow(() -> new NotFoundException("admin not found"));
        reportReview.setAdmin(admin);
        final List<Review> reviews = reviewRepository.findAllById(
                reportReviewDTO.getReviews() == null ? Collections.emptyList() : reportReviewDTO.getReviews());
        if (reviews.size() != (reportReviewDTO.getReviews() == null ? 0 : reportReviewDTO.getReviews().size())) {
            throw new NotFoundException("one of reviews not found");
        }
        reportReview.setReviews(new HashSet<>(reviews));
        return reportReview;
    }

}
