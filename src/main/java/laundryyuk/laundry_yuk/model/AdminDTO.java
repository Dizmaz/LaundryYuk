package laundryyuk.laundry_yuk.model;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AdminDTO {

    private Long id;

    @Size(max = 255)
    private String email;

    @Size(max = 255)
    private String nama;

    private Role role;

    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
