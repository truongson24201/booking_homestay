package website.booking_homestay.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import website.booking_homestay.service.IInvoiceService;

@RestController
@RequestMapping("api/charts")
@RequiredArgsConstructor
public class ChartController {

    private final IInvoiceService invoiceService;

    @GetMapping("invoices/new")
    public ResponseEntity<?> getInvoicesNew(){
        return invoiceService.getInvoicesNew();
    }

    @GetMapping("invoices/pending")
    public ResponseEntity<?> getInvoicesPending(){
        return invoiceService.getInvoicesPending();
    }


}
