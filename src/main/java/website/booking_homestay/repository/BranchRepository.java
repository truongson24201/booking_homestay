package website.booking_homestay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import website.booking_homestay.entity.Branch;
import website.booking_homestay.entity.Tourist;

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
    @Query("SELECT b FROM Branch b JOIN b.tourists t WHERE b.status = true and t.name = :address")
    List<Branch> findAllByTouristName(@Param("address") String address);

}
