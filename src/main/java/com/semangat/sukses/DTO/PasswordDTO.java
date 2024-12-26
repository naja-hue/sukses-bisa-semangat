package com.semangat.sukses.DTO;

public class PasswordDTO {
    private String old_password;  // Password lama
    private String new_password;  // Password baru
    private String confirm_new_password;  // Konfirmasi password baru

    // Constructor default
    public PasswordDTO() {}

    // Getter dan setter untuk old_password
    public String getOld_password() {
        return old_password;
    }

    public void setOld_password(String old_password) {
        this.old_password = old_password;
    }

    // Getter dan setter untuk new_password
    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }

    // Getter dan setter untuk confirm_new_password
    public String getConfirm_new_password() {
        return confirm_new_password;
    }

    public void setConfirm_new_password(String confirm_new_password) {
        this.confirm_new_password = confirm_new_password;
    }
}
