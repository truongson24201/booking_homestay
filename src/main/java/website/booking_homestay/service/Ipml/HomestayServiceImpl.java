package website.booking_homestay.service.Ipml;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import website.booking_homestay.DTO.chart.Ranks;
import website.booking_homestay.DTO.create.HomestayCreate;
import website.booking_homestay.DTO.details.*;
import website.booking_homestay.DTO.update.HomestayUpdate;
import website.booking_homestay.entity.*;
import website.booking_homestay.entity.enumreration.ERole;
import website.booking_homestay.entity.enumreration.EStatus;
import website.booking_homestay.exception.BaseException;
import website.booking_homestay.repository.*;
import website.booking_homestay.service.IContextHolder;
import website.booking_homestay.service.IHomestayService;
import website.booking_homestay.service.IImagesService;
import website.booking_homestay.utils.MessageResponse;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class HomestayServiceImpl implements IHomestayService {
    private final ModelMapper modelMapper;
    private final PricesRepository pricesRepository;
    private final IContextHolder contextHolder;
    private final BranchRepository branchRepository;
    private final HomestayRepository homestayRepository;
    private final IImagesService imagesService;
    private final HomestayImageRepository imageRepository;
    private final FacilitiesRepository facilityRepository;
    private final HomesPricesRepository homesPricesRepository;
    private final InvoiceRepository invoiceRepository;

    private static final Logger logger = LoggerFactory.getLogger(HomestayServiceImpl.class);


//    @Override
//    public ResponseEntity<?> getHomestaysClient(Long branchId, Date checkIn, Date checkOut, Integer numPeople) {
//        if (checkIn.before(new Date()) || checkOut.before(new Date())) return ResponseEntity.ok("Empty");
//        List<Homestay> homestays = homestayRepository.findAllByNumPeople(branchId,numPeople);
//        homestays.forEach(homestay -> {
//            if (invoiceRepository.checkInvoicesHomeMatch(homestay.getHomestayId(),checkIn,checkOut)){
//                homestays.remove(homestay);
//            }
//        });
//        List<HomestayClient> homestayClients = new ArrayList<>();
//        homestays.forEach(homestay -> {
//            HomestayClient homestayClient = modelMapper.map(homestay,HomestayClient.class);
//            HomesPrices homesPrices = homesPricesRepository.findByPricePresent(homestay.getHomestayId());
//            homestayClient.setPrice(homesPrices.getPriceList().getPrice());
//            homestayClients.add(homestayClient);
//        });
//        return ResponseEntity.ok(homestayClients);
//    }

    @Transactional
    @Override
    public ResponseEntity<?> createHomestay(Long branchId,HomestayCreate homestayCreate) {
        String username = contextHolder.getUsernameFromContext();
        Branch branch = branchRepository.findById(branchId).get();
        Homestay homestay = new Homestay(homestayCreate.getName(),homestayCreate.getNumPeople());
        homestay.setUpdateOn(new Date());
        homestay.setUpdateBy(username);
        homestay.setFlag(false);
        homestay.setStatus(EStatus.MAINTENANCE);
        homestay.setBranch(branch);
        try {
            homestayRepository.save(homestay);
        }catch (Exception e){
            logger.error("Save homestay failed!");
            return ResponseEntity.badRequest().body(MessageResponse.ERROR_CREATE);
        }
        return ResponseEntity.ok("Create successfully!");
    }

    @Transactional
    @Override
    public ResponseEntity<?> updateHomestay(Long homestayId,HomestayUpdate homestayUpdate) {
        String username = contextHolder.getUsernameFromContext();
        Homestay homestay = homestayRepository.findById(homestayId)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND,MessageResponse.NOT_FOUND));
        if (homestay.getHomePrices().isEmpty() && homestayUpdate.getStatus().equals(EStatus.OPEN.name()))
            return ResponseEntity.badRequest().body("Homestay doesn't any price, can't OPEN");
        modelMapper.map(homestayUpdate,homestay);
        homestay.setUpdateOn(new Date());
        homestay.setUpdateBy(username);
//        homestay.setStatus(EStatus.CLOSE);
        try {
            homestayRepository.save(homestay);
        }catch (Exception e){
            logger.error("Update homestay failed!");
            return ResponseEntity.badRequest().body(MessageResponse.ERROR_UPDATE);
        }
        return ResponseEntity.ok("Update successfully!");
    }

    @Transactional
    @Override
    public ResponseEntity<?> removeHomestay(Long homestayId) {
        String username = contextHolder.getUsernameFromContext();
        Homestay homestay = homestayRepository.findById(homestayId)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND,MessageResponse.NOT_FOUND));
        if (homestay.getInvoices().isEmpty() && homestay.getHomePrices().isEmpty()) {
//            List<Prices> prices = homestay.getPrices();
//            prices.forEach(price -> price.getHomestays().remove(homestay));
            try {
//                pricesRepository.saveAll(prices);
                homestayRepository.delete(homestay);
            } catch (Exception e) {
                logger.error("Remove homestay failed!");
                return ResponseEntity.badRequest().body(MessageResponse.ERROR_REMOVE);
            }
        } else {
            homestay.setStatus(EStatus.SHUTDOWN);
            homestay.setUpdateOn(new Date());
            homestay.setUpdateBy(username);
            try {
                homestayRepository.save(homestay);
            } catch (Exception e) {
                logger.error("Remove homestay failed!");
                return ResponseEntity.badRequest().body(MessageResponse.ERROR_REMOVE);
            }
        }

        return ResponseEntity.ok("Remove homestay successfully!");
    }

    @Override
    public ResponseEntity<?> getStatus() {
        EStatus[] status = EStatus.values();
        return ResponseEntity.ok(status);
    }


    @Override
    public ResponseEntity<?> getHomestays(Long branchId) {
        List<Homestay> homestays = homestayRepository.findAllByBranch_BranchId(branchId);
        List<HomestayDetails> homestayDetails = homestays.stream().map(homestay -> modelMapper.map(homestay,HomestayDetails.class)).collect(Collectors.toList());
        return ResponseEntity.ok(homestayDetails);
    }

    @Override
    public ResponseEntity<?> getHomestayDetails(Long homestayId) {
        Homestay homestay = homestayRepository.findById(homestayId).get();
        return ResponseEntity.ok(modelMapper.map(homestay,HomestayDetails.class));
    }

    @Override
    public ResponseEntity<?> getPricesOfHomestayId(Long homestayId) {
        Homestay homestay = homestayRepository.findById(homestayId)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND,MessageResponse.NOT_FOUND));
        List<HomesPricesDetails> details = homesPricesRepository.findPricesByHomeId(homestay.getHomestayId());
        return ResponseEntity.ok(details);
    }

    @Override
    public ResponseEntity<?> getImages(Long homestayId) {
        Homestay homestay = homestayRepository.findById(homestayId).get();
        return ResponseEntity.ok(homestay.getImages());
    }
    @Transactional
    @Override
    public ResponseEntity<?> removeImageOfHomestay(Long homestayId, Long imageId) {
        Homestay homestay = homestayRepository.findById(homestayId).get();
        homestay.getImages().remove(imageRepository.findById(imageId).get());
        try {
            homestayRepository.save(homestay);
        }catch (Exception e){
            logger.error("remove images failed!");
            return ResponseEntity.badRequest().body("remove images failed!");
        }
        return ResponseEntity.ok("remove image successfully!");
    }

    @Transactional
    @Override
    public ResponseEntity<?> setImagesOfHomestay(Long homestayId, MultipartFile[] images) throws IOException {
        Homestay homestay = homestayRepository.findById(homestayId).get();
        List<Images> imageList = new ArrayList<>();
        for (MultipartFile image : images){
            if(!image.isEmpty()){
                String fileName = imagesService.save(image);
                String urlImage = imagesService.getImageUrl(fileName);
                Images newImage = new Images(urlImage);
                imageList.add(newImage);
            }
        }
        try {
//            imageList = imageRepository.saveAll(imageList);
            homestay.getImages().addAll(imageList);
            homestayRepository.save(homestay);
        }catch (Exception e){
            logger.error("Save images failed!");
            return ResponseEntity.badRequest().body("Save images failed!");
        }
        return ResponseEntity.ok(homestay.getImages());
    }

    @Override
    public ResponseEntity<?> getFacilities(Long homestayId) {
        Homestay homestay = homestayRepository.findById(homestayId).get();
        return ResponseEntity.ok(homestay.getFacilities());
    }

    @Transactional
    @Override
    public ResponseEntity<?> setFacilityOfHomestay(Long homestayId, Long facilityId) {
        Homestay homestay = homestayRepository.findById(homestayId).get();
        Facilities facility = facilityRepository.findById(facilityId).get();
        homestay.getFacilities().add(facility);
        try {
            homestayRepository.save(homestay);
        }catch (Exception e){
            logger.error("Save facility failed!");
            return ResponseEntity.badRequest().body("Save facility failed!");
        }
        return ResponseEntity.ok(facility);
    }

    @Transactional
    @Override
    public ResponseEntity<?> removeFacilityOfHomestay(Long homestayId, Long facilityId) {
        Homestay homestay = homestayRepository.findById(homestayId).get();
        Facilities facility = facilityRepository.findById(facilityId).get();
        homestay.getFacilities().remove(facility);
        try {
            homestayRepository.save(homestay);
        }catch (Exception e){
            logger.error("remove facility failed!");
            return ResponseEntity.badRequest().body("remove facility failed!");
        }
        return ResponseEntity.ok(facility);
    }

    @Override
    public ResponseEntity<?> getFacilitiesNotBelongHome(Long homestayId) {
//        User user = contextHolder.getUser();
        Homestay homestay = homestayRepository.findById(homestayId).get();
        List<Facilities> facilities = facilityRepository.findFacilitiesNotBelongHome(homestay.getHomestayId());
        return ResponseEntity.ok(facilities);
    }

    @Override
    public ResponseEntity<?> getHomestayDetailsClient(Long homestayId) {
        Homestay homestay = homestayRepository.findById(homestayId).get();
        HomeClientDetail detail = modelMapper.map(homestay, HomeClientDetail.class);
        List<Tourist> tourists = homestay.getBranch().getTourists().stream().toList();
        detail.setTourists(tourists);
        HomesPrices homesPrices = homesPricesRepository.findByPricePresent(homestay.getHomestayId());
        detail.setPrice(homesPrices.getPrice());
        return ResponseEntity.ok(detail);
    }

    @Override
    public ResponseEntity<?> getHomestaysClient(Long branchId, Date checkIn, Date checkOut, Integer numPeople) {
        if (branchId == 0 || checkIn == null || checkOut == null || numPeople <= 0)
            return ResponseEntity.badRequest().body("Couldn't find it due to lack of information!");
        List<Homestay> homestays = homestayRepository.findHomestaysClient(numPeople,branchId,checkIn,checkOut);
        if (homestays.isEmpty()) return ResponseEntity.badRequest().body("No matching homestay found!");
        List<HomesClient> homesClients = new ArrayList<>();
        homestays.forEach(homestay -> {
            HomesPrices homesPrices = homesPricesRepository.findByPricePresent(homestay.getHomestayId());
            HomesClient homesClient = new HomesClient(homestay.getHomestayId(),homestay.getName(),
                    homestay.getNumPeople(),homesPrices.getPrice(),homestay.getImages().get(0));
            homesClients.add(homesClient);
        });
        return ResponseEntity.ok(homesClients);
    }

    public record HomesDTO(Long branchId,String address, String name, Double price, String image){}
    @Override
    public ResponseEntity<?> getHomestaysPublic() {
        List<Homestay> homestays = homestayRepository.findHomestayFromEachBranch();
        List<HomesDTO> homesDTOS = new ArrayList<>();
//        List<HomesClient> homesClients = new ArrayList<>();
        homestays.forEach(homestay -> {
            HomesPrices homesPrices = homesPricesRepository.findByPricePresent(homestay.getHomestayId());
            if (homesPrices != null){
                Branch branch = homestay.getBranch();
                HomesDTO homesDTO = new HomesDTO(branch.getBranchId(),branch.getProvince().getName()+", "+branch.getDistrict().getName()+", "+branch.getWard().getName(),
                        homestay.getName(),homesPrices.getPrice(),homestay.getImages().get(0).getUrl());
                homesDTOS.add(homesDTO);
            }
//            HomesClient homesClient = new HomesClient(homestay.getHomestayId(),homestay.getName(),
//                    homestay.getNumPeople(),homesPrices.getPrice(),homestay.getImages().get(0));
        });
        return ResponseEntity.ok(homesDTOS);
    }

    @Override
    public ResponseEntity<?> getRanks() {
        User user = contextHolder.getUser();
        List<Ranks> ranks;
        if (user.getRole().getName().equals(ERole.ADMIN)){
            ranks = branchRepository.getRanksAdmin();
            return ResponseEntity.ok(ranks);
        }else {
//            ranks =
        }
        return null;
    }


//    @Override
//    public ResponseEntity<?> getHomestayNotBelongBranch() {
//        List<Homestay> homestays = homestayRepository.findAllByBranchIsNull();
//        List<HomestayView> homestayViews = homestays.stream().map(homestay -> modelMapper.map(homestay,HomestayView.class)).collect(Collectors.toList());
//        return ResponseEntity.ok(homestayViews);
//    }

//    @Override
//    public ResponseEntity<?> createHomestay(MultipartFile[] images, HomestayDTO homestayDTO) throws IOException {
//        Homestay homestay = modelMapper.map(homestayDTO,Homestay.class);
//        List<String> urlImages = new ArrayList<>();
//        List<HomestayImage> homestayImages = new ArrayList<>();
//        for (MultipartFile image : images){
//            if(!image.isEmpty()){
//                String fileName = imagesService.save(image);
//                String urlImage = imagesService.getImageUrl(fileName);
//                HomestayImage homestayImage = new HomestayImage(urlImage);
//                homestayImages.add(homestayImage);
//            }
//        }
//        try {
//            homestayImages = imageRepository.saveAll(homestayImages);
//        }catch (Exception e){
//            logger.error("Save images failed!");
//            return ResponseEntity.badRequest().body("Save images failed!");
//        }
//        homestay.setHomestayImages(homestayImages);
//        try {
//            homestay = homestayRepository.save(homestay);
//        }catch (Exception e){
//            logger.error("Save homestay failed!");
//            return ResponseEntity.badRequest().body(MessageResponse.ERROR_CREATE);
//        }
//        return ResponseEntity.ok(modelMapper.map(homestay,HomestayDTO.class));
//    }
}
