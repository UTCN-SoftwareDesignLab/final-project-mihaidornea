package server.repository;

import server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findById(Long id);
    User findByUsername(String username);

    List<User> findByLatitudeBetweenAndLongitudeBetween(double latitudeMin,
                                                        double latitudeMax,
                                                        double longitudeMin,
                                                        double longitudeMax);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.latitude = :newLatitude, u.longitude = :newLongitude WHERE u.username = :username")
    void updateLocation(@Param("newLatitude") double newLatitude,
                        @Param("newLongitude") double newLongitude,
                        @Param("username") String username);



}
