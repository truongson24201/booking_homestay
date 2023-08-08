package website.booking_homestay.service;

import org.springframework.http.ResponseEntity;
import website.booking_homestay.DTO.create.BranchCreate;
import website.booking_homestay.DTO.update.BranchUpdate;
import website.booking_homestay.DTO.view.UserView;
import website.booking_homestay.entity.Branch;

import java.util.List;

public interface IBranchService {
    ResponseEntity<?> getAllBranches();

    public ResponseEntity<?> createBranch(BranchCreate branchCreate);

    ResponseEntity<?> updateBranch(Long branchId,BranchUpdate branchUpdate);

    ResponseEntity<?> removeBranch(Long branchId);

    ResponseEntity<?> getBranchesDropDown();

    ResponseEntity<?> activityBranch(Long branchId);

    Branch getBranchOfManager();

    ResponseEntity<?> getBranchDetails(Long branchId);

    ResponseEntity<?> getManagersBranchId(Long branchId);

    ResponseEntity<?> getTouristsBranchId(Long branchId);

    ResponseEntity<?> setManagerToBranch(Long branchId, UserView userView);

    ResponseEntity<?> removeManagerToBranch(Long branchId, Long accountId);

    ResponseEntity<?> setTouristToBranch(Long branchId, Long touristId);

    ResponseEntity<?> removeTouristToBranch(Long branchId, Long touristId);

    ResponseEntity<?> getTouristNotBelongBranch(Long branchId);

    ResponseEntity<?> getBranchesAddress(Object address);
}
