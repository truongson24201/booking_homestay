package website.booking_homestay.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import website.booking_homestay.DTO.create.HomestayCreate;
import website.booking_homestay.DTO.update.HomestayUpdate;

import java.io.IOException;
import java.util.Date;

public interface IHomestayService {


    ResponseEntity<?> removeHomestay(Long homestayId);// admin

    ResponseEntity<?> getStatus();

    ResponseEntity<?> getHomestays(Long branchId);

    ResponseEntity<?> getHomestayDetails(Long homestayId);

    ResponseEntity<?> createHomestay(Long branchId,HomestayCreate homestayCreate); // admin, manager update images if i can code

    ResponseEntity<?> updateHomestay(Long homestayId,HomestayUpdate homestayUpdate);

    ResponseEntity<?> getPricesOfHomestayId(Long homestayId);

    ResponseEntity<?> getImages(Long homestayId);

    ResponseEntity<?> removeImageOfHomestay(Long homestayId, Long imageId);

    ResponseEntity<?> setImagesOfHomestay(Long homestayId, MultipartFile[] images) throws IOException;

    ResponseEntity<?> getFacilities(Long homestayId);

    ResponseEntity<?> setFacilityOfHomestay(Long homestayId, Long facilityId);

    ResponseEntity<?> removeFacilityOfHomestay(Long homestayId, Long facilityId);

    ResponseEntity<?> getFacilitiesNotBelongHome(Long homestayId);

    ResponseEntity<?> getHomestayDetailsClient(Long homestayId);

    ResponseEntity<?> getHomestaysClient(Long branchId, Date checkIn, Date checkOut, Integer numPeople);

    ResponseEntity<?> getHomestaysPublic();

    ResponseEntity<?> getRanks();
}
