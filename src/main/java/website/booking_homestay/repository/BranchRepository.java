package website.booking_homestay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import website.booking_homestay.DTO.chart.Ranks;
import website.booking_homestay.DTO.chart.TotalBooking;
import website.booking_homestay.entity.Branch;
import website.booking_homestay.entity.Tourist;
import website.booking_homestay.entity.enumreration.EInvoice;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface BranchRepository extends JpaRepository<Branch,Long> {

    @Query("select br from Branch br where br.branchId =:branchId and br.status = true")
    Optional<Branch> findBranchTrue(@Param("branchId") Long branchId);
    List<Branch> findAllByStatusTrue();
    List<Branch> findAllBy();

    @Query("SELECT b FROM Branch b WHERE b.status = true and b.province.name LIKE %:address% or b.district.name LIKE %:address% or b.ward.name LIKE %:address%")
    List<Branch> findAllByProvince(@Param("address") String address);
    @Query("SELECT b FROM Branch b JOIN b.tourists t WHERE b.status = true and t.name LIKE %:address%")
    List<Branch> findAllByTouristName(@Param("address") String address);

    @Query("SELECT new website.booking_homestay.DTO.chart.TotalBooking(COUNT(i),SUM(i.total)) " +
            "FROM Branch b " +
            "JOIN b.homestays h " +
            "JOIN h.invoices i " +
            "WHERE b.branchId = :branchId " +
            "AND (i.status = 'PAID' or i.status = 'CHECKOUT') and i.checkIn >= :from")
    TotalBooking getTotalActiveManager(@Param("branchId") Long branchId,@Param("from") Date from);

    @Query("SELECT new website.booking_homestay.DTO.chart.TotalBooking(COUNT(i),SUM(i.total)) " +
            "FROM Branch b " +
            "JOIN b.homestays h " +
            "JOIN h.invoices i " +
            "WHERE b.branchId = :branchId " +
            "AND (i.status = 'PAID' or i.status = 'CHECKOUT') and i.checkIn >= :from and i.checkOut <= :to ")
    TotalBooking getTotalActiveManager(@Param("branchId") Long branchId,@Param("from") Date from,@Param("to") Date to);

    @Query("SELECT new website.booking_homestay.DTO.chart.TotalBooking(COUNT(i),SUM(i.total)) " +
            "FROM Branch b " +
            "JOIN b.homestays h " +
            "JOIN h.invoices i " +
            "WHERE b.branchId = :branchId " +
            "AND (i.status <> 'PENDING' and i.status <> 'UNPAID') and i.checkIn >= :from")
    TotalBooking getTotalManager(@Param("branchId") Long branchId,@Param("from") Date from);

    @Query("SELECT new website.booking_homestay.DTO.chart.TotalBooking(COUNT(i),SUM(i.total)) " +
            "FROM Branch b " +
            "JOIN b.homestays h " +
            "JOIN h.invoices i " +
            "WHERE b.branchId = :branchId " +
            "AND (i.status <> 'PENDING' and i.status <> 'UNPAID') and i.checkIn >= :from and i.checkOut <= :to ")
    TotalBooking getTotalManager(@Param("branchId") Long branchId,@Param("from") Date from,@Param("to") Date to);

    @Query("SELECT new website.booking_homestay.DTO.chart.TotalBooking(COUNT(i),SUM(i.total)) " +
            "FROM Branch b " +
            "JOIN b.homestays h " +
            "JOIN h.invoices i " +
            "WHERE i.status = 'CANCEL' and i.checkIn >= :from and i.checkOut <= :to " +
            "And b.branchId = :branchId")
    TotalBooking getTotalCancelManager(@Param("branchId") Long branchId,@Param("from") Date from,@Param("to") Date to);

        @Query("SELECT new website.booking_homestay.DTO.chart.TotalBooking(COUNT(i),SUM(i.total)) " +
            "FROM Branch b " +
            "JOIN b.homestays h " +
            "JOIN h.invoices i " +
            "WHERE i.status = 'CANCEL' and i.checkIn >= :from " +
            "And b.branchId = :branchId")
    TotalBooking getTotalCancelManager(@Param("branchId") Long branchId,@Param("from") Date from);


    // ranks
    @Query("SELECT new website.booking_homestay.DTO.chart.Ranks(b.name,SUM(i.total)) " +
            "FROM Branch b " +
            "JOIN b.homestays h " +
            "JOIN h.invoices i " +
            "WHERE i.status = 'PAID' or i.status = 'CHECKOUT' " +
            "GROUP BY b.name")
    List<Ranks> getRanksAdmin();

//    @Query("SELECT new website.booking_homestay.DTO.chart.Ranks(u.username,SUM(i.total)) " +
//            "FROM User u " +
//            "JOIN u.invoices i " +
//            "WHERE u.branch.branchId = :branchId " +
//            "and i.status = 'PAID' or i.status = 'CHECKOUT' " +
//            "GROUP BY u.accountId, u.username "+
//            "ORDER BY SUM(i.total) DESC")

    @Query("SELECT new website.booking_homestay.DTO.chart.Ranks(u.username,SUM(i.total)) " +
            "FROM Branch b " +
            "JOIN b.homestays h " +
            "JOIN h.invoices i " +
            "JOIN i.user u " +
            "WHERE b.branchId = :branchId " +
            "and (i.status = 'PAID' OR i.status = 'CHECKOUT') " +
            "GROUP BY u.username " +
            "ORDER BY SUM(i.total) DESC")
    List<Ranks> getRanksManager(@Param("branchId") Long branchId);

}
