package laundryyuk.laundry_yuk.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import laundryyuk.laundry_yuk.model.ReportIncomeDTO;
import laundryyuk.laundry_yuk.service.ReportIncomeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/reportIncomes", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReportIncomeResource {

    private final ReportIncomeService reportIncomeService;

    public ReportIncomeResource(final ReportIncomeService reportIncomeService) {
        this.reportIncomeService = reportIncomeService;
    }

    @GetMapping
    public ResponseEntity<List<ReportIncomeDTO>> getAllReportIncomes() {
        return ResponseEntity.ok(reportIncomeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportIncomeDTO> getReportIncome(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(reportIncomeService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createReportIncome(
            @RequestBody @Valid final ReportIncomeDTO reportIncomeDTO) {
        final Long createdId = reportIncomeService.create(reportIncomeDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateReportIncome(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final ReportIncomeDTO reportIncomeDTO) {
        reportIncomeService.update(id, reportIncomeDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteReportIncome(@PathVariable(name = "id") final Long id) {
        reportIncomeService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
