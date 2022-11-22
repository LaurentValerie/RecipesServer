package recipes.business.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipes.business.entities.Recipe;
import recipes.business.entities.User;
import recipes.persistence.RecipeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public long saveOrUpdate(Recipe recipe) {
        Recipe savedRecipe = recipeRepository.save(recipe);
        return savedRecipe.getId();
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> allRecipes = new ArrayList<>();
        recipeRepository.findAll().forEach(allRecipes::add);
        return allRecipes;
    }

    public Optional<Recipe> getRecipeById(long id) {
        if (recipeRepository.findById(id).isPresent()) {
            return Optional.of(recipeRepository.findById(id).get());
        }
        return Optional.empty();
    }

    public List<Recipe> getRecipeByName(String name) {
        return recipeRepository.findByNameContainingIgnoreCaseOrderByDateDesc(name);
    }

    public List<Recipe> getRecipeByCategory(String category) {
        return recipeRepository.findByCategoryIgnoreCaseOrderByDateDesc(category);
    }

    public Optional<Long> getOwnerIdById(Long id) {
        if (recipeRepository.findById(id).isPresent()) {
            User owner = recipeRepository.findById(id).get().getOwner();
            return Optional.of(owner.getId());
        }
        return Optional.empty();
    }

    public void delete(long id) {
        recipeRepository.deleteById(id);
    }
}
