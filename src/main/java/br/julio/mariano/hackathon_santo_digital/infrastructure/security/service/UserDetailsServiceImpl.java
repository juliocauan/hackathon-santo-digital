package br.julio.mariano.hackathon_santo_digital.infrastructure.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import br.julio.mariano.hackathon_santo_digital.infrastructure.repository.UserRepository;
import br.julio.mariano.hackathon_santo_digital.util.mapper.UserMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        return UserMapper.domainToUserDetails(userRepository.findByUsername(username));
    }
    
}