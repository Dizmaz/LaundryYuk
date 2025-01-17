package laundryyuk.laundry_yuk.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OrderDTO {

    private Long id;

    private String customerName;

    private OrderStatus orderStatus;

    private PaymentStatus paymentStatus;

    private Double weight;

    private Double price;

    @NotNull
    private Long customer;

    @OrderPaymentUnique
    private Long payment;

}
