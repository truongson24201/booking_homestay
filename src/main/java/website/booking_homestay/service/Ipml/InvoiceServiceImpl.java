package website.booking_homestay.service.Ipml;

import com.sun.jdi.event.ExceptionEvent;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import website.booking_homestay.DTO.HomestayDTO;
import website.booking_homestay.DTO.InvoiceDTO;
import website.booking_homestay.DTO.RegisterHomestay;
import website.booking_homestay.DTO.RequestBooking;
import website.booking_homestay.DTO.details.InvoiceDetails;
import website.booking_homestay.DTO.update.InvoiceUpdate;
import website.booking_homestay.DTO.view.HomestayView;
import website.booking_homestay.DTO.view.InvoiceClient;
import website.booking_homestay.DTO.view.InvoiceFull;
import website.booking_homestay.entity.Branch;
import website.booking_homestay.entity.Homestay;
import website.booking_homestay.entity.Invoice;
import website.booking_homestay.entity.User;
import website.booking_homestay.entity.enumreration.ECardType;
import website.booking_homestay.entity.enumreration.EInvoice;
import website.booking_homestay.entity.enumreration.ERole;
import website.booking_homestay.entity.enumreration.EStatus;
import website.booking_homestay.exception.BaseException;
import website.booking_homestay.repository.BranchRepository;
import website.booking_homestay.repository.HomestayRepository;
import website.booking_homestay.repository.InvoiceRepository;
import website.booking_homestay.service.IBranchService;
import website.booking_homestay.service.IContextHolder;
import website.booking_homestay.service.IInvoiceService;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements IInvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final ModelMapper modelMapper;
    private final IContextHolder contextHolder;
    private final BranchRepository branchRepository;
    private final HomestayRepository homestayRepository;

    private static final Logger logger = LoggerFactory.getLogger(InvoiceServiceImpl.class);


    public record invoiceDTO(Integer amount, String status){}
    @Override
    public ResponseEntity<?> getInvoicesNew() {
        User user = contextHolder.getUser();
        Branch branch =user.getBranch();
        List<Homestay> homestays;
        if (user.getRole().getName().equals(ERole.ADMIN)){
            homestays = homestayRepository.findAll();
        }else {
            homestays = branch.getHomestays();
        }
//        List<Invoice> invoices = new ArrayList<>();
        Integer amount = 0;
        for (Homestay homestay : homestays){
            Integer count = invoiceRepository.countInvoiceStatus(homestay.getHomestayId(),EInvoice.UNPAID);
            amount+=count;
//            Object[] result = invoiceRepository.findInvoiceStatus(homestay.getHomestayId(), EInvoice.UNPAID.name());
//            List<Invoice> invoice = Stream.of(result[0])
//                    .map(obj -> (Invoice) obj)
//                    .collect(Collectors.toList());
//            Integer invoiceCount = (Integer) result[1];
//            invoices.addAll(invoice);
//            count+=invoiceCount;
        }
        invoiceDTO invoiceDTO = new invoiceDTO(amount, EInvoice.UNPAID.name());
        return ResponseEntity.ok(invoiceDTO);
    }

    @Override
    public ResponseEntity<?> getInvoicesPending() {
        User user = contextHolder.getUser();
        Branch branch =user.getBranch();
        List<Homestay> homestays;
        if (user.getRole().getName().equals(ERole.ADMIN)){
            homestays = homestayRepository.findAll();
        }else {
            homestays = branch.getHomestays();
        }
        Integer amount = 0;
        for (Homestay homestay : homestays){
            Integer count = invoiceRepository.countInvoiceStatus(homestay.getHomestayId(),EInvoice.PENDING);
            amount+=count;
        }
        invoiceDTO invoiceDTO = new invoiceDTO(amount, EInvoice.PENDING.name());
        return ResponseEntity.ok(invoiceDTO);
    }

    @Override
    public ResponseEntity<?> getInvoices(String status) {
        User user = contextHolder.getUser();
        EInvoice eInvoice = EInvoice.valueOf(status);
        List<Invoice> invoices = new ArrayList<>();
        if (user.getRole().getName().equals(ERole.ADMIN)){
            invoices = invoiceRepository.findOfStatus(eInvoice);
        }
        else {
            Branch branch = user.getBranch();
            if (branch == null) return ResponseEntity.badRequest().body("You don't have branch!");
            invoices = invoiceRepository.findAll(user.getBranch().getBranchId(),eInvoice);
        }
        List<InvoiceDetails> invoicesDetails = new ArrayList<>();
        invoices.forEach(invoice -> {
            InvoiceDetails detail = modelMapper.map(invoice,InvoiceDetails.class);
            detail.setHomestayId(invoice.getHomestay().getHomestayId());
            detail.setName(invoice.getHomestay().getName());
            invoicesDetails.add(detail);
        });
        return ResponseEntity.ok(invoicesDetails);
    }


    @Override
    public ResponseEntity<?> getInvoiceDetails(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId).get();
        InvoiceDetails invoiceDetail = modelMapper.map(invoice, InvoiceDetails.class);
        invoiceDetail.setHomestayId(invoice.getHomestay().getHomestayId());
        invoiceDetail.setName(invoice.getHomestay().getName());
        return ResponseEntity.ok(invoiceDetail);
    }

    @Override
    public ResponseEntity<?> updateInvoice(Long invoiceId, InvoiceUpdate invoiceUpdate) {
        Invoice invoice = invoiceRepository.findById(invoiceId).get();
        if (invoice.getStatus().equals(EInvoice.EXPIRED) || invoice.getStatus().equals(EInvoice.CANCEL)){
            return ResponseEntity.badRequest().body("Can't update invoice (invoice status have not permission)");
        }
        invoice.setIdentityNumber(invoiceUpdate.getIdentityNumber());
        invoice.setCardType(ECardType.valueOf(invoiceUpdate.getCardType()));
        invoice.setStatus(EInvoice.valueOf(invoiceUpdate.getStatus()));
        invoice.setUpdateBy(contextHolder.getUsernameFromContext());
        invoice.setUpdateOn(new Date());
        try {
            invoice = invoiceRepository.save(invoice);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Update invoice failed!");
        }
        InvoiceDetails details = modelMapper.map(invoice,InvoiceDetails.class);
        details.setHomestayId(invoice.getHomestay().getHomestayId());
        details.setName(invoice.getHomestay().getName());
        return ResponseEntity.ok(details);
    }


    @Override
    public ResponseEntity<?> searchInvoice(String emailOrPhone) {
        Invoice invoice = invoiceRepository.findByEmailOrPhoneNumber(emailOrPhone,emailOrPhone);
        InvoiceFull invoiceFull = modelMapper.map(invoice, InvoiceFull.class);
        HomestayView homestayView = modelMapper.map(invoice.getHomestay(),HomestayView.class);
        invoiceFull.setHomestay(homestayView);
        return ResponseEntity.ok(invoiceFull);
    }

    @Override
    public ResponseEntity<?> getInfoUpdateInvoice(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId).get();
        return ResponseEntity.ok(modelMapper.map(invoice,InvoiceDTO.class));
    }

    @Override
    public ResponseEntity<?> getInvoiceDetailsFull(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId).get();
        InvoiceFull invoiceFull = modelMapper.map(invoice, InvoiceFull.class);
        HomestayView homestayView = modelMapper.map(invoice.getHomestay(),HomestayView.class);
        invoiceFull.setHomestay(homestayView);
        return ResponseEntity.ok(invoiceFull);
    }


    @Override
    public ResponseEntity<?> registerHomestay(RegisterHomestay booking) {
        Homestay homestay = homestayRepository.findById(booking.getHomestayId())
                .orElseThrow(()->new BaseException(HttpStatus.NOT_FOUND, "The homestay you registered for is currently unavailable"));
        if (!homestay.getStatus().name().equals(EStatus.OPEN.name())){
            return ResponseEntity.badRequest().body("The homestay you registered isn't open!");
        }
//        String username = contextHolderService.getUsernameFromContext();
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(()->new BaseException(HttpStatus.BAD_REQUEST, "Something went wrong with your account!"));
        User user = contextHolder.getUser();
        if (!user.getStatus()) return ResponseEntity.badRequest().body("Your account was locked!");
        Invoice invoice = new Invoice(new Date(),booking.getCheckIn(),booking.getCheckOut(),
                booking.getTotal(),booking.getFullName(),booking.getEmail(),booking.getPhoneNumber(),
                EInvoice.UNPAID,user,homestay);
        try {
            invoiceRepository.save(invoice);
        }catch (Exception e){
            logger.error("Invoice creation failed");
            ResponseEntity.badRequest().body("Invoice creation failed");
        }
        return ResponseEntity.ok("Booking successfully!");
    }

    @Override
    public ResponseEntity<?> cancelInvoice(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND,"Invoice not found"));
        if (invoice.getStatus().name().equals(EInvoice.EXPIRED.name())) {
            return ResponseEntity.badRequest().body("Can't cancel the invoice because the invoice has expired");
        }else if (invoice.getStatus().name().equals(EInvoice.PAID.name()) || invoice.getStatus().name().equals(EInvoice.PENDING.name())){
            invoice.setStatus(EInvoice.PENDING);
        }else {
            invoice.setStatus(EInvoice.CANCEL);
        }
        try {
            invoice = invoiceRepository.save(invoice);
        }catch (Exception e){
            logger.error("Cancel invoice failed!");
            return ResponseEntity.badRequest().body("Cancel invoice failed");
        }
        return ResponseEntity.ok(modelMapper.map(invoice,InvoiceDTO.class));//"Your cancellation request has been sent successfully. We will respond soon!"
    }

    @Override
    public ResponseEntity<?> getAllInvoice() {
        User user = contextHolder.getUser();
        List<InvoiceClient> invoiceClients = new ArrayList<>();
        List<Invoice> invoices = user.getInvoices();
        invoices.forEach(invoice -> {
            Long id = invoice.getHomestay().getHomestayId();
            String address = homestayRepository.getAddress(id);
            InvoiceClient invoiceClient = modelMapper.map(invoice, InvoiceClient.class);
            invoiceClient.setAddress(address);
            invoiceClient.setHomestayId(id);
            invoiceClients.add(invoiceClient);
        });
        return ResponseEntity.ok(invoiceClients);
    }

    @Override
    public ResponseEntity<?> getRegisterHomestay(RequestBooking booking) {
        LocalDate localDate1 = booking.getCheckIn().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localDate2 = booking.getCheckOut().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Period period = Period.between(localDate1, localDate2);
        int day = period.getDays();
        Homestay homestay = homestayRepository.findById(booking.getHomestayId()).get();
        String address = homestay.getBranch().getProvince().getName()+", "+homestay.getBranch().getDistrict().getName()+", "+homestay.getBranch().getWard().getName();
        RegisterHomestay registerHomestay = new RegisterHomestay(homestay.getHomestayId(),
                homestay.getName(),address,"",booking.getCheckIn(),booking.getCheckOut(),"","",booking.getNumPeople(),booking.getPrice()*day);
        return ResponseEntity.ok(registerHomestay);
    }

    @Override
    public ResponseEntity<?> getStatus() {
        List<EInvoice> status = new ArrayList<>();
        status.add(EInvoice.PAID);
        status.add(EInvoice.UNPAID);
        status.add(EInvoice.PENDING);
        return ResponseEntity.ok(status);
    }

}
