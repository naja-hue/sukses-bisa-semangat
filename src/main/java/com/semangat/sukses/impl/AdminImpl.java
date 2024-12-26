package com.semangat.sukses.impl;

import com.semangat.sukses.DTO.PasswordDTO;
import com.semangat.sukses.exception.BadRequestException;
import com.semangat.sukses.exception.NotFoundException;
import com.semangat.sukses.model.Admin;
import com.semangat.sukses.repository.AdminRepository;
import com.semangat.sukses.service.AdminService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AdminImpl implements AdminService {

    private final AdminRepository adminRepository;

    @Autowired
    PasswordEncoder encoder;

    public AdminImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public Admin registerAdmin(@NotNull Admin admin) {
        admin.setPassword(encoder.encode(admin.getPassword()));
        return adminRepository.save(admin);
    }

    @Override
    public Admin getById(Long id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Id Admin Tidak Ditemukan"));
    }

    @Override
    public List<Admin> getAll() {
        return adminRepository.findAll();
    }

    @Override
    public Admin edit(Long id, @NotNull Admin admin) {
        Admin existingAdmin = adminRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Admin Tidak Ditemukan"));

        existingAdmin.setAdminname(admin.getAdminname());
        existingAdmin.setEmail(admin.getEmail());
        return adminRepository.save(existingAdmin);
    }

    @Override
    public Admin putPasswordAdmin(@NotNull PasswordDTO passwordDTO, Long id) {
        Admin update = adminRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Id Tidak Ditemukan"));

        boolean isOldPasswordCorrect = encoder.matches(passwordDTO.getOld_password(), update.getPassword());

        if (!isOldPasswordCorrect) {
            throw new NotFoundException("Password Lama Tidak Sesuai");
        }

        if (passwordDTO.getNew_password().equals(passwordDTO.getConfirm_new_password())) {
            update.setPassword(encoder.encode(passwordDTO.getNew_password()));
            return adminRepository.save(update);
        } else {
            throw new BadRequestException("Password Tidak Sesuai");
        }
    }

    @Override
    public Map<String, Boolean> delete(Long id) {
        Map<String, Boolean> response = new HashMap<>();
        try {
            adminRepository.deleteById(id);
            response.put("Deleted", Boolean.TRUE);
        } catch (Exception e) {
            response.put("Deleted", Boolean.FALSE);
        }
        return response;
    }
}