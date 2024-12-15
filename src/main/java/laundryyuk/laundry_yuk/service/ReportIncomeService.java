package laundryyuk.laundry_yuk.service;

import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import laundryyuk.laundry_yuk.domain.Admin;
import laundryyuk.laundry_yuk.domain.Payment;
import laundryyuk.laundry_yuk.domain.ReportIncome;
import laundryyuk.laundry_yuk.model.ReportIncomeDTO;
import laundryyuk.laundry_yuk.repos.AdminRepository;
import laundryyuk.laundry_yuk.repos.PaymentRepository;
import laundryyuk.laundry_yuk.repos.ReportIncomeRepository;
import laundryyuk.laundry_yuk.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class ReportIncomeService {

    private final ReportIncomeRepository reportIncomeRepository;
    private final AdminRepository adminRepository;
    private final PaymentRepository paymentRepository;

    public ReportIncomeService(final ReportIncomeRepository reportIncomeRepository,
            final AdminRepository adminRepository, final PaymentRepository paymentRepository) {
        this.reportIncomeRepository = reportIncomeRepository;
        this.adminRepository = adminRepository;
        this.paymentRepository = paymentRepository;
    }

    public List<ReportIncomeDTO> findAll() {
        final List<ReportIncome> reportIncomes = reportIncomeRepository.findAll(Sort.by("id"));
        return reportIncomes.stream()
                .map(reportIncome -> mapToDTO(reportIncome, new ReportIncomeDTO()))
                .toList();
    }

    public ReportIncomeDTO get(final Long id) {
        return reportIncomeRepository.findById(id)
                .map(reportIncome -> mapToDTO(reportIncome, new ReportIncomeDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ReportIncomeDTO reportIncomeDTO) {
        final ReportIncome reportIncome = new ReportIncome();
        mapToEntity(reportIncomeDTO, reportIncome);
        return reportIncomeRepository.save(reportIncome).getId();
    }

    public void update(final Long id, final ReportIncomeDTO reportIncomeDTO) {
        final ReportIncome reportIncome = reportIncomeRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(reportIncomeDTO, reportIncome);
        reportIncomeRepository.save(reportIncome);
    }

    public void delete(final Long id) {
        reportIncomeRepository.deleteById(id);
    }

    private ReportIncomeDTO mapToDTO(final ReportIncome reportIncome,
            final ReportIncomeDTO reportIncomeDTO) {
        reportIncomeDTO.setId(reportIncome.getId());
        reportIncomeDTO.setTotalIncome(reportIncome.getTotalIncome());
        reportIncomeDTO.setAdmin(reportIncome.getAdmin() == null ? null : reportIncome.getAdmin().getId());
        reportIncomeDTO.setPayments(reportIncome.getPayments().stream()
                .map(payment -> payment.getId())
                .toList());
        return reportIncomeDTO;
    }

    private ReportIncome mapToEntity(final ReportIncomeDTO reportIncomeDTO,
            final ReportIncome reportIncome) {
        reportIncome.setTotalIncome(reportIncomeDTO.getTotalIncome());
        final Admin admin = reportIncomeDTO.getAdmin() == null ? null : adminRepository.findById(reportIncomeDTO.getAdmin())
                .orElseThrow(() -> new NotFoundException("admin not found"));
        reportIncome.setAdmin(admin);
        final List<Payment> payments = paymentRepository.findAllById(
                reportIncomeDTO.getPayments() == null ? Collections.emptyList() : reportIncomeDTO.getPayments());
        if (payments.size() != (reportIncomeDTO.getPayments() == null ? 0 : reportIncomeDTO.getPayments().size())) {
            throw new NotFoundException("one of payments not found");
        }
        reportIncome.setPayments(new HashSet<>(payments));
        return reportIncome;
    }

}
