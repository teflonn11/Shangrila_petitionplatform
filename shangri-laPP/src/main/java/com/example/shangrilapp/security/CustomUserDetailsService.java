//package com.example.shangrilapp.security;
//
////import com.example.shangrilapp.model.Petitioner;
////import com.example.shangrilapp.repository.PetitionerRepository;
////import org.springframework.security.core.authority.SimpleGrantedAuthority;
////import org.springframework.security.core.userdetails.User;
////import org.springframework.security.core.userdetails.UserDetails;
////import org.springframework.security.core.userdetails.UserDetailsService;
////import org.springframework.security.core.userdetails.UsernameNotFoundException;
////import org.springframework.stereotype.Service;
//
//import com.example.shangrilapp.model.Petitioner;
//import com.example.shangrilapp.repository.PetitionerRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.Collections;
//
//
//@Service
//@RequiredArgsConstructor
//
//public class CustomUserDetailsService implements UserDetailsService {
//    private final PetitionerRepository petitionerRepository;
//    private static final String COMMITTEE_EMAIL = "admin@petition.parliament.sr";
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        if (COMMITTEE_EMAIL.equals(username)) {
//            return createCommitteeUserDetails();
//        }
//
//        Petitioner petitioner = petitionerRepository.findByEmail(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//        return User.builder()
//                .username(petitioner.getEmail())
//                .password(petitioner.getPassword())
//                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_PETITIONER")))
//                .build();
//    }
//
//    private UserDetails createCommitteeUserDetails() {
//        return User.builder()
//                .username(COMMITTEE_EMAIL)
//                .password("$2a$12$LQV1HDo95fWgF0QL.Dp7aOYj8tCnKQ/qPrKwhydu8Z1la.CAqzJ4O") // Encoded "2025%shangrila"
//                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_COMMITTEE")))
//                .build();
//    }
//}
