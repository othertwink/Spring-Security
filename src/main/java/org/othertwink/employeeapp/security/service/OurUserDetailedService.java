package org.othertwink.employeeapp.security.service;

import org.othertwink.employeeapp.model.entity.User;
import org.othertwink.employeeapp.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class OurUserDetailedService implements UserDetailsService {

    private final UserRepo userRepo;

    @Autowired
    public OurUserDetailedService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isAccountNonLocked(),
                true,
                true,
                true,
                user.getAuthorities()
        );
    }

    public void increaseFailedLoginAttempts(User user) {
        int attempts = user.getFailedLoginAttempts() + 1;
        user.setFailedLoginAttempts(attempts);
        if (attempts >= 5) {
            user.setAccountNonLocked(false); // БАН!!!
        }
        userRepo.save(user);
    }

    public void resetFailedLoginAttempts(User user) {
        user.setFailedLoginAttempts(0);
        user.setAccountNonLocked(true);
        userRepo.save(user);
    }


}