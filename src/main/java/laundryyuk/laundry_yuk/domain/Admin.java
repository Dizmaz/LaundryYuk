package laundryyuk.laundry_yuk.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "\"Admin\"")
@Getter
@Setter
public class Admin extends User {
}
