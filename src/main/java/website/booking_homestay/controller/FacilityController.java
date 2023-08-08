package website.booking_homestay.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import website.booking_homestay.service.IFacilitiesService;

@RestController
@RequestMapping("api/facilities")
@RequiredArgsConstructor
public class FacilityController {
    private final IFacilitiesService facilitiesService;

    @GetMapping("")
    public ResponseEntity<?> getFacilities(){
        return facilitiesService.getFacilities();
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getFacilityDetails(@PathVariable("id") Long facilityId){
        return facilitiesService.getFacilityDetails(facilityId);
    }


    @GetMapping("{id}/homestays")
    public ResponseEntity<?> getHomestays(@PathVariable("id") Long facilityId){
        return facilitiesService.getHomestays(facilityId);
    }

    @PostMapping("")
    public ResponseEntity<?> createFacility(@RequestBody Object name){
        return facilitiesService.createFacility(name);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<?> updateFacility(@PathVariable("id") Long facilityId,@RequestBody Object name){
        return facilitiesService.updateFacility(facilityId,name);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> removeFacility(@PathVariable("id") Long facilityId){
        return facilitiesService.removeFacility(facilityId);
    }

}
