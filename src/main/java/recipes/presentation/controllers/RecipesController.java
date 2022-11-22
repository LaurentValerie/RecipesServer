package recipes.presentation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import recipes.business.entities.ID;
import recipes.business.entities.Recipe;
import recipes.business.entities.User;
import recipes.business.services.RecipeService;
import recipes.business.services.UserService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("api/recipe")
public class RecipesController {
    private final RecipeService recipeService;
    private final UserService userService;

    @Autowired
    public RecipesController(RecipeService recipeService, UserService userService) {
        this.recipeService = recipeService;
        this.userService = userService;
    }

    @PostMapping(path = "/new", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ID> postRecipe(@Valid @RequestBody Recipe recipe,
                                         @AuthenticationPrincipal UserDetails details) {
        User user = userService.findByEmail(details.getUsername()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.FORBIDDEN));
        recipe.setOwner(user);
        long id = recipeService.saveOrUpdate(recipe);
        return ResponseEntity.ok(new ID(id));
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateRecipe(@Valid @PathVariable long id,
                                          @Valid @RequestBody Recipe recipe,
                                          @AuthenticationPrincipal UserDetails details) {
        if (isRequestPermitted(details, id)) {
            Recipe DBRecipe = recipeService.getRecipeById(id).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            DBRecipe.setAll(recipe);
            recipeService.saveOrUpdate(DBRecipe);
            return ResponseEntity.noContent().build();
        }
    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable int id) {
        recipeService.getRecipeById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.of(recipeService.getRecipeById(id));
    }


    @GetMapping("/search")
    public ResponseEntity<List<Recipe>> findRecipeByName(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category) {
        if (name != null) {
            List<Recipe> recipes = recipeService.getRecipeByName(name);
            return new ResponseEntity<>(recipes, HttpStatus.OK);
        } else if (category != null) {
            List<Recipe> recipes = recipeService.getRecipeByCategory(category);
            return new ResponseEntity<>(recipes, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeRecipe(@Valid @PathVariable long id,
                                          @AuthenticationPrincipal UserDetails details) {
        if (isRequestPermitted(details, id)) {
            recipeService.getRecipeById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            recipeService.delete(id);
            return ResponseEntity.noContent().build();
        } // If user try to delete someone else's recipe
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    // Sends why validation fail
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    private boolean isRequestPermitted(UserDetails details, long id) {
        if (details.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }
        Long userId = userService.getIdByEmail(details.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));
        Long recipeUserId = recipeService.getOwnerIdById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return Objects.equals(userId, recipeUserId);
    }
}
