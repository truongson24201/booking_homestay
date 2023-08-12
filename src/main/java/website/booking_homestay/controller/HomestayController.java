package website.booking_homestay.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import website.booking_homestay.DTO.create.HomestayCreate;
import website.booking_homestay.DTO.update.HomestayUpdate;
import website.booking_homestay.entity.enumreration.EStatus;
import website.booking_homestay.service.IHomestayService;
import website.booking_homestay.service.Ipml.SchedulingServiceImpl;

import java.io.IOException;
import java.util.Date;

@RestController
@RequestMapping("api/homestays")
@RequiredArgsConstructor
public class HomestayController {

    private final IHomestayService homestayService;
    private final SchedulingServiceImpl schedulingService;

    @GetMapping("")
    public ResponseEntity<?> getHomestays(@RequestParam(name = "id",defaultValue = "") Long branchId,
                                          @RequestParam(name = "date",required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date){
        return homestayService.getHomestays(branchId,date);
    }

    @GetMapping("calendar")
    public ResponseEntity<?> getCalendar(@RequestParam(value = "month",required = false) int month,
                                         @RequestParam(value = "year",required = false) int year){
        return homestayService.getCalendar(year,month);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getHomestayDetails(@PathVariable("id") Long homestayId){
        return homestayService.getHomestayDetails(homestayId);
    }

    @PostMapping("{id}")
    public ResponseEntity<?> createHomestay(@PathVariable("id") Long branchId,@RequestBody HomestayCreate homestayCreate) {
        return homestayService.createHomestay(branchId,homestayCreate);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateHomestay(@PathVariable("id") Long homestayId,@RequestBody HomestayUpdate homestayUpdate){
        return homestayService.updateHomestay(homestayId,homestayUpdate);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> removeHomestay(@PathVariable("id") Long homestayId){
        return homestayService.removeHomestay(homestayId);
    }

    @GetMapping("{id}/images")
    public ResponseEntity<?> getImages(@PathVariable("id") Long homestayId){
        return homestayService.getImages(homestayId);
    }

    @PostMapping(value = "/images",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> setImagesOfHomestay(@RequestParam(name = "id") Long homestayId, @RequestPart(name = "images") MultipartFile[] images) throws IOException {
//        System.out.println(images);
        return homestayService.setImagesOfHomestay(homestayId,images);
    }

    @DeleteMapping("/{id}/images/{imageId}")
    public ResponseEntity<?> removeImageOfHomestay(@PathVariable("id") Long homestayId,@PathVariable("imageId") Long imageId){
        return homestayService.removeImageOfHomestay(homestayId,imageId);
    }

    @GetMapping("{id}/facilities")
    public ResponseEntity<?> getFacilities(@PathVariable("id") Long homestayId){
        return homestayService.getFacilities(homestayId);
    }

    @GetMapping("{id}/combobox")
    public ResponseEntity<?> getFacilitiesNotBelongHome(@PathVariable("id") Long homestayId){
        return homestayService.getFacilitiesNotBelongHome(homestayId);
    }

    @PostMapping("{id}/facilities")
    public ResponseEntity<?> setFacilityOfHomestay(@PathVariable("id") Long homestayId, @RequestBody Long facilityId) {
        return homestayService.setFacilityOfHomestay(homestayId,facilityId);
    }

    @PutMapping("{id}/facilities/{facilityId}")
    public ResponseEntity<?> removeFacilityOfHomestay(@PathVariable("id") Long homestayId,@PathVariable("facilityId") Long facilityId){
        return homestayService.removeFacilityOfHomestay(homestayId,facilityId);
    }

    @GetMapping("/status")
    public ResponseEntity<?> getStatus(){
        EStatus[] statusValues = EStatus.values();
        return ResponseEntity.ok(statusValues);
    }

    @GetMapping("{id}/prices")
    public ResponseEntity<?> getPricesOfHomestayId(@PathVariable("id") Long homestayId){
        return homestayService.getPricesOfHomestayId(homestayId);
    }

    @PutMapping("{id}/prices/refresh")
    public ResponseEntity<?> refreshPricesOfHome(@PathVariable("id") Long homestayId){
        return schedulingService.refreshPricesHomeId(homestayId);
    }

}
