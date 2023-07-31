package website.booking_homestay.service.Ipml;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import website.booking_homestay.DTO.create.HomePrice;
import website.booking_homestay.DTO.details.HomesPricesDetails;
import website.booking_homestay.DTO.details.HomestayDetails;
import website.booking_homestay.entity.*;
import website.booking_homestay.entity.enumreration.EPrice;
import website.booking_homestay.entity.enumreration.ERole;
import website.booking_homestay.exception.BaseException;
import website.booking_homestay.repository.BranchRepository;
import website.booking_homestay.repository.HomesPricesRepository;
import website.booking_homestay.repository.HomestayRepository;
import website.booking_homestay.repository.PricesRepository;
import website.booking_homestay.service.IContextHolder;
import website.booking_homestay.service.IPricesService;
import website.booking_homestay.utils.MessageResponse;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PricesServiceImpl implements IPricesService {

    private final PricesRepository pricesRepository;
    private final ModelMapper modelMapper;
    private final IContextHolder contextHolder;
    private final HomesPricesRepository homesPricesRepository;
    private final HomestayRepository homestayRepository;
    private final BranchRepository branchRepository;

    private static final Logger logger = LoggerFactory.getLogger(PricesServiceImpl.class);


    @Override
    public ResponseEntity<?> getPriceList(Long branchId) {
        User user = contextHolder.getUser();
        if (user.getRole().getName().equals(ERole.MANAGER) && user.getBranch().getBranchId() != branchId){ //all
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You don't have permission access this resources!");
        }
        List<PriceList> prices = pricesRepository.findAllByBranchId(branchId);
        return ResponseEntity.ok(prices);
    }


    @Override
    public ResponseEntity<?> getPriceDetails(Long priceId) {
        PriceList price = pricesRepository.findById(priceId).get();
        return ResponseEntity.ok(price);
    }

    @Transactional
    @Override
    public ResponseEntity<?> createPrice(Long branchId,Date priceListCreate) {
        User user = contextHolder.getUser();
        if (user.getRole().getName().equals(ERole.MANAGER) && user.getBranch().getBranchId() != branchId){ //all
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You don't have permission access this resources!");
        }
        int check = priceListCreate.compareTo(new Date());
        if (check < 0 || check == 0 ){
            return ResponseEntity.badRequest().body("The applicable date cannot be less equal than the current or existing date of application!");
        }

        Optional<PriceList> pricesCheck = pricesRepository.findByEffectiveDate(branchId,priceListCreate);
        if (!pricesCheck.isEmpty()) return ResponseEntity.badRequest().body("Effective date already exists");
        Branch branch = branchRepository.findById(branchId).get();
        PriceList prices = new PriceList(priceListCreate,new Date(),user.getUsername(),branch);
        try {
            prices = pricesRepository.save(prices);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(MessageResponse.ERROR_CREATE);
        }
        return ResponseEntity.ok(prices);
    }

    @Transactional
    @Override
    public ResponseEntity<?> updatePrice(Long pricelistId,Date priceListCreate) {
        int check = priceListCreate.compareTo(new Date());
        if (check < 0 || check == 0 ){
            return ResponseEntity.badRequest().body("The applicable date cannot be less equal than the current or existing date of application!");
        }
        PriceList priceList = pricesRepository.findById(pricelistId).get();
        Optional<PriceList> pricesCheck = pricesRepository.findByEffectiveDate(priceList.getBranch().getBranchId(),priceListCreate);
        if (!pricesCheck.isEmpty()) return ResponseEntity.badRequest().body("Effective date already exists");
        priceList.setEffectiveDate(priceListCreate);
        priceList.setUpdateBy(contextHolder.getUsernameFromContext());
        priceList.setUpdateOn(new Date());
        try {
            pricesRepository.save(priceList);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(MessageResponse.ERROR_UPDATE);
        }
        return ResponseEntity.ok("Update price successfully!");
    }

    @Transactional
    @Override
    public ResponseEntity<?> removePrice(Long pricelistId) {
        PriceList priceList = pricesRepository.findById(pricelistId)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND,MessageResponse.NOT_FOUND));
        if (priceList.getHomePrices().isEmpty()){
            try {
                pricesRepository.delete(priceList);
            }catch (Exception e){
                return ResponseEntity.badRequest().body(MessageResponse.ERROR_REMOVE);
            }
            return ResponseEntity.ok("Remove price successfully!");
        }else {
            return ResponseEntity.badRequest().body("Price already have homestay available, cannot be deleted");
        }
    }

    @Override
    public ResponseEntity<?> getHomestaysOfPrice(Long priceId) {
        List<HomesPricesDetails> homesPrices = homesPricesRepository.findHomesPricesByPriceId(priceId);
        return ResponseEntity.ok(homesPrices);
//        List<Object[]> resultList = homesPricesRepository.findHomesPricesByPriceId(priceId);
//        List<HomesPricesDetails> homesPricesDetailsList = new ArrayList<>();
//        for (Object[] result : resultList) {
//            HomesPricesDetails details = new HomesPricesDetails(
//                    (Long) result[0],        // homestayId
//                    (Long) result[1],        // pricelistId
//                    (String) result[2],      // name
//                    (Integer) result[3],      // numPeople
//                    (Date) result[4],        // effectiveDate
//                    (Double) result[5],      // price
//                    (String) result[6]       // status
//            );
//            homesPricesDetailsList.add(details);
//        }
    }

    public boolean isEffectiveDateExists(List<HomesPrices> homesPrices, Date targetEffectiveDate) {
        return homesPrices.stream()
                .anyMatch(homePrice -> homePrice.getPriceList().getEffectiveDate().equals(targetEffectiveDate));
    }

//    @Transactional
//    @Override
//    public ResponseEntity<?> setHomestayInOf(Long priceId, HomePrice homePrice) {
//        Homestay homestay = homestayRepository.findById(homePrice.getHomestayId()).get();
//        PriceList price = pricesRepository.findById(priceId).get();
//        int checkDate = price.getEffectiveDate().compareTo(new Date());
//        if (checkDate < 0 || checkDate == 0){
//            return ResponseEntity.badRequest().body("The Effective Date PASSED!, Can not SET to Homestay");
//        }
//        if(isEffectiveDateExists(homestay.getHomePrices(),price.getEffectiveDate())){
//            return ResponseEntity.badRequest().body("The effective date has been used in this homestay");
//        }
//        HomesPrices homesPrices = new HomesPrices(homestay,price);
//        homesPrices.setPrice(homePrice.getPrice());
//        homesPrices.setStatus(EPrice.PENDING);
//        try {
//            homestay.setFlag(true);
//            homestayRepository.save(homestay);
//            homesPricesRepository.save(homesPrices);
//        }catch (Exception e){
//            logger.error("Set price to homestay failed!");
//            return ResponseEntity.badRequest().body("Set price to homestay failed!");
//        }
//        HomesPricesDetails details = HomesPricesDetails.map(homesPrices);
//        return ResponseEntity.ok(details);
//    }

    @Transactional
    @Override
    public ResponseEntity<?> setHomestayInOf(Long priceId, HomePrice homePrice) {
        PriceList price = pricesRepository.findById(priceId).get();
        int checkDate = price.getEffectiveDate().compareTo(new Date());
        if (checkDate < 0 || checkDate == 0){
            return ResponseEntity.badRequest().body("The Effective Date PASSED!, Can not SET to Homestay");
        }
        List<Homestay> homestays = new ArrayList<>();
        List<HomesPrices> homesPrices = new ArrayList<>();
        for (Long i : homePrice.getHomestayId()){
            Homestay homestay = homestayRepository.findById(i).get();
//            if(isEffectiveDateExists(homestay.getHomePrices(),price.getEffectiveDate())){
//                return ResponseEntity.badRequest().body("The effective date has been used in this homestay");
//            }
            homestays.add(homestay);
            HomesPrices hP = new HomesPrices(homestay,price);
            hP.setPrice(homePrice.getPrice());
            hP.setStatus(EPrice.PENDING);
            homesPrices.add(hP);
            homestay.setFlag(true);
        }
        try {
            homestayRepository.saveAll(homestays);
            homesPricesRepository.saveAll(homesPrices);
        }catch (Exception e){
            logger.error("Set price to homestay failed!");
            return ResponseEntity.badRequest().body("Set price to homestay failed!");
        }
        List<HomesPricesDetails> details = new ArrayList<>();
        homesPrices.forEach(i -> {
            HomesPricesDetails detail = HomesPricesDetails.map(i);
            details.add(detail);
        });
        return ResponseEntity.ok(details);
    }

    @Transactional
    @Override
    public ResponseEntity<?> removeHomestayOutOf(Long priceId, Long homestayId) {
        HomesPrices homesPrices = homesPricesRepository.findHomesPricesById(homestayId,priceId);
        int checkDate = homesPrices.getPriceList().getEffectiveDate().compareTo(new Date());
        if (checkDate < 0 || checkDate == 0) return ResponseEntity.badRequest().body("The Effective Date PASSED!, Can not REMOVE homestay out of this");
        try {
           homesPricesRepository.delete(homesPrices);
        }catch (Exception e){
           logger.error("Remove homestay out of price failed!");
           return ResponseEntity.badRequest().body("Remove homestay out of price failed!");
        }
        HomesPricesDetails details = HomesPricesDetails.map(homesPrices);
        return ResponseEntity.ok(details);
    }


    @Override
    public ResponseEntity<?> getHomesNotBelongToPrice(Long priceId) {
        List<Homestay> homestays = homesPricesRepository.findHomesNoBelongPriceId(priceId);
        List<HomestayDetails> homestayDetails = homestays.stream().map(homestay -> modelMapper.map(homestay,HomestayDetails.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(homestayDetails);
    }


    // Do not use because after the application date is automatically expired
//    @Override
//    public ResponseEntity<?> activityPrice(Long priceId) {
////        Prices prices = pricesRepository.findById(priceId)
////                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND,MessageResponse.NOT_FOUND));
////        prices.setStatus(!prices.getStatus());
////        try {
////            pricesRepository.save(prices);
////        }catch (Exception e){
////            return ResponseEntity.badRequest().body(MessageResponse.ERROR_UPDATE);
////        }
//        return ResponseEntity.ok("Change status of price successfully!");
//    }
}
