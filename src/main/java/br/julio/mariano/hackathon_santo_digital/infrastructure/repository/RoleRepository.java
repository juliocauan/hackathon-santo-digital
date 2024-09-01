package br.julio.mariano.hackathon_santo_digital.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.julio.mariano.hackathon_santo_digital.domain.model.Role;
import br.julio.mariano.hackathon_santo_digital.infrastructure.repository.specification.RoleSpecification;
import jakarta.persistence.EntityNotFoundException;

public interface RoleRepository extends JpaRepository<Role, Short>, JpaSpecificationExecutor<Role> {

    default Role findByName(String name) {
        Role role = this.findOne(RoleSpecification.nameEquals(name))
            .orElseThrow(() -> new EntityNotFoundException("Role [%s] not found!".formatted(name)));
        return role;
    }

 }