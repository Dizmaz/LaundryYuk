package laundryyuk.laundry_yuk.repos;

import laundryyuk.laundry_yuk.domain.Customer;
import laundryyuk.laundry_yuk.domain.Order;
import laundryyuk.laundry_yuk.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findFirstByCustomer(Customer customer);

    Order findFirstByPayment(Payment payment);

    boolean existsByPaymentId(Long id);

}
