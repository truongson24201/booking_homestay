package website.booking_homestay.service.Ipml;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import website.booking_homestay.DTO.FacilityDTO;
import website.booking_homestay.DTO.details.HomestayDetails;
import website.booking_homestay.entity.Branch;
import website.booking_homestay.entity.Facilities;
import website.booking_homestay.entity.Homestay;
import website.booking_homestay.entity.User;
import website.booking_homestay.entity.enumreration.ERole;
import website.booking_homestay.repository.FacilitiesRepository;
import website.booking_homestay.repository.HomestayRepository;
import website.booking_homestay.service.IContextHolder;
import website.booking_homestay.service.IFacilitiesService;
import website.booking_homestay.utils.MessageResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacilityServiceImpl implements IFacilitiesService {
    private final FacilitiesRepository facilitiesRepository;
    private final ModelMapper modelMapper;
    private final HomestayRepository homestayRepository;
    private final IContextHolder contextHolder;

    @Override
    public ResponseEntity<?> createFacility(Object name) {
        Facilities facility = new Facilities(name.toString());
        try {
            facility = facilitiesRepository.save(facility);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(MessageResponse.ERROR_CREATE);
        }
        return ResponseEntity.ok(facility);
    }

    @Override
    public ResponseEntity<?> getFacilities() {
        User user = contextHolder.getUser();
        if (user.getRole().getName().equals(ERole.MANAGER) && user.getBranch() == null){
            ResponseEntity.badRequest().body("you don't have any branch manager yet!");
        }
        return ResponseEntity.ok(facilitiesRepository.findAll());
    }

    @Override
    public ResponseEntity<?> updateFacility(Long facilityId,Object name) {
        Facilities facilities = facilitiesRepository.findById(facilityId).get();
        if (name == null) return ResponseEntity.badRequest().body("Name null");
        facilities.setName(name.toString());
        try {
            facilitiesRepository.save(facilities);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("update facility failed!");
        }
        return ResponseEntity.ok("update facility successfully!");
    }

    @Override
    public ResponseEntity<?> getHomestays(Long facilityId) {
        Facilities facilities = facilitiesRepository.findById(facilityId).get();
        List<Homestay> homestays = facilities.getHomestays();
        List<HomestayDetails> homestayDetails = homestays.stream().map(homestay -> modelMapper.map(homestay,HomestayDetails.class)).collect(Collectors.toList());
        return ResponseEntity.ok(homestayDetails);
    }

    @Override
    public ResponseEntity<?> getFacilityDetails(Long facilityId) {
        Facilities facilities = facilitiesRepository.findById(facilityId).get();
        return ResponseEntity.ok(facilities);
    }


    @Override
    public ResponseEntity<?> removeFacility(Long facilityId) {
        User user = contextHolder.getUser();
        Facilities facilities = facilitiesRepository.findById(facilityId).get();
        List<Homestay> homestays;
        if (user.getRole().getName().equals(ERole.ADMIN)){
            homestays = facilities.getHomestays();
            if (!homestays.isEmpty()){
                homestays.forEach(homestay -> {
                    homestay.getFacilities().remove(facilities);
                });
                homestayRepository.saveAll(homestays);
            }
            try {
                facilitiesRepository.delete(facilities);
            }catch (Exception e){
                return ResponseEntity.badRequest().body("Remove facility failed!");
            }
            return ResponseEntity.ok("Remove facility successfully!");
        }else {
            homestays = homestayRepository.findHomestaysByBranchIdAndFacilityId(user.getBranch().getBranchId(),facilityId);
            if (!homestays.isEmpty()){
                homestays.forEach(homestay -> {
                    homestay.getFacilities().remove(facilities);
                });
                try {
                    homestayRepository.saveAll(homestays);
                }catch (Exception e){
                    return ResponseEntity.badRequest().body("Remove facility out of homestay failed!");
                }
            }
            homestays = facilities.getHomestays();
            if (homestays.isEmpty()) {
                try {
                    facilitiesRepository.delete(facilities);
                }catch (Exception e){
                    return ResponseEntity.badRequest().body("Remove facility failed!");
                }
                return ResponseEntity.ok("Remove facility successfully!");
            }
            return ResponseEntity.badRequest().body("No Processing. Because NO homestays have this Facility!");
        }
    }
}
