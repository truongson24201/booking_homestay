package website.booking_homestay.service;

import org.springframework.http.ResponseEntity;

public interface IFacilitiesService {
    ResponseEntity<?> createFacility(Object name);

    ResponseEntity<?> getFacilities();

    ResponseEntity<?> updateFacility(Long facilityId,Object name);

    ResponseEntity<?> getHomestays(Long facilityId);

    ResponseEntity<?> getFacilityDetails(Long facilityId);

    ResponseEntity<?> removeFacility(Long facilityId);
}
