package laundryyuk.laundry_yuk.repos;

import java.util.List;
import laundryyuk.laundry_yuk.domain.Admin;
import laundryyuk.laundry_yuk.domain.Order;
import laundryyuk.laundry_yuk.domain.ReportOrder;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReportOrderRepository extends JpaRepository<ReportOrder, Long> {

    ReportOrder findFirstByAdmin(Admin admin);

    ReportOrder findFirstByOrders(Order order);

    List<ReportOrder> findAllByOrders(Order order);

}
