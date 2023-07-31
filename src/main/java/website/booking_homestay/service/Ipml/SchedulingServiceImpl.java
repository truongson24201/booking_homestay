package website.booking_homestay.service.Ipml;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import website.booking_homestay.DTO.details.HomesPricesDetails;
import website.booking_homestay.entity.HomesPrices;
import website.booking_homestay.entity.Homestay;
import website.booking_homestay.entity.Invoice;
import website.booking_homestay.entity.enumreration.EInvoice;
import website.booking_homestay.entity.enumreration.EPrice;
import website.booking_homestay.exception.BaseException;
import website.booking_homestay.repository.HomesPricesRepository;
import website.booking_homestay.repository.HomestayRepository;
import website.booking_homestay.repository.InvoiceRepository;
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

    private static final Logger logger = LoggerFactory.getLogger(SchedulingServiceImpl.class);

//    @Scheduled(fixedRate = 10000)
//    @Scheduled(cron = "0 0 0 * * ?") // daily
    public void UpdateStatusPrices(){
        List<HomesPrices> homesPrices;
        List<Homestay> homestays = homestayRepository.findAll();
        for (Homestay homestay : homestays){
            boolean flag = false; // homestay
            if (homestay.getFlag().equals(true)){
                homesPrices = homestay.getHomePrices();
                if (homesPrices.size() == 1){
                    HomesPrices homePriceTmp = homesPrices.get(0);
                    homePriceTmp.setStatus(EPrice.PRESENT);
                    try {
                        homesPricesRepository.save(homePriceTmp);
                    }catch (Exception e){
                        logger.error("Auto change status prices to homestay failed!");
                    }
                }else {
                    boolean present = false; // price
                    int j = 0;
                    for (int i = 0; i <  homesPrices.size() -1; i++){
                        int checkDate = homesPrices.get(i).getPriceList().getEffectiveDate().compareTo(new Date());
                        if (checkDate < 0){
                            homesPrices.get(i).setStatus(EPrice.PASSED);
                            j = i;
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
                        homestayRepository.save(homestay);
                        homesPricesRepository.saveAll(homesPrices);
                    }catch (Exception e){
                        logger.error("Auto change status prices to homestay failed!");
                    }
                }
            }
            if (!flag) homestay.setFlag(false); // no future -> don't need auto update -> change false
            try {
                homestayRepository.save(homestay);
            }catch (Exception e){
                logger.error("Auto change status prices to homestay failed!");
            }
        }
    }

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
            if (homesPrices.size() == 1){
                HomesPrices homePriceTmp = homesPrices.get(0);
                homePriceTmp.setStatus(EPrice.PRESENT);
                try {
                    homesPricesRepository.save(homePriceTmp);
                }catch (Exception e){
                    logger.error("Auto change status prices to homestay failed!");
                }
            }else {
                boolean present = false; // price
                int j = 0;
                Date maxPassed = null;
                HomesPrices homePrice;
                for (int i = 0; i <=  homesPrices.size() -1; i++){
                    int checkDate = homesPrices.get(i).getPriceList().getEffectiveDate().compareTo(new Date());
                    if (checkDate < 0){
                        homePrice = homesPrices.get(i);
                        homePrice.setStatus(EPrice.PASSED);
                        if (maxPassed == null || homePrice.getPriceList().getEffectiveDate().compareTo(maxPassed) > 0) {
                            maxPassed = homePrice.getPriceList().getEffectiveDate();
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
                    homestayRepository.save(homestay);
                    homesPricesRepository.saveAll(homesPrices);
                }catch (Exception e){
                    logger.error("Auto change status prices to homestay failed!");
                }
            }
            if (!flag) homestay.setFlag(false); // no future -> don't need auto update -> change false
            try {
                homestayRepository.save(homestay);
            }catch (Exception e){
                logger.error("Auto change status prices to homestay failed!");
            }
            List<HomesPricesDetails> details = new ArrayList<>();
            homesPrices.forEach(homePrice -> {
                HomesPricesDetails hp = HomesPricesDetails.map(homePrice);
                details.add(hp);
            });
            return ResponseEntity.ok(details);
        }
    }

//    @Scheduled(fixedRate = 10000)
//    @Scheduled(cron = "0 0 7 * * ?") // 7h am daily
    public void refreshInvoicesAuto(){
        List<Invoice> invoices = invoiceRepository.findByStatus(EInvoice.PAID);
        invoices.forEach(invoice -> {
            if (invoice.getCheckOut().after(new Date())){
                invoice.setStatus(EInvoice.EXPIRED);
            }
        });
        try {
            invoiceRepository.saveAll(invoices);
        }catch (Exception e){
            logger.error("Auto update status invoice failed!");
        }
    }

    public ResponseEntity<?> refreshInvoices(){
        List<Invoice> invoices = invoiceRepository.findByStatus(EInvoice.PAID);
        if (invoices.isEmpty()) return ResponseEntity.badRequest().body("Invoices Empty => no Processing!");
        AtomicBoolean flag = new AtomicBoolean(false);
        invoices.forEach(invoice -> {
            if (invoice.getCheckOut().before(new Date())){
                invoice.setStatus(EInvoice.EXPIRED);
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
