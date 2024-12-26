package com.semangat.sukses.service;


import com.semangat.sukses.model.Admin;
import com.semangat.sukses.DTO.PasswordDTO;


import java.util.List;
import java.util.Map;

public interface AdminService {

    Admin registerAdmin(Admin admin);

    Admin getById(Long id);

    List<Admin> getAll();

    Admin edit(Long id, Admin admin);

    Admin putPasswordAdmin(PasswordDTO passwordDTO, Long id);

    Map<String, Boolean> delete(Long id);
}