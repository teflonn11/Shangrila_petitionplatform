package com.example.shangrilapp.service;

import com.example.shangrilapp.model.Petitioner;
import com.example.shangrilapp.repository.PetitionerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final PetitionerRepository petitionerRepository;

    public CustomUserDetailsService(PetitionerRepository petitionerRepository) {
        this.petitionerRepository = petitionerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return petitionerRepository.findByEmail(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private UserDetails createUserDetails(Petitioner petitioner) {
        return org.springframework.security.core.userdetails.User
                .withUsername(petitioner.getEmail())
                .password(petitioner.getPassword())
                .roles("USER")
                .build();
    }
}