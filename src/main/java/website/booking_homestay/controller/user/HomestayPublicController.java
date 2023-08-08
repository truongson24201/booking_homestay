package website.booking_homestay.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import website.booking_homestay.service.IBranchService;
import website.booking_homestay.service.IHomestayService;

import java.util.Date;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class HomestayPublicController {
    private final IHomestayService homestayService;
    private final IBranchService branchService;

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

}
