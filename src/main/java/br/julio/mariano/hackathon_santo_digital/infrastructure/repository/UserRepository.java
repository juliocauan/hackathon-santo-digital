package br.julio.mariano.hackathon_santo_digital.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.julio.mariano.hackathon_santo_digital.domain.model.User;
import br.julio.mariano.hackathon_santo_digital.infrastructure.repository.specification.UserSpecification;
import br.julio.mariano.hackathon_santo_digital.util.PasswordUtil;
import jakarta.persistence.EntityExistsException;

public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    default User findByUsername(String username) {
        User user = this.findOne(UserSpecification.usernameEquals(username))
            .orElseThrow(() -> new UsernameNotFoundException("Username [%s] not found!".formatted(username)));
        return user;
    }

    default void register(User user) {
        boolean userExists = this.exists(UserSpecification.usernameEquals(user.getUsername()));
        if(userExists)
            throw new EntityExistsException("Username [%s] is already taken!".formatted(user.getUsername()));
        user.setPassword(PasswordUtil.encode(user.getPassword()));
        this.save(user);
    }
    
}
