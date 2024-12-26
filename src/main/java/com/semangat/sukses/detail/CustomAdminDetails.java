package com.semangat.sukses.detail;

import com.semangat.sukses.model.Admin;
import com.semangat.sukses.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("customUserDetails")
public class CustomAdminDetails implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Admin> adminOptional = adminRepository.findByAdminname(username);
        if (adminOptional.isPresent()) {
            return AdminDetail.buildAdmin(adminOptional.get());
        }
        throw new UsernameNotFoundException("Admin not found with username: " + username);
    }
}
