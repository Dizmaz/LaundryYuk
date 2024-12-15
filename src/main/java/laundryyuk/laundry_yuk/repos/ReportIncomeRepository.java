package laundryyuk.laundry_yuk.repos;

import java.util.List;
import laundryyuk.laundry_yuk.domain.Admin;
import laundryyuk.laundry_yuk.domain.Payment;
import laundryyuk.laundry_yuk.domain.ReportIncome;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReportIncomeRepository extends JpaRepository<ReportIncome, Long> {

    ReportIncome findFirstByAdmin(Admin admin);

    ReportIncome findFirstByPayments(Payment payment);

    List<ReportIncome> findAllByPayments(Payment payment);

}
