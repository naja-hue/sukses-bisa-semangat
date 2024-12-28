package com.semangat.sukses.service;

import com.semangat.sukses.detail.AdminDetail;
import com.semangat.sukses.model.LoginRequest;
import com.semangat.sukses.model.Admin;
import com.semangat.sukses.repository.AdminRepository;
import com.semangat.sukses.securityNew.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AdminDetail loadAdminByEmail(String email) {  // Changed method name to reflect email search
        Optional<Admin> adminOptional = adminRepository.findByEmail(email);  // Search by email
        if (adminOptional.isPresent()) {
            return AdminDetail.buildAdmin(adminOptional.get());
        }
        throw new IllegalArgumentException("Admin not found with email: " + email);
    }

    public Map<String, Object> authenticate(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        AdminDetail adminDetails = loadAdminByEmail(email);  // Use the email here

        if (!passwordEncoder.matches(loginRequest.getPassword(), adminDetails.getPassword())) {
            throw new BadCredentialsException("Email or password is incorrect");
        }

        String token = jwtTokenUtil.generateToken(adminDetails);

        Map<String, Object> adminData = new HashMap<>();
        adminData.put("id", adminDetails.getId());
        adminData.put("email", adminDetails.getEmail());
        adminData.put("role", adminDetails.getRole());

        Map<String, Object> response = new HashMap<>();
        response.put("adminData", adminData);  // Ensure consistent naming with the front-end
        response.put("token", token);

        return response;
    }
}