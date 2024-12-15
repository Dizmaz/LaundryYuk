package laundryyuk.laundry_yuk.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ReportReviewDTO {

    private Long id;
    private Long admin;
    private List<Long> reviews;

}
