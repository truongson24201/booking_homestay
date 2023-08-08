package website.booking_homestay.controller.user;


import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import website.booking_homestay.DTO.Order;
import website.booking_homestay.DTO.RegisterHomestay;
import website.booking_homestay.DTO.RequestBooking;
import website.booking_homestay.DTO.create.InvoiceCreate;
import website.booking_homestay.DTO.update.InvoiceClientUpdate;
import website.booking_homestay.service.IInvoiceService;
import website.booking_homestay.service.Ipml.PaypalService;

@RestController
@RequestMapping("api/clients/invoices")
@RequiredArgsConstructor
public class InvoicesClientController {
    private final IInvoiceService invoiceService;

    private final PaypalService paypalService;

    public static final String SUCCESS_URL = "pay";
    public static final String CANCEL_URL = "pay/cancel";

    @PostMapping("{id}")
    public ResponseEntity<?> createInvoice(@PathVariable("id") Long homestayId,@RequestBody InvoiceCreate invoiceCreate){
        return invoiceService.registerHomestay(homestayId,invoiceCreate);
    }

    @PostMapping("{id}/form")
    public ResponseEntity<?> getInvoiceRegister(@PathVariable("id") Long homestayId,@RequestBody RequestBooking booking){
        return invoiceService.getRegisterHomestay(homestayId,booking);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> cancelInvoice(@PathVariable("id") Long invoiceId){
        return invoiceService.cancelInvoice(invoiceId);
    }

    @GetMapping("")
    public ResponseEntity<?> getAllInvoice(){
        return invoiceService.getAllInvoice();
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getInvoiceDetails(@PathVariable("id") Long invoiceId){
        return invoiceService.getInvoiceClientDetails(invoiceId);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateInfoInvoice(@PathVariable("id") Long invoiceId, @RequestBody InvoiceClientUpdate clientUpdate){
        return invoiceService.updateInvoiceClient(invoiceId,clientUpdate);
    }

    //localhost:9090/pay
    @PostMapping("/pay")
    public ResponseEntity<String> payment(@RequestBody Order order) {
        try {
            Payment payment = paypalService.createPayment(order.getPrice(), order.getCurrency(), order.getMethod(),
                    order.getIntent(), order.getDescription(), "http://localhost:9090/" + CANCEL_URL,
                    "http://localhost:3000/invoices/" + SUCCESS_URL);

            for(Links link:payment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                    return ResponseEntity.status(HttpStatus.OK).body(link.getHref());
                }
            }

        } catch (PayPalRESTException e) {

            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment creation failed.");
    }

    @GetMapping(value = CANCEL_URL)
    public String cancelPay() {
        return "cancel";
    }

    @GetMapping("pay/success")
    public ResponseEntity<String> successPay(@RequestParam("paymentId") String paymentId, @RequestParam("payerID") String payerId) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                return ResponseEntity.ok(payment.getTransactions().get(0).getDescription());
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        return ResponseEntity.badRequest().body("failed");
    }

    @PutMapping("{id}/payment")
    public ResponseEntity<?> updateInvoicePayment(@PathVariable("id") Long invoiceId){
        return invoiceService.updateInvoicePayment(invoiceId);
    }
}
