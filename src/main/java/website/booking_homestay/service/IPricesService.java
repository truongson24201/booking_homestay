package website.booking_homestay.service;

import org.springframework.http.ResponseEntity;
import website.booking_homestay.DTO.create.HomePrice;

import java.util.Date;

public interface IPricesService {
    ResponseEntity<?> createPrice(Long branchId, Date priceListCreate);

    ResponseEntity<?> removePrice(Long pricelistId);

    ResponseEntity<?> updatePrice(Long pricelistId,Date priceListCreate);

//    ResponseEntity<?> activityPrice(Long priceId);

    ResponseEntity<?> getPriceList(Long branchId);

    ResponseEntity<?> getPriceDetails(Long priceId);

    ResponseEntity<?> setHomestayInOf(Long priceId, HomePrice homePrice);

    ResponseEntity<?> removeHomestayOutOf(Long priceId, Long homestayId);

    ResponseEntity<?> getHomestaysOfPrice(Long priceId);

    ResponseEntity<?> getHomesNotBelongToPrice(Long priceId);

}
