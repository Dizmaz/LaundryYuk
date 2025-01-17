package laundryyuk.laundry_yuk.repos;

import laundryyuk.laundry_yuk.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByEmail(String email);
}
