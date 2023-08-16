package website.booking_homestay.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import website.booking_homestay.DTO.create.EvaluationCreate;
import website.booking_homestay.service.IBranchService;
import website.booking_homestay.service.IEvaluationService;
import website.booking_homestay.service.IHomestayService;
import website.booking_homestay.service.IInvoiceService;

import java.util.Date;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class HomestayPublicController {
    private final IHomestayService homestayService;
    private final IBranchService branchService;
    private final IEvaluationService evaluationService;
    private final IInvoiceService invoiceService;

    @PostMapping("/search")
    public ResponseEntity<?> searchHomestay(@RequestBody Object address){
        return branchService.getBranchesAddress(address);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getHomestaysClient(@PathVariable("id") Long branchId,
                                                @RequestParam(value = "checkIn",required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date checkIn,
                                                @RequestParam(value = "checkOut",required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date checkOut,
                                                @RequestParam("numPeople") Integer numPeople){
        return homestayService.getHomestaysClient(branchId,checkIn,checkOut,numPeople);
    }

    @GetMapping("{id}/details")
    public ResponseEntity<?> getHomestayDetails(@PathVariable("id") Long homestayId){
        return homestayService.getHomestayDetailsClient(homestayId);
    }

    @GetMapping("homestays")
    public ResponseEntity<?> getHomestaysPublic() {
        return homestayService.getHomestaysPublic();
    }

    @GetMapping("homestay/{id}/evaluation")
    public ResponseEntity<?> getEvaluation(@PathVariable("id") Long homestayId) {
        return evaluationService.getEvaluation(homestayId);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("homestay/{id}")
    public ResponseEntity<?> sendEvaluation(@PathVariable("id") Long homestayId,@RequestBody EvaluationCreate evaluationCreate){
        return evaluationService.createEvaluation(homestayId,evaluationCreate);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("calendar")
    public ResponseEntity<?> getCalendar(@RequestParam("year") int year,@RequestParam("month") int month){
        return invoiceService.getCalendar(year,month);
    }

}
