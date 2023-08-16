package website.booking_homestay.service.Ipml;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import website.booking_homestay.DTO.create.EvaluationCreate;
import website.booking_homestay.entity.Evaluation;
import website.booking_homestay.entity.Homestay;
import website.booking_homestay.entity.User;
import website.booking_homestay.repository.EvaluationRepository;
import website.booking_homestay.repository.HomestayRepository;
import website.booking_homestay.repository.InvoiceRepository;
import website.booking_homestay.service.IContextHolder;
import website.booking_homestay.service.IEvaluationService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EvaluationServiceImpl implements IEvaluationService {
    private final EvaluationRepository evaluationRepository;
    private final IContextHolder contextHolder;
    private final InvoiceRepository invoiceRepository;
    private final HomestayRepository homestayRepository;

    public record EvaluationDTO(String username,@JsonFormat(pattern = "dd/MM/yyyy'-'HH:mm", timezone = "Asia/Ho_Chi_Minh") Date create,
                                Integer point, String content){}
    @Override
    public ResponseEntity<?> getEvaluation(Long homestayId) {
        List<Evaluation> evaluations = evaluationRepository.findAllByHomestay_HomestayId(homestayId);
        List<EvaluationDTO> evaluationDTOS = new ArrayList<>();
        if (!evaluations.isEmpty()) {
            evaluations.forEach(evaluation -> {
                EvaluationDTO evaluationDTO = new EvaluationDTO(evaluation.getUser().getUsername(),
                        evaluation.getCreate(),evaluation.getPoint(),evaluation.getContent());
                evaluationDTOS.add(evaluationDTO);
            });
        }
        return ResponseEntity.ok(evaluationDTOS);
    }

    @Override
    public ResponseEntity<?> createEvaluation(Long homestayId, EvaluationCreate evaluationCreate) {
        User user = contextHolder.getUser();
        Homestay homestay = homestayRepository.findById(homestayId).get();
        if (!invoiceRepository.checkEvaluation(user.getAccountId(),homestayId)){
            return ResponseEntity.badRequest().body("You have never been to this homestay, can't evaluation");
        }
        Evaluation evaluation = new Evaluation(user,homestay,new Date(),evaluationCreate.getPoint(),evaluationCreate.getContent());
        try {
            evaluationRepository.save(evaluation);
            EvaluationDTO evaluationDTO = new EvaluationDTO(evaluation.getUser().getUsername(),evaluation.getCreate(),evaluation.getPoint(),evaluation.getContent());
            return ResponseEntity.ok(evaluationDTO);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Create Comment failed!");
        }
    }
}
