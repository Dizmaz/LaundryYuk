package laundryyuk.laundry_yuk.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ReportOrderDTO {

    private Long id;
    private Long admin;
    private List<Long> orders;

}
