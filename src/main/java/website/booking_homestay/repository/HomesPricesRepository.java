package website.booking_homestay.repository;

import jakarta.persistence.SqlResultSetMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import website.booking_homestay.DTO.details.HomesPricesDetails;
import website.booking_homestay.DTO.details.HomestayDetails;
import website.booking_homestay.DTO.details.PriceListDetails;
import website.booking_homestay.entity.HomePriceId;
import website.booking_homestay.entity.HomesPrices;
import website.booking_homestay.entity.Homestay;
import website.booking_homestay.entity.PriceList;

import java.util.List;

@Repository
public interface HomesPricesRepository extends JpaRepository<HomesPrices, HomePriceId> {

    @Query("SELECT new website.booking_homestay.DTO.details.HomesPricesDetails(h.homestayId, pl.pricelistId, h.name, h.numPeople, pl.effectiveDate, hp.price, hp.status) " +
            "FROM Homestay h " +
            "JOIN HomesPrices hp ON h.homestayId = hp.homestay.homestayId " +
            "JOIN PriceList pl ON pl.pricelistId = hp.priceList.pricelistId " +
            "WHERE hp.homestay.homestayId = :homestayId")
    List<HomesPricesDetails> findPricesByHomeId(@Param("homestayId") Long homestayId);

//    @Query(nativeQuery = true, name = "HomesPrices.findHomesPricesByPriceId")

//    @Query(value = "SELECT h.homestay_id as homestayId, pl.pricelist_id as pricelistId, h.name, h.num_people as numPeople, pl.effective_date as effectiveDate, hp.price, hp.status " +
//            "FROM homestays h " +
//            "JOIN homestays_prices hp ON h.homestay_id = hp.homestay_id " +
//            "JOIN price_list pl ON pl.pricelist_id = hp.pricelist_id " +
//            "WHERE hp.pricelist_id = :priceId", nativeQuery = true)

    @Query("SELECT new website.booking_homestay.DTO.details.HomesPricesDetails(h.homestayId, pl.pricelistId, h.name, h.numPeople, pl.effectiveDate, hp.price, hp.status) " +
            "FROM Homestay h " +
            "JOIN HomesPrices hp ON h.homestayId = hp.homestay.homestayId " +
            "JOIN PriceList pl ON pl.pricelistId = hp.priceList.pricelistId " +
            "WHERE hp.priceList.pricelistId = :priceId")
    List<HomesPricesDetails> findHomesPricesByPriceId(@Param("priceId") Long priceId);
//    List<HomesPricesDetails> findHomesPricesByPriceId(@Param("priceId") Long priceId);

//    List<HomesPricesDetails> findHomesByPriceId(@Param("priceId") Long priceId);

    @Modifying
    @Query("DELETE FROM HomesPrices hp WHERE hp.homestay.homestayId = :homestayId AND hp.priceList.pricelistId = :priceId")
    void deleteHomePrice(@Param("homestayId") Long homestayId,@Param("priceId") Long priceId);

    @Query("SELECT h FROM Homestay h LEFT JOIN HomesPrices hp On h.homestayId = hp.homestay.homestayId " +
            "AND hp.priceList.pricelistId = :priceId " +
            "WHERE hp.homestay.homestayId IS NULL")
    List<Homestay> findHomesNoBelongPriceId(@Param("priceId") Long priceId);

    List<HomesPrices> findByHomestay_HomestayId(Long homeId);

    HomesPrices findByHomestayAndPriceList(Homestay homestay, PriceList priceList);

    @Query("SELECT hp FROM HomesPrices hp WHERE hp.homestay.homestayId = :homestayId and hp.status = 'PRESENT'")
    HomesPrices findByPricePresent(@Param("homestayId") Long homestayId);

    @Query("SELECT hp FROM HomesPrices hp WHERE hp.homestay.homestayId = :homestayId " +
            "AND hp.priceList.pricelistId = :priceId")
    HomesPrices findHomesPricesById(@Param("homestayId") Long homestayId,@Param("priceId") Long priceId);


}
