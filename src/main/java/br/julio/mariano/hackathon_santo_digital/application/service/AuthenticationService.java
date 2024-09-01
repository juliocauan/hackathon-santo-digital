package br.julio.mariano.hackathon_santo_digital.application.service;

import java.util.HashSet;
import java.util.Set;

import org.openapitools.model.RoleEnum;
import org.openapitools.model.UserData;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import br.julio.mariano.hackathon_santo_digital.domain.model.Role;
import br.julio.mariano.hackathon_santo_digital.domain.model.User;
import br.julio.mariano.hackathon_santo_digital.infrastructure.repository.RoleRepository;
import br.julio.mariano.hackathon_santo_digital.infrastructure.repository.UserRepository;
import br.julio.mariano.hackathon_santo_digital.infrastructure.security.jwt.JwtProvider;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public final class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserData authenticate(String username, String password) {
        UserData userData = JwtProvider.authenticate(username, password, authenticationManager);
        return userData;
    }

    public void registerUser(String username, String password, RoleEnum roleEnum) {
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(roleEnum.getValue()));
        User user = new User(username, password, roles);
        userRepository.register(user);
    }

}
