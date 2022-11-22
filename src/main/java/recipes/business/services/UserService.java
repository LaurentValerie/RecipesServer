package recipes.business.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipes.business.entities.User;
import recipes.persistence.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public long saveOrUpdate(User user) {
        User savedRecipe = userRepository.save(user);
        return savedRecipe.getId();
    }

    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>();
        userRepository.findAll().forEach(allUsers::add);
        return allUsers;
    }

    public Optional<User> findByEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            return Optional.of(userRepository.findByEmail(email).get());
        }
        return Optional.empty();
    }

    public Optional<User> findById(long id) {
        if (userRepository.findById(id).isPresent()) {
            return Optional.of(userRepository.findById(id).get());
        }
        return Optional.empty();
    }

    public Optional<Long> getIdByEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            return Optional.of(userRepository.findByEmail(email).get().getId());
        }
        return Optional.empty();
    }
    public boolean isUserInBase(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
