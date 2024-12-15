package laundryyuk.laundry_yuk.model;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PaymentDTO {

    private Long id;
    private Double nomimal;
    private PaymentStatus statusPayment;

}
