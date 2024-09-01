package br.julio.mariano.hackathon_santo_digital.infrastructure.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import br.julio.mariano.hackathon_santo_digital.domain.model.Role;

public interface RoleSpecification {

    static Specification<Role> nameEquals(String nameEquals){
        return (root, criteriaQuery, criteriaBuilder) -> {
            return criteriaBuilder.like(root.get("name"), nameEquals);
        };
    }

}
