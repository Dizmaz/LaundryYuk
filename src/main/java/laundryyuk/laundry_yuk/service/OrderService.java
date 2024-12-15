package laundryyuk.laundry_yuk.service;

import jakarta.transaction.Transactional;
import java.util.List;
import laundryyuk.laundry_yuk.domain.Customer;
import laundryyuk.laundry_yuk.domain.Order;
import laundryyuk.laundry_yuk.domain.Payment;
import laundryyuk.laundry_yuk.domain.Review;
import laundryyuk.laundry_yuk.model.OrderDTO;
import laundryyuk.laundry_yuk.repos.CustomerRepository;
import laundryyuk.laundry_yuk.repos.OrderRepository;
import laundryyuk.laundry_yuk.repos.PaymentRepository;
import laundryyuk.laundry_yuk.repos.ReportOrderRepository;
import laundryyuk.laundry_yuk.repos.ReviewRepository;
import laundryyuk.laundry_yuk.util.NotFoundException;
import laundryyuk.laundry_yuk.util.ReferencedWarning;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final PaymentRepository paymentRepository;
    private final ReportOrderRepository reportOrderRepository;
    private final ReviewRepository reviewRepository;

    public OrderService(final OrderRepository orderRepository,
            final CustomerRepository customerRepository, final PaymentRepository paymentRepository,
            final ReportOrderRepository reportOrderRepository,
            final ReviewRepository reviewRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.paymentRepository = paymentRepository;
        this.reportOrderRepository = reportOrderRepository;
        this.reviewRepository = reviewRepository;
    }

    public List<OrderDTO> findAll() {
        final List<Order> orders = orderRepository.findAll(Sort.by("id"));
        return orders.stream()
                .map(order -> mapToDTO(order, new OrderDTO()))
                .toList();
    }

    public OrderDTO get(final Long id) {
        return orderRepository.findById(id)
                .map(order -> mapToDTO(order, new OrderDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final OrderDTO orderDTO) {
        final Order order = new Order();
        mapToEntity(orderDTO, order);
        return orderRepository.save(order).getId();
    }

    public void update(final Long id, final OrderDTO orderDTO) {
        final Order order = orderRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(orderDTO, order);
        orderRepository.save(order);
    }

    public void delete(final Long id) {
        final Order order = orderRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        // remove many-to-many relations at owning side
        reportOrderRepository.findAllByOrders(order)
                .forEach(reportOrder -> reportOrder.getOrders().remove(order));
        orderRepository.delete(order);
    }

    private OrderDTO mapToDTO(final Order order, final OrderDTO orderDTO) {
        orderDTO.setId(order.getId());
        orderDTO.setOrderStatus(order.getOrderStatus());
        orderDTO.setWeight(order.getWeight());
        orderDTO.setPrice(order.getPrice());
        orderDTO.setCustomer(order.getCustomer() == null ? null : order.getCustomer().getId());
        orderDTO.setPayment(order.getPayment() == null ? null : order.getPayment().getId());
        return orderDTO;
    }

    private Order mapToEntity(final OrderDTO orderDTO, final Order order) {
        order.setOrderStatus(orderDTO.getOrderStatus());
        order.setWeight(orderDTO.getWeight());
        order.setPrice(orderDTO.getPrice());
        final Customer customer = orderDTO.getCustomer() == null ? null : customerRepository.findById(orderDTO.getCustomer())
                .orElseThrow(() -> new NotFoundException("customer not found"));
        order.setCustomer(customer);
        final Payment payment = orderDTO.getPayment() == null ? null : paymentRepository.findById(orderDTO.getPayment())
                .orElseThrow(() -> new NotFoundException("payment not found"));
        order.setPayment(payment);
        return order;
    }

    public boolean paymentExists(final Long id) {
        return orderRepository.existsByPaymentId(id);
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Order order = orderRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Review orderReview = reviewRepository.findFirstByOrder(order);
        if (orderReview != null) {
            referencedWarning.setKey("order.review.order.referenced");
            referencedWarning.addParam(orderReview.getId());
            return referencedWarning;
        }
        return null;
    }

}
