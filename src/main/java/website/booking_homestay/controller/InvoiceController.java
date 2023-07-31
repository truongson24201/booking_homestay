package website.booking_homestay.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import website.booking_homestay.DTO.InvoiceDTO;
import website.booking_homestay.DTO.update.InvoiceUpdate;
import website.booking_homestay.entity.enumreration.ECardType;
import website.booking_homestay.entity.enumreration.EInvoice;
import website.booking_homestay.service.IInvoiceService;
import website.booking_homestay.service.Ipml.SchedulingServiceImpl;

@RestController
@RequestMapping("api/invoices")
@RequiredArgsConstructor
public class InvoiceController {
    private final IInvoiceService invoiceService;
    private final SchedulingServiceImpl schedulingService;

    @GetMapping("")
    public ResponseEntity<?> getInvoices(@RequestParam(value = "status",defaultValue = "all") String status){
        return invoiceService.getInvoices(status);
    }

    @GetMapping("search")
    public ResponseEntity<?> searchInvoice(@RequestParam(name = "search") String emailOrPhone){
        return invoiceService.searchInvoice(emailOrPhone);
    }

    //client
    @GetMapping("{id}")
    public ResponseEntity<?> getInvoiceDetails(@PathVariable(name = "id") Long invoiceId){
        return invoiceService.getInvoiceDetails(invoiceId);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateInvoice(@PathVariable("id") Long invoiceId,@RequestBody InvoiceUpdate invoiceUpdate){
        return invoiceService.updateInvoice(invoiceId,invoiceUpdate);
    }


    // manager
    @GetMapping("details-full/{id}")
    public ResponseEntity<?> getInvoiceDetailsFull(@PathVariable(name = "id") Long invoiceId){
        return invoiceService.getInvoiceDetailsFull(invoiceId);
    }

    @GetMapping("info-update/{id}")
    public ResponseEntity<?> getInfoUpdateInvoice(@PathVariable("id") Long invoiceId){
        return invoiceService.getInfoUpdateInvoice(invoiceId);
    }

    @GetMapping("combobox-invoice")
    public ResponseEntity<?> getComboboxInvoice(@RequestParam("flag") Boolean flag){
        if (flag) {
            EInvoice[] statusValues = EInvoice.values();
            return ResponseEntity.ok(statusValues);
        }else {
            return invoiceService.getStatus();
        }
    }

    @GetMapping("combobox-card")
    public ResponseEntity<?> getComboboxCard(){
        ECardType[] statusValues = ECardType.values();
        return ResponseEntity.ok(statusValues);
    }

    @PutMapping("refresh")
    public ResponseEntity<?> refreshInvoices(){
        return schedulingService.refreshInvoices();
    }

}
