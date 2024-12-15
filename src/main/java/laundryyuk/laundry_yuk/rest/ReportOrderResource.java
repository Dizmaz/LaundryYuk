package laundryyuk.laundry_yuk.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import laundryyuk.laundry_yuk.model.ReportOrderDTO;
import laundryyuk.laundry_yuk.service.ReportOrderService;
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
@RequestMapping(value = "/api/reportOrders", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReportOrderResource {

    private final ReportOrderService reportOrderService;

    public ReportOrderResource(final ReportOrderService reportOrderService) {
        this.reportOrderService = reportOrderService;
    }

    @GetMapping
    public ResponseEntity<List<ReportOrderDTO>> getAllReportOrders() {
        return ResponseEntity.ok(reportOrderService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportOrderDTO> getReportOrder(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(reportOrderService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createReportOrder(
            @RequestBody @Valid final ReportOrderDTO reportOrderDTO) {
        final Long createdId = reportOrderService.create(reportOrderDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateReportOrder(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final ReportOrderDTO reportOrderDTO) {
        reportOrderService.update(id, reportOrderDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteReportOrder(@PathVariable(name = "id") final Long id) {
        reportOrderService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
