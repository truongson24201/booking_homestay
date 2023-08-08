package website.booking_homestay.service.Ipml;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import website.booking_homestay.DTO.details.HomesPricesDetails;
import website.booking_homestay.entity.*;
import website.booking_homestay.entity.enumreration.EInvoice;
import website.booking_homestay.entity.enumreration.EPrice;
import website.booking_homestay.entity.enumreration.ERole;
import website.booking_homestay.exception.BaseException;
import website.booking_homestay.repository.HomesPricesRepository;
import website.booking_homestay.repository.HomestayRepository;
import website.booking_homestay.repository.InvoiceRepository;
import website.booking_homestay.service.IContextHolder;
import website.booking_homestay.utils.MessageResponse;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class SchedulingServiceImpl {
    private final HomestayRepository homestayRepository;
    private final HomesPricesRepository homesPricesRepository;
    private final InvoiceRepository invoiceRepository;
    private final IContextHolder contextHolder;


    private static final Logger logger = LoggerFactory.getLogger(SchedulingServiceImpl.class);

//    @Scheduled(fixedRate = 10000)
//    @Scheduled(cron = "0 0 0 * * ?") // daily
    public void UpdateStatusPrices(){
        List<HomesPrices> homesPrices;
        List<Homestay> homestays = homestayRepository.findAllByFlagTrue();
        for (Homestay homestay : homestays){
            boolean flag = false; // homestay
            homesPrices = homestay.getHomePrices();
            boolean present = false; // price
            int j = 0;
            Date maxPassed = null;
            for (int i = 0; i <  homesPrices.size() -1; i++){
                int checkDate = homesPrices.get(i).getPriceList().getEffectiveDate().compareTo(new Date());
                if (checkDate < 0){
                    homesPrices.get(i).setStatus(EPrice.PASSED);
                    if (maxPassed == null || homesPrices.get(i).getPriceList().getEffectiveDate().compareTo(maxPassed) > 0) {
                        maxPassed = homesPrices.get(i).getPriceList().getEffectiveDate();
                        j = i;
                    }
                }else if (checkDate == 0){
                    homesPrices.get(i).setStatus(EPrice.PRESENT);
                    present = true;
                }else {
                    homesPrices.get(i).setStatus(EPrice.FUTURE);
                    flag = true;
                }
            }
            if (!present) homesPrices.get(j).setStatus(EPrice.PRESENT);

            try {
//                homestayRepository.save(homestay);
                homesPricesRepository.saveAll(homesPrices);
            }catch (Exception e){
                logger.error("Auto change status prices to homestay failed!");
            }
            if (!flag) homestay.setFlag(false); // no future -> don't need auto update -> change false
            try {
                homestayRepository.save(homestay);
            }catch (Exception e){
                logger.error("Auto change status prices to homestay failed!");
            }
        }
    }


    @Transactional
    public ResponseEntity<?> refreshPricesHomeId(Long homestayId){
        Homestay homestay = homestayRepository.findById(homestayId)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND, MessageResponse.NOT_FOUND));
        List<HomesPrices> homesPrices;
        boolean flag = false; // homestay
        if (!homestay.getFlag()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not processing");
        }
        else {
            homesPrices = homestay.getHomePrices();
            boolean present = false; // price
            boolean passed = false;
            int j = 0;
            Date maxPassed = null;
            for (int i = 0; i <=  homesPrices.size() -1; i++){
                Date current = new Date();
                if (homesPrices.get(i).getPriceList().getEffectiveDate().before(current)){
                    homesPrices.get(i).setStatus(EPrice.PASSED);
                    if (maxPassed == null || homesPrices.get(i).getPriceList().getEffectiveDate().after(maxPassed)) {
                        maxPassed = homesPrices.get(i).getPriceList().getEffectiveDate();
                        j = i;
                    }
                    passed = true;
                }else if (homesPrices.get(i).getPriceList().getEffectiveDate().after(current)){
                    homesPrices.get(i).setStatus(EPrice.FUTURE);
                    flag = true;
                }else {
                    homesPrices.get(i).setStatus(EPrice.PRESENT);
                    present = true;
                }
            }
            if (!present) {
                if (!flag) homesPrices.get(j).setStatus(EPrice.PRESENT);
                else {
                    if (passed) homesPrices.get(j).setStatus(EPrice.PRESENT);
                }
            }
            try {
//                    homestayRepository.save(homestay);
                homesPricesRepository.saveAll(homesPrices);
            }catch (Exception e){
                logger.error("Auto change status prices to homestay failed!");
            }
            // no future -> don't need auto update -> change false
            if (!flag) {
                homestay.setFlag(false);
                try {
                    homestayRepository.save(homestay);
                }catch (Exception e){
                    logger.error("Auto change status prices to homestay failed!");
                }
            }
            List<HomesPricesDetails> details = new ArrayList<>();
            homesPrices.forEach(i -> {
                HomesPricesDetails hp = HomesPricesDetails.map(i);
                details.add(hp);
            });
            return ResponseEntity.ok(details);
        }
    }

    @Transactional
    public ResponseEntity<?> refreshAllPrices(){
        User user = contextHolder.getUser();
        List<Homestay> homestays;
        if (user.getRole().getName().equals(ERole.ADMIN)){
            homestays = homestayRepository.findAllByFlagTrue();
        }else {
            homestays = homestayRepository.findAllByBranchAndTrue(user.getBranch().getBranchId());
        }
        List<HomesPrices> homesPrices;
        if (homestays.isEmpty()) ResponseEntity.badRequest().body("Homestay Empty -> No Processing!");
        for (Homestay homestay : homestays){
            boolean flag = false; // homestay
            homesPrices = homestay.getHomePrices();
            boolean present = false; // price
            boolean passed = false;
            int j = 0;
            Date maxPassed = null;
            for (int i = 0; i <=  homesPrices.size() -1; i++){
                Date current = new Date();
                if (homesPrices.get(i).getPriceList().getEffectiveDate().before(current)){
                    homesPrices.get(i).setStatus(EPrice.PASSED);
                    if (maxPassed == null || homesPrices.get(i).getPriceList().getEffectiveDate().after(maxPassed)) {
                        maxPassed = homesPrices.get(i).getPriceList().getEffectiveDate();
                        j = i;
                    }
                    passed = true;
                }else if (homesPrices.get(i).getPriceList().getEffectiveDate().after(current)){
                    homesPrices.get(i).setStatus(EPrice.FUTURE);
                    flag = true;
                }else {
                    homesPrices.get(i).setStatus(EPrice.PRESENT);
                    present = true;
                }
            }
            if (!present) {
                if (!flag) homesPrices.get(j).setStatus(EPrice.PRESENT);
                else {
                    if (passed) homesPrices.get(j).setStatus(EPrice.PRESENT);
                }
            }
            try {
//                    homestayRepository.save(homestay);
                homesPricesRepository.saveAll(homesPrices);
            }catch (Exception e){
                logger.error("Auto change status prices to homestay failed!");
            }
            // no future -> don't need auto update -> change false
            if (!flag){
                homestay.setFlag(false);
                try {
                    homestayRepository.save(homestay);
                }catch (Exception e){
                    logger.error("Auto change status prices to homestay failed!");
                }
            }
        }
        return ResponseEntity.ok("Refresh All prices successfully!");
    }

//    @Scheduled(fixedRate = 10000)
//    @Scheduled(cron = "0 0 7 * * ?") // 7h am daily
    public void refreshInvoicesAuto(){
        List<Invoice> invoices = invoiceRepository.findOfStatus(EInvoice.PAID);
        if (invoices.isEmpty()) return;
        AtomicBoolean flag = new AtomicBoolean(false);
        invoices.forEach(invoice -> {
            if (invoice.getCheckOut().before(new Date())){
                invoice.setStatus(EInvoice.CHECKOUT);
                flag.set(true);
            }
        });
        if(flag.get()){
            try {
                invoiceRepository.saveAll(invoices);
            }catch (Exception e){
                logger.error("Auto update status invoice failed!");
            }
        }
    }

    @Transactional
    public ResponseEntity<?> refreshInvoices(){
        User user = contextHolder.getUser();
        List<Invoice> invoices = new ArrayList<>();
        if (user.getRole().getName().equals(ERole.ADMIN)){
            invoices = invoiceRepository.findOfStatus(EInvoice.PAID);
        }
        else {
            Branch branch = user.getBranch();
            if (branch == null) return ResponseEntity.badRequest().body("You don't have branch!");
            invoices = invoiceRepository.findAllOfBranch(user.getBranch().getBranchId(),EInvoice.PAID);
        }
        if (invoices.isEmpty()) return ResponseEntity.badRequest().body("Invoices Empty => no Processing!");
        AtomicBoolean flag = new AtomicBoolean(false);
        invoices.forEach(invoice -> {
            if (invoice.getCheckOut().before(new Date())){
                invoice.setStatus(EInvoice.CHECKOUT);
                flag.set(true);
            }
        });
        if (flag.get()){
            try {
                invoiceRepository.saveAll(invoices);
                return ResponseEntity.ok("Refresh Invoice successfully!");
            }catch (Exception e){
                logger.error("Auto update status invoice failed!");
                return ResponseEntity.badRequest().body("Refresh Invoice failed!");
            }
        }else {
            return ResponseEntity.ok("No Processing!");
        }
    }

}
