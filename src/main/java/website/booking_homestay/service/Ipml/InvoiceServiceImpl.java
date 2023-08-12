package website.booking_homestay.service.Ipml;

import com.sun.jdi.event.ExceptionEvent;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import website.booking_homestay.DTO.InvoiceDTO;
import website.booking_homestay.DTO.RequestBooking;
import website.booking_homestay.DTO.chart.TotalBooking;
import website.booking_homestay.DTO.create.InvoiceCreate;
import website.booking_homestay.DTO.details.InvoiceDetails;
import website.booking_homestay.DTO.update.InvoiceClientUpdate;
import website.booking_homestay.DTO.update.InvoiceUpdate;
import website.booking_homestay.DTO.view.HomestayView;
import website.booking_homestay.DTO.view.InvoiceFull;
import website.booking_homestay.DTO.view.InvoiceView;
import website.booking_homestay.entity.*;
import website.booking_homestay.entity.enumreration.ECardType;
import website.booking_homestay.entity.enumreration.EInvoice;
import website.booking_homestay.entity.enumreration.ERole;
import website.booking_homestay.entity.enumreration.EStatus;
import website.booking_homestay.exception.BaseException;
import website.booking_homestay.repository.BranchRepository;
import website.booking_homestay.repository.HomesPricesRepository;
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
    private final HomesPricesRepository homesPricesRepository;

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
            invoices = invoiceRepository.findAllOfBranch(user.getBranch().getBranchId(),eInvoice);
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
    public ResponseEntity<?> getInvoicesDate(String status, Date date) {
        User user = contextHolder.getUser();
        EInvoice eInvoice = EInvoice.valueOf(status);
        List<Invoice> invoices = new ArrayList<>();
        if (user.getRole().getName().equals(ERole.ADMIN)){
            invoices = invoiceRepository.findOfStatusDate(date,eInvoice);
        }
        else {
            Branch branch = user.getBranch();
            if (branch == null) return ResponseEntity.badRequest().body("You don't have branch!");
            invoices = invoiceRepository.findAllOfBranchDate(user.getBranch().getBranchId(),date,eInvoice);
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
        if (invoice.getStatus().name().equals(EInvoice.CHECKOUT.name()) || invoice.getStatus().name().equals(EInvoice.CANCEL.name())){
            return ResponseEntity.badRequest().body("Can't update invoice (invoice status have not permission)");
        }
        invoice.setIdentityNumber(invoiceUpdate.getIdentityNumber());
        if (invoiceUpdate.getCardType() != null){
            invoice.setCardType(ECardType.valueOf(invoiceUpdate.getCardType()));
        }
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

    public int periodBetweenDays(Date checkIn, Date checkOut){
        LocalDate localDate1 = checkIn.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localDate2 = checkOut.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Period period = Period.between(localDate1, localDate2);
        return period.getDays();
    }

    @Transactional
    @Override
    public ResponseEntity<?> registerHomestay(Long homestayId,InvoiceCreate invoiceCreate) {
        Homestay homestay = homestayRepository.findById(homestayId)
                .orElseThrow(()->new BaseException(HttpStatus.NOT_FOUND, "The homestay you registered for is currently unavailable"));
        if (!homestay.getStatus().name().equals(EStatus.OPEN.name())){
            return ResponseEntity.badRequest().body("The homestay you registered isn't open!");
        }
//        String username = contextHolderService.getUsernameFromContext();
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(()->new BaseException(HttpStatus.BAD_REQUEST, "Something went wrong with your account!"));
        if (invoiceRepository.checkInvoicesHomeMatch(homestayId,invoiceCreate.getCheckIn(),invoiceCreate.getCheckOut())){
            return ResponseEntity.badRequest().body("Invoice already exists!");
        }
        User user = contextHolder.getUser();
        if (!user.getStatus()) return ResponseEntity.badRequest().body("Your account was locked!");
        HomesPrices homesPrices = homesPricesRepository.findByPricePresent(homestay.getHomestayId());
        int day = periodBetweenDays(invoiceCreate.getCheckIn(),invoiceCreate.getCheckOut());
        Invoice invoice = new Invoice(new Date(),invoiceCreate.getCheckIn(),invoiceCreate.getCheckOut(),
                day*homesPrices.getPrice(),invoiceCreate.getFullName(),invoiceCreate.getEmail(),invoiceCreate.getPhoneNumber(),
                EInvoice.UNPAID,user,homestay);
        try {
            invoice = invoiceRepository.save(invoice);
        }catch (Exception e){
            logger.error("Invoice creation failed");
            ResponseEntity.badRequest().body("Invoice creation failed");
        }
        String address = homestay.getBranch().getProvince().getName()+", "+homestay.getBranch().getDistrict().getName()+", "+homestay.getBranch().getWard().getName();
        InvoiceView invoiceView = modelMapper.map(invoice,InvoiceView.class);
        invoiceView.setAddress(address);
        invoiceView.setName(homestay.getName());
        invoiceView.setNumPeople(homestay.getNumPeople());
        return ResponseEntity.ok(invoiceView);
    }

    @Transactional
    @Override
    public ResponseEntity<?> cancelInvoice(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND,"Invoice not found"));
        if (invoice.getStatus().name().equals(EInvoice.CHECKOUT.name())) {
            return ResponseEntity.badRequest().body("Can't cancel the invoice because the invoice has checked out");
        }else if (invoice.getStatus().name().equals(EInvoice.CANCEL.name()) || invoice.getStatus().name().equals(EInvoice.PENDING.name())) {
            return ResponseEntity.badRequest().body("No processing!, invoice canceled or pending!");
        }
        else if (invoice.getStatus().name().equals(EInvoice.PAID.name())){
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
        InvoiceView invoiceView = mapInvoiceToInvoiceView(invoice);
        return ResponseEntity.ok(invoiceView);//"Your cancellation request has been sent successfully. We will respond soon!"
    }

    @Override
    public ResponseEntity<?> getAllInvoice() {
        User user = contextHolder.getUser();
        List<InvoiceView> invoiceViews = new ArrayList<>();
        List<Invoice> invoices = user.getInvoices();
        invoices.forEach(invoice -> {
            InvoiceView invoiceView = mapInvoiceToInvoiceView(invoice);
            invoiceViews.add(invoiceView);
        });
        return ResponseEntity.ok(invoiceViews);
    }

    @Override
    public ResponseEntity<?> getRegisterHomestay(Long homestayId,RequestBooking booking) {
//        LocalDate localDate1 = booking.getCheckIn().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//        LocalDate localDate2 = booking.getCheckOut().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//        Period period = Period.between(localDate1, localDate2);
        int day = periodBetweenDays(booking.getCheckIn(),booking.getCheckOut());
        Homestay homestay = homestayRepository.findById(homestayId).get();
        HomesPrices homesPrices = homesPricesRepository.findByPricePresent(homestay.getHomestayId());
        String address = homestay.getBranch().getProvince().getName()+", "+homestay.getBranch().getDistrict().getName()+", "+homestay.getBranch().getWard().getName();
        InvoiceView invoiceView = new InvoiceView(null,homestay.getName(),homestay.getNumPeople(),
                address,booking.getCheckIn(),booking.getCheckOut(),null,homesPrices.getPrice()*day,"","","","");
        return ResponseEntity.ok(invoiceView);
    }

    @Override
    public ResponseEntity<?> getStatus() {
        List<EInvoice> status = new ArrayList<>();
        status.add(EInvoice.PAID);
        status.add(EInvoice.UNPAID);
        status.add(EInvoice.PENDING);
        return ResponseEntity.ok(status);
    }

    @Override
    public ResponseEntity<?> getInvoiceClientDetails(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND,"Invoice not found"));
        InvoiceView invoiceView = mapInvoiceToInvoiceView(invoice);
        return ResponseEntity.ok(invoiceView);
    }

    @Override
    public ResponseEntity<?> updateInvoiceClient(Long invoiceId, InvoiceClientUpdate clientUpdate) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND,"Invoice not found"));
        if (clientUpdate.getFullName().equals("") || clientUpdate.getEmail().equals("") || clientUpdate.getPhoneNumber().equals("")) {
            return ResponseEntity.badRequest().body("Error, information for update must NOT blank!");
        }
        invoice.setFullName(clientUpdate.getFullName());
        invoice.setEmail(clientUpdate.getEmail());
        invoice.setPhoneNumber(clientUpdate.getPhoneNumber());
        try {
            invoiceRepository.save(invoice);
        }catch (Exception e){
            logger.error("Update invoice failed!");
            return ResponseEntity.badRequest().body("Update invoice failed");
        }
        InvoiceView invoiceView = mapInvoiceToInvoiceView(invoice);
        return ResponseEntity.ok(invoiceView);
    }

    @Override
    public ResponseEntity<?> updateInvoicePayment(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND,"Invoice not found"));
        if (!invoice.getStatus().name().equals(EInvoice.UNPAID.name()))
            return ResponseEntity.badRequest().body("Update invoice failed!");
        invoice.setStatus(EInvoice.PAID);
        try {
            invoiceRepository.save(invoice);
        }catch (Exception e){
            logger.error("Update invoice failed!");
            return ResponseEntity.badRequest().body("Update invoice failed");
        }
        return ResponseEntity.ok("Payment invoice successfully!");
    }

    @Override
    public ResponseEntity<?> getTotalBooking(Date from, Date to) {
        User user = contextHolder.getUser();
        TotalBooking totalBooking = new TotalBooking();
        if (user.getRole().getName().equals(ERole.ADMIN)){
            if (to == null) totalBooking = homestayRepository.getTotalAdmin(from);
            else totalBooking = homestayRepository.getTotalAdmin(from,to);
        }else {
            Branch branch = user.getBranch();
            if (to == null) totalBooking = branchRepository.getTotalManager(branch.getBranchId(),from);
            else totalBooking = branchRepository.getTotalManager(branch.getBranchId(),from,to);
        }
        return ResponseEntity.ok(totalBooking);
    }

    @Override
    public ResponseEntity<?> getTotalActive(Date from, Date to) {
        User user = contextHolder.getUser();
        TotalBooking totalBooking = new TotalBooking();
        if (user.getRole().getName().equals(ERole.ADMIN)){
            if (to == null) totalBooking = homestayRepository.getTotalActiveAdmin(from);
            else totalBooking = homestayRepository.getTotalActiveAdmin(from,to);
        }else {
            Branch branch = user.getBranch();
            if (to == null) totalBooking = branchRepository.getTotalActiveManager(branch.getBranchId(),from);
            else totalBooking = branchRepository.getTotalActiveManager(branch.getBranchId(),from,to);
        }

        return ResponseEntity.ok(totalBooking);
    }

    @Override
    public ResponseEntity<?> getTotalCancel(Date from, Date to) {
        User user = contextHolder.getUser();
        TotalBooking totalBooking = new TotalBooking();
        if (user.getRole().getName().equals(ERole.ADMIN)){
            if (to == null) totalBooking = homestayRepository.getTotalCancelAdmin(from);
            else totalBooking = homestayRepository.getTotalCancelAdmin(from,to);
        }else {
            Branch branch = user.getBranch();
            if (to == null) totalBooking = branchRepository.getTotalCancelManager(branch.getBranchId(),from);
            else totalBooking = branchRepository.getTotalCancelManager(branch.getBranchId(),from,to);
        }

        return ResponseEntity.ok(totalBooking);
    }

    public record TotalOfYear(Long month,Double total){}
    @Override
    public ResponseEntity<?> getTotalOfYear(int year) {
        User user = contextHolder.getUser();
        List<Object[]> results;
        List<TotalOfYear> totalOfYears = new ArrayList<>();
        if (user.getRole().getName().equals(ERole.ADMIN)){
            results = invoiceRepository.getTotalOfYear(year);
            for (Object[] result : results) {
                TotalOfYear totalOfYear = new TotalOfYear(((Integer) result[0]).longValue(),(Double) result[1]);
                totalOfYears.add(totalOfYear);
            }
        }else {

        }
        return ResponseEntity.ok(totalOfYears);
    }

    public InvoiceView mapInvoiceToInvoiceView(Invoice invoice){
        Branch branch = invoice.getHomestay().getBranch();
        Homestay homestay = invoice.getHomestay();
        String address = branch.getProvince().getName()+", "+branch.getDistrict().getName()+", "+branch.getWard().getName();
        InvoiceView invoiceView = modelMapper.map(invoice,InvoiceView.class);
        if (invoice.getUpdateOn() == null) invoiceView.setUpdateOn(invoice.getCreate());
        invoiceView.setAddress(address);
        invoiceView.setName(homestay.getName());
        invoiceView.setNumPeople(homestay.getNumPeople());
        return invoiceView;
    }

}
