package website.booking_homestay.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import website.booking_homestay.DTO.chart.Ranks;
import website.booking_homestay.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    @Query("SELECT user FROM User user WHERE user.branch.branchId = :branchId")
    List<User> findAllByBranchId(@Param("branchId") Long branchId);
    List<User> findAllByRole_Name(String role);

    @Query("SELECT user FROM User user WHERE user.role.name = 'MANAGER' and user.branch is null ")
    List<User> findAllByBranchIsNull();

    List<User> findAllByRole_Id(Long id);


//    @Query(value = "SELECT CASE WHEN EXISTS "
//            + "(SELECT username FROM User WHERE username = :username) "
//            + "OR EXISTS "
//            + "(SELECT username FROM AdminManager WHERE username = :username) "
//            + "THEN true ELSE false END from User")
//    Boolean checkUsernameExistence(@Param("username") String username);
}
