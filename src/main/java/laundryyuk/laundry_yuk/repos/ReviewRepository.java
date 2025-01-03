package laundryyuk.laundry_yuk.repos;

import laundryyuk.laundry_yuk.domain.Customer;
import laundryyuk.laundry_yuk.domain.Order;
import laundryyuk.laundry_yuk.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReviewRepository extends JpaRepository<Review, Long> {

}
