package laundryyuk.laundry_yuk.repos;

import laundryyuk.laundry_yuk.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
