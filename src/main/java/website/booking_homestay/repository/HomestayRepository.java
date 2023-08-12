package website.booking_homestay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import website.booking_homestay.DTO.chart.TotalBooking;
import website.booking_homestay.entity.Facilities;
import website.booking_homestay.entity.Homestay;

import java.util.Date;
import java.util.List;

@Repository
public interface HomestayRepository extends JpaRepository<Homestay,Long> {
    @Query("SELECT CONCAT(home.branch.province.name,' ',home.branch.district.name,' ',home.branch.ward.name) FROM Homestay home WHERE home.homestayId =: homestayId")
    String getAddress(@Param("homestayId") Long homestayId);

//    @Query("SELECT h FROM Homestay h WHERE NOT EXISTS (SELECT hp.homestay FROM HomesPrices hp WHERE hp.priceList.priceId = :priceId)")
//    List<Homestay> findAllHomesNotInPrice(Long priceId);

    @Query("SELECT f FROM Facilities f WHERE f.facilityId NOT IN (SELECT f2.facilityId FROM Facilities f2 JOIN f2.homestays h WHERE h.homestayId = :homestayId)")
    List<Facilities> findFacilitiesNotBelongHome(@Param("homestayId") Long homestayId);

    @Query("SELECT h FROM Homestay h " +
            "JOIN h.facilities f " +
            "WHERE h.branch.branchId = :branchId " +
            "AND f.facilityId = :facilityId")
    List<Homestay> findHomestaysByBranchIdAndFacilityId(@Param("branchId") Long branchId, @Param("facilityId") Long facilityId);


    List<Homestay> findAllByBranchIsNull();

    @Query("SELECT h FROM Homestay h WHERE h.branch.branchId =:branchId and h.numPeople >= :numPeople and h.status = 'OPEN'")
    List<Homestay> findAllByNumPeople(@Param("branchId") Long branchId,@Param("numPeople") Integer numPeople);

    @Query("SELECT h FROM Homestay h WHERE h.branch.branchId =:branchId " +
            "and h.status = 'OPEN' and not exists " +
            "(SELECT i FROM Invoice i WHERE i.homestay = h AND i.status <> 'CANCEL' " +
            "AND (i.checkIn >= :date and i.checkOut <= :date))")
    List<Homestay> findAllEmpty(@Param("branchId") Long branchId,@Param("date") Date date);

    List<Homestay> findAllByBranch_BranchId(Long branchId);

    @Query("SELECT h FROM Homestay h WHERE h.branch.branchId =:branchId and h.flag = true")
    List<Homestay> findAllByBranchAndTrue(@Param("branchId") Long branchId);

    List<Homestay> findAllByFlagTrue();

    @Query("SELECT h FROM Homestay h " +
            "JOIN h.branch b " +
            "WHERE b.branchId = :branchId and h.status = 'OPEN' and h.numPeople >= :numPeople " +
            "AND h.homestayId NOT IN (" +
            "   SELECT i.homestay.homestayId FROM h.invoices i " +
            "   WHERE i.checkIn < :checkOut " +
            "   AND i.checkOut > :checkIn" +
            ")")
    List<Homestay> findHomestaysClient(@Param("numPeople") Integer numPeople,
                                       @Param("branchId") Long branchId,
                                       @Param("checkIn") Date checkIn,
                                       @Param("checkOut") Date checkOut);

//    @Query("SELECT h FROM Homestay h " +
//            "WHERE h.status = 'OPEN' AND " +
//            "      (h.branch.branchId, h.homestayId) IN " +
//            "      (SELECT h1.branch.branchId, MIN(h1.homestayId) " +
//            "       FROM Homestay h1 " +
//            "       WHERE h1.status = 'OPEN' " +
//            "       GROUP BY h1.branch.branchId)")
    @Query("SELECT h FROM Homestay h WHERE h.homestayId = (SELECT MIN(h2.homestayId) " +
            "FROM Homestay h2 WHERE h2.branch = h.branch AND h2.status = 'OPEN')")
    List<Homestay> findHomestayFromEachBranch();

    @Query("SELECT h FROM Homestay h " +
            "WHERE h.branch.branchId = :branchId " +
            "AND h.homestayId NOT IN " +
            "(SELECT hp.homestay.homestayId FROM HomesPrices hp WHERE hp.priceList.pricelistId = :priceListId)")
    List<Homestay> findHomesNoBelongPriceId(@Param("branchId") Long branchId, @Param("priceListId") Long priceListId);



    @Query("SELECT new website.booking_homestay.DTO.chart.TotalBooking(COUNT(i),SUM(i.total)) " +
            "FROM Homestay h " +
            "JOIN h.invoices i " +
            "WHERE i.checkIn >= :from and (i.status <> 'PENDING' and i.status <> 'UNPAID') ")
    TotalBooking getTotalAdmin(@Param("from") Date from);

    @Query("SELECT new website.booking_homestay.DTO.chart.TotalBooking(COUNT(i),SUM(i.total)) " +
            "FROM Homestay h " +
            "JOIN h.invoices i " +
            "WHERE i.checkIn >= :from and i.checkOut <= :to and (i.status <> 'PENDING' and i.status <> 'UNPAID') ")
    TotalBooking getTotalAdmin(@Param("from") Date from,@Param("to") Date to);

    @Query("SELECT new website.booking_homestay.DTO.chart.TotalBooking(COUNT(i),SUM(i.total)) " +
            "FROM Homestay h " +
            "JOIN h.invoices i " +
            "WHERE i.checkIn >= :from and (i.status = 'PAID' or i.status = 'CHECKOUT') ")
    TotalBooking getTotalActiveAdmin(@Param("from") Date from);

    @Query("SELECT new website.booking_homestay.DTO.chart.TotalBooking(COUNT(i),SUM(i.total)) " +
            "FROM Homestay h " +
            "JOIN h.invoices i " +
            "WHERE i.checkIn >= :from and i.checkOut <= :to and (i.status = 'PAID' or i.status = 'CHECKOUT') ")
    TotalBooking getTotalActiveAdmin(@Param("from") Date from,@Param("to") Date to);

    @Query("SELECT new website.booking_homestay.DTO.chart.TotalBooking(COUNT(i),SUM(i.total)) " +
            "FROM Homestay h " +
            "JOIN h.invoices i " +
            "WHERE i.status = 'CANCEL' and i.checkIn >= :from ")
    TotalBooking getTotalCancelAdmin(@Param("from") Date from);

    @Query("SELECT new website.booking_homestay.DTO.chart.TotalBooking(COUNT(i),SUM(i.total)) " +
            "FROM Homestay h " +
            "JOIN h.invoices i " +
            "WHERE i.status = 'CANCEL' and i.checkIn >= :from and i.checkOut <= :to ")
    TotalBooking getTotalCancelAdmin(@Param("from") Date from,@Param("to") Date to);

    @Query(value = "SELECT MONTH(check_out) as month, SUM(total) as total FROM invoices WHERE YEAR(check_out) = :year AND status = 'CHECKOUT' GROUP BY MONTH(check_out)", nativeQuery = true)
    List<Object[]> getTotalOfYear(int year);

}
