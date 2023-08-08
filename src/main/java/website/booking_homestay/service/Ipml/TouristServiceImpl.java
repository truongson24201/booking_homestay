package website.booking_homestay.service.Ipml;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import website.booking_homestay.DTO.view.BranchView;
import website.booking_homestay.entity.Branch;
import website.booking_homestay.entity.Tourist;
import website.booking_homestay.entity.User;
import website.booking_homestay.entity.enumreration.ERole;
import website.booking_homestay.repository.BranchRepository;
import website.booking_homestay.repository.TouristRepository;
import website.booking_homestay.service.IContextHolder;
import website.booking_homestay.service.ITouristService;
import website.booking_homestay.utils.MessageResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TouristServiceImpl implements ITouristService {
    private final TouristRepository touristRepository;
    private final ModelMapper modelMapper;
    private final BranchRepository branchRepository;
    private final IContextHolder contextHolder;


    private static final Logger logger = LoggerFactory.getLogger(TouristServiceImpl.class);


    @Transactional
    @Override
    public ResponseEntity<?> removeTourist(Long touristId) {
        Tourist tourist = touristRepository.findById(touristId).get();
        List<Branch> branches = tourist.getBranches();
        if (!branches.isEmpty()) {
            branches.forEach(branch -> {
                branch.getTourists().remove(tourist);
            });
            try {
                branchRepository.saveAll(branches);
            }catch (Exception e){
                logger.error("Remove tourist failed!");
                return ResponseEntity.badRequest().body(MessageResponse.ERROR_REMOVE);
            }
        }
        try {
            touristRepository.deleteById(touristId);
        }catch (Exception e){
            logger.error("Remove tourist failed!");
            return ResponseEntity.badRequest().body(MessageResponse.ERROR_REMOVE);
        }
        return ResponseEntity.ok("Remove tourist successfully!");
    }

    @Override
    public ResponseEntity<?> getBranchesOfTourist(Long touristId) {
        Tourist tourist = touristRepository.findById(touristId).get();
        List<Branch> branches = tourist.getBranches();
        List<BranchView> branchViews = new ArrayList<>();
        branches.forEach(branch -> {
            String address = branch.getProvince().getName() + ", " + branch.getDistrict().getName() + ", "+ branch.getWard().getName();
            BranchView branchView = modelMapper.map(branch,BranchView.class);
            branchView.setAddress(address);
            branchViews.add(branchView);
            if (branch.getStatus()) branchView.setStatus("OPEN");
            else branchView.setStatus("CLOSE");
        });
        return ResponseEntity.ok(branchViews);
    }

    @Override
    public ResponseEntity<?> getTouristDetails(Long touristId) {
        Tourist tourist = touristRepository.findById(touristId).get();
        return ResponseEntity.ok(tourist);
    }

    @Override
    public ResponseEntity<?> getTourists() {
        User user = contextHolder.getUser();
        if (user.getRole().getName().equals(ERole.MANAGER) && user.getBranch() == null){
            ResponseEntity.badRequest().body("you don't have any branch manager yet!");
        }
        return ResponseEntity.ok(touristRepository.findAll());
    }


    @Transactional
    @Override
    public ResponseEntity<?> createTourist(Object name) {
        Tourist tourist = new Tourist();
        User user = contextHolder.getUser();
        if (user.getRole().getName().equals(ERole.ADMIN)){
            tourist.setName(name.toString());
        }else {
            Branch branch = user.getBranch();
            if (branch == null) return ResponseEntity.badRequest().body("you don't have any branch manager yet!");
            tourist.setName(name.toString()+" ("+branch.getProvince().getName()+")");
        }
        try {
            tourist = touristRepository.save(tourist);
        }catch (Exception e){
            logger.error("Create tourist failed!");
            return ResponseEntity.badRequest().body(MessageResponse.ERROR_CREATE);
        }
        return ResponseEntity.ok(tourist);
    }

    @Transactional
    @Override
    public ResponseEntity<?> updateTourist(Long touristId, Object name) {
        Tourist tourist = touristRepository.findById(touristId).get();
        if (name == null) return ResponseEntity.badRequest().body("Name null");
        tourist.setName(name.toString());
        try {
            touristRepository.save(tourist);
        }catch (Exception e){
            logger.error("Update tourist failed!");
            return ResponseEntity.badRequest().body(MessageResponse.ERROR_UPDATE);
        }
        return ResponseEntity.ok("Update tourist successfully!");
    }

}
