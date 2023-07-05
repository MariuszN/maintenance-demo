package pl.nurkowski.maintenance.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.nurkowski.maintenance.model.SecurityUser;
import pl.nurkowski.maintenance.model.User;
import pl.nurkowski.maintenance.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Component
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    public JpaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRoles()));

        return new SecurityUser(user);
    }
}
