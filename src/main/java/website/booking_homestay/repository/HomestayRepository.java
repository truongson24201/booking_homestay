package website.booking_homestay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import website.booking_homestay.entity.Facilities;
import website.booking_homestay.entity.Homestay;

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

    List<Homestay> findAllByBranch_BranchId(Long branchId);
}
