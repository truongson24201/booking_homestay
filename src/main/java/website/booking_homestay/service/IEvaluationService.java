package website.booking_homestay.service;

import org.springframework.http.ResponseEntity;
import website.booking_homestay.DTO.create.EvaluationCreate;

public interface IEvaluationService {
    ResponseEntity<?> getEvaluation(Long homestayId);

    ResponseEntity<?> createEvaluation(Long homestayId, EvaluationCreate evaluationCreate);
}
