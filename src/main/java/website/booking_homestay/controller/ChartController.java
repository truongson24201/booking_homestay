package website.booking_homestay.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import website.booking_homestay.service.IHomestayService;
import website.booking_homestay.service.IInvoiceService;

import java.util.Date;

@RestController
@RequestMapping("api/charts")
@RequiredArgsConstructor
public class ChartController {

    private final IInvoiceService invoiceService;
    private final IHomestayService iHomestayService;

    @GetMapping("invoices/new")
    public ResponseEntity<?> getInvoicesNew(){
        return invoiceService.getInvoicesNew();
    }

    @GetMapping("invoices/pending")
    public ResponseEntity<?> getInvoicesPending(){
        return invoiceService.getInvoicesPending();
    }

    @GetMapping("/invoices/total")
    public ResponseEntity<?> getTotalBooking(@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date from,
                                             @RequestParam(value = "to",required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date to) {
        return invoiceService.getTotalBooking(from,to);
    }

    @GetMapping("/invoices/active")
    public ResponseEntity<?> getTotalActive(@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date from,
                                             @RequestParam(value = "to",required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date to) {
        return invoiceService.getTotalActive(from,to);
    }

    @GetMapping("/invoices/cancel")
    public ResponseEntity<?> getTotalCancel(@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date from,
                                            @RequestParam(value = "to",required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date to) {
        return invoiceService.getTotalCancel(from,to);
    }

    @GetMapping("ranks")
    public ResponseEntity<?> getRanks() {
        return iHomestayService.getRanks();
    }

    @GetMapping("total")
    public ResponseEntity<?> getTotalOfYear(@RequestParam("year") int year){
        return invoiceService.getTotalOfYear(year);
    }

}
