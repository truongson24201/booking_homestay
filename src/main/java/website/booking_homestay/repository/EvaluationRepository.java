package website.booking_homestay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import website.booking_homestay.entity.Evaluation;
import website.booking_homestay.entity.UserHomeId;

import java.util.List;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, UserHomeId> {
    List<Evaluation> findAllByHomestay_HomestayId(Long homestayId);
}
