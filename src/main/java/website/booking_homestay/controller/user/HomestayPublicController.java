package website.booking_homestay.controller.user;

import lombok.RequiredArgsConstructor;
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
                                                @RequestParam("checkIn") Date checkIn,
                                                @RequestParam("checkOut") Date checkOut,
                                                @RequestParam("numPeople") Integer numPeople){
        return homestayService.getHomestaysClient(branchId,checkIn,checkOut,numPeople);
    }

    @GetMapping("home/details/{id}")
    public ResponseEntity<?> getHomestayDetails(@PathVariable("id") Long homestayId){
        return homestayService.getHomestayDetailsClient(homestayId);
    }

}
