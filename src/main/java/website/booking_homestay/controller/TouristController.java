package website.booking_homestay.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import website.booking_homestay.service.ITouristService;

@RestController
@RequestMapping("api/tourists")
@RequiredArgsConstructor
public class TouristController {
    private final ITouristService touristService;

    @GetMapping("")
    public ResponseEntity<?> getTourists(){
        return touristService.getTourists();
    }

    @PostMapping("")
    public ResponseEntity<?> createTourist(@RequestBody Object name){
        return touristService.createTourist(name);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<?> updateTourist(@PathVariable("id") Long touristId,@RequestBody Object name){
        return touristService.updateTourist(touristId,name);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<?> removeTourist(@PathVariable("id") Long touristId){
        return touristService.removeTourist(touristId);
    }

    @GetMapping("/{id}/branches")
    public ResponseEntity<?> getBranchesOfTourist(@PathVariable("id") Long touristId){
        return touristService.getBranchesOfTourist(touristId);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getTouristDetails(@PathVariable("id") Long touristId){
        return touristService.getTouristDetails(touristId);
    }
}
