package recipes.business.entities;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;


@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;

    @Email(regexp=".+@.+\\..+", message="Please provide a valid email address")
    @NotBlank(message = "Please provide a email address")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min=8, message = "Password length must at least 8")
    private String password;

    @ToString.Exclude
    @ElementCollection(fetch = FetchType.EAGER)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    // All users have only user role by default
    private List<GrantedAuthority> roles = List.of(new SimpleGrantedAuthority("ROLE_USER"));

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    private List<Recipe> ownedRecipes = new ArrayList<>();
}
