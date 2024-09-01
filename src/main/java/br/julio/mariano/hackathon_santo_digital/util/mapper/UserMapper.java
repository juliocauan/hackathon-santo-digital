package br.julio.mariano.hackathon_santo_digital.util.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.julio.mariano.hackathon_santo_digital.domain.model.User;
import br.julio.mariano.hackathon_santo_digital.infrastructure.security.model.UserPrincipal;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class UserMapper {

    public static UserDetails domainToUserDetails(User model) {
        UserPrincipal userPrincipal = new UserPrincipal();
        Set<SimpleGrantedAuthority> authorities = model.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());
        userPrincipal.setUsername(model.getUsername());
        userPrincipal.setPassword(model.getPassword());
        userPrincipal.setAuthorities(authorities);
        return userPrincipal;
    }
    
}
