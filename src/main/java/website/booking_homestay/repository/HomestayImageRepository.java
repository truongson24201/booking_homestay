package website.booking_homestay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import website.booking_homestay.entity.Homestay;
import website.booking_homestay.entity.Images;

import java.util.List;

@Repository
public interface HomestayImageRepository extends JpaRepository<Images,Long> {
}
