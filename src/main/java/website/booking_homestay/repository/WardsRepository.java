package website.booking_homestay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import website.booking_homestay.entity.Wards;

@Repository
public interface WardsRepository extends JpaRepository<Wards,Long> {
}
