package website.booking_homestay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import website.booking_homestay.entity.Homestay;
import website.booking_homestay.entity.Tourist;

import java.util.List;

@Repository
public interface TouristRepository extends JpaRepository<Tourist,Long> {
    @Query("SELECT t FROM Tourist t WHERE t.touristId NOT IN (SELECT t2.touristId FROM Tourist t2 JOIN t2.branches b WHERE b.branchId = :branchId)")
    List<Tourist> findTouristsNotBelongBranch(@Param("branchId") Long branchId);

}
