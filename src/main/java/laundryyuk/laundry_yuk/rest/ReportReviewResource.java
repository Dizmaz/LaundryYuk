package laundryyuk.laundry_yuk.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import laundryyuk.laundry_yuk.model.ReportReviewDTO;
import laundryyuk.laundry_yuk.service.ReportReviewService;
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
@RequestMapping(value = "/api/reportReviews", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReportReviewResource {

    private final ReportReviewService reportReviewService;

    public ReportReviewResource(final ReportReviewService reportReviewService) {
        this.reportReviewService = reportReviewService;
    }

    @GetMapping
    public ResponseEntity<List<ReportReviewDTO>> getAllReportReviews() {
        return ResponseEntity.ok(reportReviewService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportReviewDTO> getReportReview(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(reportReviewService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createReportReview(
            @RequestBody @Valid final ReportReviewDTO reportReviewDTO) {
        final Long createdId = reportReviewService.create(reportReviewDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateReportReview(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final ReportReviewDTO reportReviewDTO) {
        reportReviewService.update(id, reportReviewDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteReportReview(@PathVariable(name = "id") final Long id) {
        reportReviewService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
