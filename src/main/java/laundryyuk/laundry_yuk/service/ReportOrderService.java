package laundryyuk.laundry_yuk.service;

import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import laundryyuk.laundry_yuk.domain.Admin;
import laundryyuk.laundry_yuk.domain.Order;
import laundryyuk.laundry_yuk.domain.ReportOrder;
import laundryyuk.laundry_yuk.model.ReportOrderDTO;
import laundryyuk.laundry_yuk.repos.AdminRepository;
import laundryyuk.laundry_yuk.repos.OrderRepository;
import laundryyuk.laundry_yuk.repos.ReportOrderRepository;
import laundryyuk.laundry_yuk.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class ReportOrderService {

    private final ReportOrderRepository reportOrderRepository;
    private final AdminRepository adminRepository;
    private final OrderRepository orderRepository;

    public ReportOrderService(final ReportOrderRepository reportOrderRepository,
            final AdminRepository adminRepository, final OrderRepository orderRepository) {
        this.reportOrderRepository = reportOrderRepository;
        this.adminRepository = adminRepository;
        this.orderRepository = orderRepository;
    }

    public List<ReportOrderDTO> findAll() {
        final List<ReportOrder> reportOrders = reportOrderRepository.findAll(Sort.by("id"));
        return reportOrders.stream()
                .map(reportOrder -> mapToDTO(reportOrder, new ReportOrderDTO()))
                .toList();
    }

    public ReportOrderDTO get(final Long id) {
        return reportOrderRepository.findById(id)
                .map(reportOrder -> mapToDTO(reportOrder, new ReportOrderDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ReportOrderDTO reportOrderDTO) {
        final ReportOrder reportOrder = new ReportOrder();
        mapToEntity(reportOrderDTO, reportOrder);
        return reportOrderRepository.save(reportOrder).getId();
    }

    public void update(final Long id, final ReportOrderDTO reportOrderDTO) {
        final ReportOrder reportOrder = reportOrderRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(reportOrderDTO, reportOrder);
        reportOrderRepository.save(reportOrder);
    }

    public void delete(final Long id) {
        reportOrderRepository.deleteById(id);
    }

    private ReportOrderDTO mapToDTO(final ReportOrder reportOrder,
            final ReportOrderDTO reportOrderDTO) {
        reportOrderDTO.setId(reportOrder.getId());
        reportOrderDTO.setAdmin(reportOrder.getAdmin() == null ? null : reportOrder.getAdmin().getId());
        reportOrderDTO.setOrders(reportOrder.getOrders().stream()
                .map(order -> order.getId())
                .toList());
        return reportOrderDTO;
    }

    private ReportOrder mapToEntity(final ReportOrderDTO reportOrderDTO,
            final ReportOrder reportOrder) {
        final Admin admin = reportOrderDTO.getAdmin() == null ? null : adminRepository.findById(reportOrderDTO.getAdmin())
                .orElseThrow(() -> new NotFoundException("admin not found"));
        reportOrder.setAdmin(admin);
        final List<Order> orders = orderRepository.findAllById(
                reportOrderDTO.getOrders() == null ? Collections.emptyList() : reportOrderDTO.getOrders());
        if (orders.size() != (reportOrderDTO.getOrders() == null ? 0 : reportOrderDTO.getOrders().size())) {
            throw new NotFoundException("one of orders not found");
        }
        reportOrder.setOrders(new HashSet<>(orders));
        return reportOrder;
    }

}
