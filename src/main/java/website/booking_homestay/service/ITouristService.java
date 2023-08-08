package website.booking_homestay.service;

import org.springframework.http.ResponseEntity;

public interface ITouristService {

    ResponseEntity<?> getBranchesOfTourist(Long touristId);

    ResponseEntity<?> getTouristDetails(Long touristId);

    ResponseEntity<?> getTourists();

    ResponseEntity<?> createTourist(Object name);

    ResponseEntity<?> updateTourist(Long touristId, Object name);

    ResponseEntity<?> removeTourist(Long touristId);
}
