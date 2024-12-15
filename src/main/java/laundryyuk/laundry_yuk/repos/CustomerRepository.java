package laundryyuk.laundry_yuk.repos;

import laundryyuk.laundry_yuk.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
