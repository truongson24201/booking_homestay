package website.booking_homestay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import website.booking_homestay.entity.Invoice;
import website.booking_homestay.entity.enumreration.EInvoice;
import website.booking_homestay.entity.enumreration.EStatus;

import java.util.Date;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice,Long> {
    @Query("SELECT COUNT(invoice) FROM Invoice invoice WHERE invoice.homestay.homestayId = :homestayId and invoice.status = :status")
    Integer countInvoiceStatus(@Param("homestayId") Long homestayId,@Param("status") EInvoice status);

    @Query("SELECT invoice FROM Invoice invoice WHERE invoice.homestay.branch.branchId = :branchId " +
            "and invoice.status = :status")
    List<Invoice> findAll(@Param("branchId") Long branchId,@Param("status") EInvoice status);

    @Query("SELECT invoice FROM Invoice invoice WHERE invoice.homestay.homestayId = :homestayId")
    List<Invoice> findOfHome(@Param("homestayId") Long homestayId);

    @Query("SELECT invoice FROM Invoice invoice WHERE invoice.status = :status")
    List<Invoice> findOfStatus(@Param("status") EInvoice status);

    @Query("SELECT invoice FROM Invoice invoice WHERE invoice.homestay.homestayId = :homestayId and invoice.status = :status")
    List<Invoice> findHomeAndStatus(@Param("homestayId") Long homestayId,@Param("status") String status);
//
    @Query("SELECT invoice, COUNT(invoice) FROM Invoice invoice WHERE invoice.homestay.homestayId = :homestayId AND invoice.status = :status")
    Object[] findInvoiceStatus(@Param("homestayId") Long homestayId, @Param("status") String status);

    Invoice findByEmailOrPhoneNumber(String email,String phone);

    List<Invoice> findByStatus(EInvoice status);

    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN TRUE ELSE FALSE END FROM Invoice i WHERE i.homestay.homestayId = :homestayId " +
            "and i.checkIn >= :checkIn and i.checkOut <= :checkOut and i.status <> 'CANCEL' ")
    boolean checkInvoicesHomeMatch(@Param("homestayId") Long homestayId,@Param("checkIn") Date checkIn,@Param("checkOut") Date checkOut);

}
