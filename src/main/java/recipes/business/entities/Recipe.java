package recipes.business.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "recipes")
public class Recipe {
    @Id
    @ToString.Exclude
    @Column(name = "recipe_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Category is mandatory")
    private String category;

    @UpdateTimestamp
    private LocalDateTime date;

    @NotBlank(message = "Description is mandatory")
    private String description;

    @NotNull(message = "Ingredients can't be null")
    @Size(min = 1, message = "Minimal size should be 1")
    @ElementCollection
    private List<String> ingredients;

    @NotNull(message = "Directions can't be null")
    @Size(min = 1, message = "Minimal size should be 1")
    @ElementCollection
    private List<String> directions;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "user_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User owner;

    @Transactional
    public void setAll(Recipe recipe) {
        setName(recipe.getName());
        setCategory(recipe.getCategory());
        setDate(recipe.getDate());
        setDescription(recipe.getDescription());
        setDirections(recipe.getDirections());
        setIngredients(recipe.getIngredients());
    }
}
