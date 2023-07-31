package website.booking_homestay.service;

import org.springframework.http.ResponseEntity;
import website.booking_homestay.DTO.InvoiceDTO;
import website.booking_homestay.DTO.RegisterHomestay;
import website.booking_homestay.DTO.RequestBooking;
import website.booking_homestay.DTO.update.InvoiceUpdate;

public interface IInvoiceService {


    ResponseEntity<?> getInvoicesNew();

    ResponseEntity<?> getInvoicesPending();

    ResponseEntity<?> getInvoices(String status);

    ResponseEntity<?> searchInvoice(String emailOrPhone);

    ResponseEntity<?> updateInvoice(Long invoiceId, InvoiceUpdate invoiceUpdate);

    ResponseEntity<?> getInvoiceDetailsFull(Long invoiceId);

    ResponseEntity<?> getInfoUpdateInvoice(Long invoiceId);

    ResponseEntity<?> registerHomestay(RegisterHomestay booking);

    ResponseEntity<?> cancelInvoice(Long invoiceId);

    ResponseEntity<?> getAllInvoice();

    ResponseEntity<?> getInvoiceDetails(Long invoiceId);

    ResponseEntity<?> getRegisterHomestay(RequestBooking booking);

    ResponseEntity<?> getStatus();
}
