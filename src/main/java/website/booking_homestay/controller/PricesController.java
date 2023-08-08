package website.booking_homestay.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import website.booking_homestay.DTO.create.HomePrice;
import website.booking_homestay.entity.enumreration.EPrice;
import website.booking_homestay.service.IPricesService;
import website.booking_homestay.service.Ipml.SchedulingServiceImpl;

import java.util.Date;

@RestController
@RequestMapping("api/prices")
@RequiredArgsConstructor
public class PricesController {

    private final IPricesService pricesService;
    private final SchedulingServiceImpl schedulingService;

    @GetMapping("")
    public ResponseEntity<?> getPriceList(@RequestParam(name = "id", defaultValue = "0") Long branchId){
        return pricesService.getPriceList(branchId);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getPriceDetails(@PathVariable("id") Long priceId){
        return pricesService.getPriceDetails(priceId);
    }

    @PostMapping("{id}")
    public ResponseEntity<?> createPrice(@PathVariable("id") Long branchId,@RequestBody Date priceListCreate){
        return pricesService.createPrice(branchId,priceListCreate);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updatePrice(@PathVariable("id") Long pricelistId,@RequestBody Date priceListCreate){
        return pricesService.updatePrice(pricelistId,priceListCreate);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> removePrice(@PathVariable("id") Long pricelistId){
        return pricesService.removePrice(pricelistId);
    }
//    @PostMapping("activity/{id}")
//    public ResponseEntity<?> activityPrice(@PathVariable("id") Long priceId){
//        return pricesService.activityPrice(priceId);

//    }

    @GetMapping("{id}/homestays")
    public ResponseEntity<?> getHomestaysOfPrice(@PathVariable("id") Long priceId){
        return pricesService.getHomestaysOfPrice(priceId);
    }

    @PostMapping("{id}/homestays")
    public ResponseEntity<?> setHomestayInOf(@PathVariable("id") Long priceId,@RequestBody HomePrice homePrice){
        return pricesService.setHomestayInOf(priceId,homePrice);
    }


    @DeleteMapping("{id}/homestays/{homestayId}")
    public ResponseEntity<?> removeHomestayOutOf(@PathVariable("id") Long priceId,@PathVariable("homestayId") Long homestayId){
        return pricesService.removeHomestayOutOf(priceId,homestayId);
    }

    @GetMapping("{id}/combobox")
    public ResponseEntity<?> getHomesNotBelongToPrice(@PathVariable("id") Long priceId){
        return pricesService.getHomesNotBelongToPrice(priceId);
    }

    @GetMapping("/status")
    public ResponseEntity<?> getStatus(){
        EPrice[] statusValues = EPrice.values();
        return ResponseEntity.ok(statusValues);
    }

    @PutMapping("refresh")
    public ResponseEntity<?> refreshAllPrices() {
        return schedulingService.refreshAllPrices();
    }
}
