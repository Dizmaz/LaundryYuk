package laundryyuk.laundry_yuk.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import laundryyuk.laundry_yuk.model.ReviewDTO;
import laundryyuk.laundry_yuk.service.ReviewService;
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
@RequestMapping(value = "/api/reviews", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReviewResource {

    private final ReviewService reviewService;

    public ReviewResource(final ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getAllReviews() {
        return ResponseEntity.ok(reviewService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getReview(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(reviewService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createReview(@RequestBody @Valid final ReviewDTO reviewDTO) {
        final Long createdId = reviewService.create(reviewDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateReview(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final ReviewDTO reviewDTO) {
        reviewService.update(id, reviewDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteReview(@PathVariable(name = "id") final Long id) {
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
