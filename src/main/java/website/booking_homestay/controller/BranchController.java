package website.booking_homestay.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import website.booking_homestay.DTO.create.BranchCreate;
import website.booking_homestay.DTO.update.BranchUpdate;
import website.booking_homestay.service.IAccountService;
import website.booking_homestay.service.IBranchService;

import java.util.List;

@RestController
@RequestMapping("api/branches")
@RequiredArgsConstructor
public class BranchController {
    private final IBranchService branchService;
    private final IAccountService accountService;

    @GetMapping("")
    public ResponseEntity<?> getAllBranches(){
        return branchService.getAllBranches();
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getBranchDetails(@PathVariable("id") Long branchId){
        return branchService.getBranchDetails(branchId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("")
    public ResponseEntity<?> createBranch(@RequestBody BranchCreate branchCreate){
        return branchService.createBranch(branchCreate);
    }

//    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBranch(@PathVariable("id") Long branchId,@RequestBody BranchUpdate branchUpdate){
        return branchService.updateBranch(branchId,branchUpdate);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}/managers")
    public ResponseEntity<?> getManagersBranchId(@PathVariable("id") Long branchId){
        return branchService.getManagersBranchId(branchId);
    }

    @GetMapping("/{id}/tourists")
    public ResponseEntity<?> getTouristsBranchId(@PathVariable("id") Long branchId){
        return branchService.getTouristsBranchId(branchId);
    }

//    @PreAuthorize("admin")

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<?> removeBranch(@PathVariable("id") Long branchId){
        System.out.println(branchId);
        return branchService.removeBranch(branchId);
    }


    @PostMapping("/{id}/tourists")
    public ResponseEntity<?> setTouristToBranch(@PathVariable("id") Long branchId,@RequestBody Long touristId){
        return branchService.setTouristToBranch(branchId,touristId);
    }

    @PostMapping("/{id}/tourists/{touristId}")
    public ResponseEntity<?> removeTouristToBranch(@PathVariable("id") Long branchId,@PathVariable("touristId") Long touristId){
        return branchService.removeTouristToBranch(branchId,touristId);
    }

    @GetMapping("/{id}/combobox")
    public ResponseEntity<?> getTouristNotBelongBranch(@PathVariable("id") Long branchId){
        return branchService.getTouristNotBelongBranch(branchId);
    }
//    @PutMapping("/activity/{id}")
//    public ResponseEntity<?> activityBranch(@PathVariable("id") Long branchId){
//        return branchService.activityBranch(branchId);
//    }

}
