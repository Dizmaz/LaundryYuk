package laundryyuk.laundry_yuk.repos;

import laundryyuk.laundry_yuk.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AdminRepository extends JpaRepository<Admin, Long> {
}
