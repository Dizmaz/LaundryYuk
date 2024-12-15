package laundryyuk.laundry_yuk.service;

import jakarta.transaction.Transactional;
import java.util.List;
import laundryyuk.laundry_yuk.domain.Order;
import laundryyuk.laundry_yuk.domain.Payment;
import laundryyuk.laundry_yuk.model.PaymentDTO;
import laundryyuk.laundry_yuk.repos.OrderRepository;
import laundryyuk.laundry_yuk.repos.PaymentRepository;
import laundryyuk.laundry_yuk.repos.ReportIncomeRepository;
import laundryyuk.laundry_yuk.util.NotFoundException;
import laundryyuk.laundry_yuk.util.ReferencedWarning;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReportIncomeRepository reportIncomeRepository;
    private final OrderRepository orderRepository;

    public PaymentService(final PaymentRepository paymentRepository,
            final ReportIncomeRepository reportIncomeRepository,
            final OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.reportIncomeRepository = reportIncomeRepository;
        this.orderRepository = orderRepository;
    }

    public List<PaymentDTO> findAll() {
        final List<Payment> payments = paymentRepository.findAll(Sort.by("id"));
        return payments.stream()
                .map(payment -> mapToDTO(payment, new PaymentDTO()))
                .toList();
    }

    public PaymentDTO get(final Long id) {
        return paymentRepository.findById(id)
                .map(payment -> mapToDTO(payment, new PaymentDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final PaymentDTO paymentDTO) {
        final Payment payment = new Payment();
        mapToEntity(paymentDTO, payment);
        return paymentRepository.save(payment).getId();
    }

    public void update(final Long id, final PaymentDTO paymentDTO) {
        final Payment payment = paymentRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(paymentDTO, payment);
        paymentRepository.save(payment);
    }

    public void delete(final Long id) {
        final Payment payment = paymentRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        // remove many-to-many relations at owning side
        reportIncomeRepository.findAllByPayments(payment)
                .forEach(reportIncome -> reportIncome.getPayments().remove(payment));
        paymentRepository.delete(payment);
    }

    private PaymentDTO mapToDTO(final Payment payment, final PaymentDTO paymentDTO) {
        paymentDTO.setId(payment.getId());
        paymentDTO.setNomimal(payment.getNomimal());
        paymentDTO.setStatusPayment(payment.getStatusPayment());
        return paymentDTO;
    }

    private Payment mapToEntity(final PaymentDTO paymentDTO, final Payment payment) {
        payment.setNomimal(paymentDTO.getNomimal());
        payment.setStatusPayment(paymentDTO.getStatusPayment());
        return payment;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Payment payment = paymentRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Order paymentOrder = orderRepository.findFirstByPayment(payment);
        if (paymentOrder != null) {
            referencedWarning.setKey("payment.order.payment.referenced");
            referencedWarning.addParam(paymentOrder.getId());
            return referencedWarning;
        }
        return null;
    }

}
