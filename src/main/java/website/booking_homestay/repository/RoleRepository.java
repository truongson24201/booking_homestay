package website.booking_homestay.repository;

import website.booking_homestay.entity.Role;
import website.booking_homestay.entity.enumreration.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(ERole name);
}
