package laundryyuk.laundry_yuk.repos;

import java.util.List;
import laundryyuk.laundry_yuk.domain.Admin;
import laundryyuk.laundry_yuk.domain.ReportReview;
import laundryyuk.laundry_yuk.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReportReviewRepository extends JpaRepository<ReportReview, Long> {

    ReportReview findFirstByAdmin(Admin admin);

    ReportReview findFirstByReviews(Review review);

    List<ReportReview> findAllByReviews(Review review);

}
