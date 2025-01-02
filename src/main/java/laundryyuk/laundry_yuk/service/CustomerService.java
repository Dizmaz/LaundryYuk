package laundryyuk.laundry_yuk.service;

import java.util.List;
import laundryyuk.laundry_yuk.domain.Customer;
import laundryyuk.laundry_yuk.domain.Order;
import laundryyuk.laundry_yuk.domain.Review;
import laundryyuk.laundry_yuk.model.CustomerDTO;
import laundryyuk.laundry_yuk.repos.CustomerRepository;
import laundryyuk.laundry_yuk.repos.OrderRepository;
import laundryyuk.laundry_yuk.repos.ReviewRepository;
import laundryyuk.laundry_yuk.util.NotFoundException;
import laundryyuk.laundry_yuk.util.PasswordUtil;
import laundryyuk.laundry_yuk.util.ReferencedWarning;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;

    public CustomerService(final CustomerRepository customerRepository,
            final OrderRepository orderRepository, final ReviewRepository reviewRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.reviewRepository = reviewRepository;
    }

    public List<CustomerDTO> findAll() {
        final List<Customer> customers = customerRepository.findAll(Sort.by("nama"));
        return customers.stream()
                .map(customer -> mapToDTO(customer, new CustomerDTO()))
                .toList();
    }

    public CustomerDTO get(final Long id) {
        return customerRepository.findById(id)
                .map(customer -> mapToDTO(customer, new CustomerDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final CustomerDTO customerDTO) {
        final Customer customer = new Customer();
        mapToEntity(customerDTO, customer);
        return customerRepository.save(customer).getId();
    }

    public void update(final Long id, final CustomerDTO customerDTO) {
        final Customer customer = customerRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(customerDTO, customer);
        customerRepository.save(customer);
    }

    public void delete(final Long id) {
        customerRepository.deleteById(id);
    }

    private CustomerDTO mapToDTO(final Customer customer, final CustomerDTO customerDTO) {
        customerDTO.setId(customer.getId());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setNama(customer.getNama());
        customerDTO.setRole(customer.getRole());
        return customerDTO;
    }

    private Customer mapToEntity(final CustomerDTO customerDTO, final Customer customer) {
        customer.setEmail(customerDTO.getEmail());
        customer.setNama(customerDTO.getNama());
        customer.setRole(customerDTO.getRole());
        return customer;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Customer customer = customerRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Order customerOrder = orderRepository.findFirstByCustomer(customer);
        if (customerOrder != null) {
            referencedWarning.setKey("customer.order.customer.referenced");
            referencedWarning.addParam(customerOrder.getId());
            return referencedWarning;
        }
        final Review customerReview = reviewRepository.findFirstByCustomer(customer);
        if (customerReview != null) {
            referencedWarning.setKey("customer.review.customer.referenced");
            referencedWarning.addParam(customerReview.getId());
            return referencedWarning;
        }
        return null;
    }

    //========================================================================================
    public boolean emailExists(String email) {
        return customerRepository.findByEmail(email).isPresent();
    }

    public void register(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setEmail(customerDTO.getEmail());
        customer.setNama(customerDTO.getNama());
        customer.setRole(customerDTO.getRole());
        customer.setPassword(PasswordUtil.hashPassword(customerDTO.getPassword())); // Panggil PasswordUtils
        customerRepository.save(customer);
    }

    public boolean authenticate(String email, String rawPassword) {
        String hashedPassword = PasswordUtil.hashPassword(rawPassword);
        return customerRepository.findByEmail(email)
                .map(customer -> customer.getPassword().equals(hashedPassword))
                .orElse(false);
    }

}
