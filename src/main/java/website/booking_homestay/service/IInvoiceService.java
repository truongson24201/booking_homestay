package website.booking_homestay.service;

import org.springframework.http.ResponseEntity;
import website.booking_homestay.DTO.RequestBooking;
import website.booking_homestay.DTO.create.InvoiceCreate;
import website.booking_homestay.DTO.update.InvoiceClientUpdate;
import website.booking_homestay.DTO.update.InvoiceUpdate;

import java.util.Date;

public interface IInvoiceService {


    ResponseEntity<?> getInvoicesNew();

    ResponseEntity<?> getInvoicesPending();

    ResponseEntity<?> getInvoices(String status);

    ResponseEntity<?> searchInvoice(String emailOrPhone);

    ResponseEntity<?> updateInvoice(Long invoiceId, InvoiceUpdate invoiceUpdate);

    ResponseEntity<?> getInvoiceDetailsFull(Long invoiceId);

    ResponseEntity<?> getInfoUpdateInvoice(Long invoiceId);

    ResponseEntity<?> registerHomestay(Long homestayId,InvoiceCreate invoiceCreate);

    ResponseEntity<?> cancelInvoice(Long invoiceId);

    ResponseEntity<?> getAllInvoice();

    ResponseEntity<?> getInvoiceDetails(Long invoiceId);

    ResponseEntity<?> getRegisterHomestay(Long homestayId,RequestBooking booking);

    ResponseEntity<?> getStatus();

    ResponseEntity<?> getInvoiceClientDetails(Long invoiceId);

    ResponseEntity<?> updateInvoiceClient(Long invoiceId, InvoiceClientUpdate clientUpdate);

    ResponseEntity<?> updateInvoicePayment(Long invoiceId);

    ResponseEntity<?> getTotalBooking(Date from, Date to);

    ResponseEntity<?> getTotalActive(Date from, Date to);

    ResponseEntity<?> getTotalCancel(Date from, Date to);

    ResponseEntity<?> getTotalOfYear(int year);

    ResponseEntity<?> getInvoicesDate(String status,Date date);
}
