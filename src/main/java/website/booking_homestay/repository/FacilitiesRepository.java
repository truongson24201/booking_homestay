package website.booking_homestay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import website.booking_homestay.entity.Facilities;

import java.util.List;

@Repository
public interface FacilitiesRepository extends JpaRepository<Facilities,Long> {
    @Query("SELECT DISTINCT f FROM Facilities f " +
            "JOIN f.homestays h " +
            "JOIN h.branch b " +
            "WHERE b.branchId = :branchId")
    List<Facilities> findFacilitiesByBranchId(@Param("branchId") Long branchId);

//    @Query("SELECT f FROM Facilities f " +
//            "WHERE f.facilityId IN (" +
//            "    SELECT DISTINCT f2.facilityId FROM Facilities f2 " +
//            "    JOIN f2.homestays h " +
//            "    WHERE h.branch.branchId = :branchId" +
//            ") " +
//            "AND f.facilityId NOT IN (" +
//            "    SELECT DISTINCT f3.facilityId FROM Facilities f3 " +
//            "    JOIN f3.homestays h2 " +
//            "    WHERE h2.homestayId = :homestayId" +
//            ")")
//    List<Facilities> findFacilitiesNotBelongHome(@Param("branchId") Long branchId, @Param("homestayId") Long homestayId);

    @Query("SELECT f FROM Facilities f " +
            "WHERE f.facilityId NOT IN (" +
            "    SELECT DISTINCT f3.facilityId FROM Facilities f3 " +
            "    JOIN f3.homestays h2 " +
            "    WHERE h2.homestayId = :homestayId" +
            ")")
    List<Facilities> findFacilitiesNotBelongHome(@Param("homestayId") Long homestayId);
}
