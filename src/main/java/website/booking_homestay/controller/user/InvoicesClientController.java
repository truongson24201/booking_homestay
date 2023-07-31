package website.booking_homestay.controller.user;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import website.booking_homestay.DTO.RegisterHomestay;
import website.booking_homestay.DTO.RequestBooking;
import website.booking_homestay.service.IInvoiceService;

@RestController
@RequestMapping("api/clients/invoice")
@RequiredArgsConstructor
public class InvoicesClientController {
    private final IInvoiceService invoiceService;

    @PostMapping("create")
    public ResponseEntity<?> bookingHomestay(@RequestBody RegisterHomestay booking){
        return invoiceService.registerHomestay(booking);
    }

    @GetMapping("info")
    public ResponseEntity<?> getInvoiceRegister(@RequestBody RequestBooking booking){
        return invoiceService.getRegisterHomestay(booking);
    }

    @DeleteMapping("cancel/{id}")
    public ResponseEntity<?> cancelInvoice(@PathVariable("id") Long invoiceId){
        return invoiceService.cancelInvoice(invoiceId);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllInvoice(){
        return invoiceService.getAllInvoice();
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<?> getInvoiceDetails(@PathVariable("id") Long invoiceId){
        return invoiceService.getInvoiceDetails(invoiceId);
    }
}
