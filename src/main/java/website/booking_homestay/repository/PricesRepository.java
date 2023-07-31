package website.booking_homestay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import website.booking_homestay.entity.Branch;
import website.booking_homestay.entity.PriceList;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PricesRepository extends JpaRepository<PriceList,Long> {
    @Query("SELECT p FROM PriceList p WHERE p.branch.branchId = :branchId and p.effectiveDate = :date")
    Optional<PriceList> findByEffectiveDate(@Param("branchId") Long branchId, @Param("date") Date date);
    @Query("SELECT p FROM PriceList p WHERE p.branch.branchId = :branchId")
    List<PriceList> findAllByBranchId(@Param("branchId") Long branchId);

    boolean existsByEffectiveDate(Date date);
}
