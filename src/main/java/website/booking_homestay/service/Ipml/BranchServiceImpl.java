package website.booking_homestay.service.Ipml;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import website.booking_homestay.DTO.create.BranchCreate;
import website.booking_homestay.DTO.details.BranchDetails;
import website.booking_homestay.DTO.update.BranchUpdate;
import website.booking_homestay.DTO.view.BranchView;
import website.booking_homestay.DTO.view.UserView;
import website.booking_homestay.entity.Branch;
import website.booking_homestay.entity.Homestay;
import website.booking_homestay.entity.Tourist;
import website.booking_homestay.entity.User;
import website.booking_homestay.entity.enumreration.ERole;
import website.booking_homestay.entity.enumreration.EStatus;
import website.booking_homestay.exception.BaseException;
import website.booking_homestay.repository.BranchRepository;
import website.booking_homestay.repository.HomestayRepository;
import website.booking_homestay.repository.TouristRepository;
import website.booking_homestay.repository.UserRepository;
import website.booking_homestay.service.IContextHolder;
import website.booking_homestay.service.IBranchService;
import website.booking_homestay.utils.MessageResponse;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements IBranchService {
    private final BranchRepository branchRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final IContextHolder contextHolder;
    private final HomestayRepository homestayRepository;
    private final TouristRepository touristRepository;

    private static final Logger logger = LoggerFactory.getLogger(BranchServiceImpl.class);

    // client
//    public ResponseEntity<?> getBranchList(String search){
//
//    }

    @Override
    public ResponseEntity<?> getAllBranches() {
        User user = contextHolder.getUser();
        List<Branch> branches = new ArrayList<>();
        if (user.getRole().getName().equals(ERole.ADMIN)){
            branches = branchRepository.findAll();
        }else {
            Branch branch = branchRepository.findById(user.getBranch().getBranchId()).get();
            branches.add(branch);
        }
        List<BranchDetails> branchViews = branches.stream().map(branch -> modelMapper.map(branch,BranchDetails.class)).collect(Collectors.toList());
        return ResponseEntity.ok(branchViews);
    }

    @Override
    public ResponseEntity<?> getBranchDetails(Long branchId) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND,MessageResponse.NOT_FOUND));
        return ResponseEntity.ok(modelMapper.map(branch, BranchDetails.class));
    }


    @Transactional
    @Override
    public ResponseEntity<?> createBranch(BranchCreate branchCreate) {
        Branch branch = modelMapper.map(branchCreate,Branch.class);
//        System.out.println(branch);
//        branch.setStatus(false);
//        List<User> users = new ArrayList<>();
//        branchDTO.getManagers().forEach(managerDTO -> users.add(userRepository.findById(managerDTO.getAccountId()).get()));
        branch.setStatus(true);
        try {
            branchRepository.save(branch);
//            users.forEach(adminManager -> adminManager.setBranch(branch));
//            userRepository.saveAll(users);
        }catch (Exception e){
            logger.error("Create or update branch failed!");
            return ResponseEntity.badRequest().body(MessageResponse.ERROR_CREATE);
        }
        return ResponseEntity.ok("Create branch successfully!");
    }

    @Transactional
    @Override
    public ResponseEntity<?> updateBranch(Long branchId,BranchUpdate branchUpdate) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND,MessageResponse.NOT_FOUND));
//        List<User> oldManagers = userRepository.findAllByBranchId(branch.getBranchId());
//        List<User> newManagers = new ArrayList<>();
//        oldManagers.forEach(adminManager -> adminManager.setBranch(null));
//        branchDTO.getManagers().forEach(managerDTO -> newManagers.add(userRepository.findById(managerDTO.getAccountId()).get()));
        List<Homestay> homestays = branch.getHomestays();
        List<Homestay> homestaysFilter;
        if (!homestays.isEmpty()){
            if (!branchUpdate.getStatus()){ //  false -> close all homestay
                homestaysFilter = homestays.stream()
                        .filter(homestay -> homestay.getStatus().name().equals(EStatus.OPEN.name()))
                        .collect(Collectors.toList());
                homestaysFilter.forEach(homestay -> homestay.setStatus(EStatus.CLOSE));
            }else { // True -> Open all
                homestaysFilter = homestays.stream()
                        .filter(homestay -> homestay.getStatus().name().equals(EStatus.CLOSE.name()))
                        .collect(Collectors.toList());
                homestaysFilter.forEach(homestay -> homestay.setStatus(EStatus.OPEN));
            }
            try {
                homestayRepository.saveAll(homestaysFilter);
            }catch (Exception e){
                logger.error("Update branch failed!");
                return ResponseEntity.badRequest().body(MessageResponse.ERROR_UPDATE);
            }
        }

        if ( branchUpdate.getProvinceId() != null || branchUpdate.getDistrictId() != null || branchUpdate.getWardId() != null){
            branch.setProvince(null);
            branch.setDistrict(null);
            branch.setWard(null);
        }
        modelMapper.map(branchUpdate,branch);
        try {
            branchRepository.save(branch);
//            newManagers.forEach(adminManager -> adminManager.setBranch(branch));
//            userRepository.saveAll(oldManagers);
//            userRepository.saveAll(newManagers);
        }catch (Exception e){
            logger.error("Update branch failed!");
            return ResponseEntity.badRequest().body(MessageResponse.ERROR_UPDATE);
        }
//        branchDTO.setBranchId(branch.getBranchId());
        return ResponseEntity.ok("Update branch successfully!");
    }

    @Transactional
    @Override
    public ResponseEntity<?> removeBranch(Long branchId) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND,MessageResponse.NOT_FOUND));
        List<User> users = userRepository.findAllByBranchId(branch.getBranchId());
        List<Homestay> homestays = branch.getHomestays();
        if (!homestays.isEmpty() || !users.isEmpty()){
            List<Homestay> homestaysFilter;
            if (branch.getStatus()){ //  true -> close all homestay
                homestaysFilter = homestays.stream()
                        .filter(homestay -> homestay.getStatus().name().equals(EStatus.OPEN.name()))
                        .collect(Collectors.toList());
                homestaysFilter.forEach(homestay -> homestay.setStatus(EStatus.CLOSE));
                branch.setStatus(false);
                try {
                    homestayRepository.saveAll(homestaysFilter);
                    branchRepository.save(branch);
                }catch (Exception e){
                    logger.error("Remove branch failed!");
                    return ResponseEntity.badRequest().body(MessageResponse.ERROR_REMOVE);
                }
            }
        } else {
            try {
                branchRepository.delete(branch);
            }catch (Exception e){
                logger.error("Remove branch failed!");
                return ResponseEntity.badRequest().body(MessageResponse.ERROR_REMOVE);
            }
        }
        System.out.println("Ok");
        return ResponseEntity.ok("Remove branch successfully!");
    }



    // no use
    @Override
    public ResponseEntity<?> activityBranch(Long branchId) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(()-> new BaseException(HttpStatus.NOT_FOUND,MessageResponse.NOT_FOUND));
        List<Homestay> homestays = branch.getHomestays();
        List<Homestay> homestaysFilter;
        if (branch.getStatus()){ // change to false -> close all homestay
            homestaysFilter = homestays.stream()
                    .filter(homestay -> homestay.getStatus().name().equals(EStatus.OPEN.name()))
                    .collect(Collectors.toList());
            homestaysFilter.forEach(homestay -> homestay.setStatus(EStatus.CLOSE));
        }else { // change to True -> Open all
            homestaysFilter = homestays.stream()
                    .filter(homestay -> homestay.getStatus().name().equals(EStatus.CLOSE.name()))
                    .collect(Collectors.toList());
            homestaysFilter.forEach(homestay -> homestay.setStatus(EStatus.OPEN));
        }
        branch.setStatus(!branch.getStatus());
        try {
            homestayRepository.saveAll(homestaysFilter);
            branchRepository.save(branch);
        }catch (Exception e){
            logger.error("update status of branch failed!");
            return ResponseEntity.badRequest().body(MessageResponse.ERROR_UPDATE);
        }
        return ResponseEntity.ok("Change status of branch successfully!");
    }

    // client use to search
    @Override
    public ResponseEntity<?> getBranchesDropDown() {
        List<Branch> branches = branchRepository.findAllByStatusTrue();
        List<BranchView> branchViews = branches.stream()
                .map(branch -> new BranchView(branch.getBranchId(), branch.getName(),
                        branch.getProvince().getName()+", "+branch.getDistrict().getName()+", "+branch.getWard().getName()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(branchViews);
    }


    @Override
    public Branch getBranchOfManager() {
        User user = contextHolder.getUser();
        return user.getBranch();
    }


    @Override
    public ResponseEntity<?> getManagersBranchId(Long branchId) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND,MessageResponse.NOT_FOUND));
        List<User> users = branch.getManagers();
        List<UserView> userViews = users.stream().map(user -> modelMapper.map(user,UserView.class)).collect(Collectors.toList());
        return ResponseEntity.ok(userViews);
    }

    @Override
    public ResponseEntity<?> getTouristsBranchId(Long branchId) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND,MessageResponse.NOT_FOUND));
        List<Tourist> tourists = branch.getTourists();
        return ResponseEntity.ok(tourists);
    }

    @Transactional
    @Override
    public ResponseEntity<?> setManagerToBranch(Long branchId, UserView userView) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND,MessageResponse.NOT_FOUND));
        User user = userRepository.findById(userView.getAccountId()).get();
        user.setBranch(branch);
        try {
            userRepository.save(user);
        }catch (Exception e){
            logger.error("Set manager of branch failed!");
            return ResponseEntity.badRequest().body("Set manager of branch failed!");
        }
        return ResponseEntity.ok("Set managers to branch successfully!");
    }

    @Transactional
    @Override
    public ResponseEntity<?> removeManagerToBranch(Long branchId, Long accountId) {
        User user = userRepository.findById(accountId)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND,MessageResponse.NOT_FOUND));
        user.setBranch(null);
        try {
            userRepository.save(user);
        }catch (Exception e){
            logger.error("remove manager out of branch failed!");
            return ResponseEntity.badRequest().body("remove manager out of branch failed!");
        }
        return ResponseEntity.ok("remove managers out of branch successfully!");
    }

    @Transactional
    @Override
    public ResponseEntity<?> setTouristToBranch(Long branchId, Long touristId) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND,MessageResponse.NOT_FOUND));
        Tourist tourist = touristRepository.findById(touristId).get();
        branch.getTourists().add(tourist);
        try {
            branchRepository.save(branch);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Add tourist to branch failed!");
        }
        return ResponseEntity.ok(tourist);
    }

    @Transactional
    @Override
    public ResponseEntity<?> removeTouristToBranch(Long branchId, Long touristId) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND,MessageResponse.NOT_FOUND));
        Tourist tourist = touristRepository.findById(touristId).get();
        branch.getTourists().remove(tourist);

        try {
            branchRepository.save(branch);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("remove tourist to branch failed!");
        }
        return ResponseEntity.ok(tourist);
    }


    @Override
    public ResponseEntity<?> getTouristNotBelongBranch(Long branchId) {
        Branch branch = branchRepository.findById(branchId).get();
        List<Tourist> tourists = touristRepository.findTouristsNotBelongBranch(branchId);
        return ResponseEntity.ok(tourists);
    }

    @Override
    public ResponseEntity<?> getBranchesAddress(Object address) {
        List<Branch> branches = branchRepository.findAllByProvince(address.toString());
        if (branches.isEmpty()) {
            branches = branchRepository.findAllByTouristName(address.toString());
        }
        List<BranchView> branchViews = branches.stream()
                .map(branch -> new BranchView(branch.getBranchId(), branch.getName(),
                        branch.getProvince().getName()+", "+branch.getDistrict().getName()+", "+branch.getWard().getName()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(branchViews);
    }


}
