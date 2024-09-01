package br.julio.mariano.hackathon_santo_digital.infrastructure.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import br.julio.mariano.hackathon_santo_digital.domain.model.User;

public interface UserSpecification {

    static Specification<User> usernameEquals(String usernameEquals){
        return (root, criteriaQuery, criteriaBuilder) -> {
            return criteriaBuilder.like(root.get("username"), usernameEquals);
        };
    }

}
