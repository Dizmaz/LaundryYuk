package laundryyuk.laundry_yuk.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ReportIncomeDTO {

    private Long id;
    private Double totalIncome;
    private Long admin;
    private List<Long> payments;

}
