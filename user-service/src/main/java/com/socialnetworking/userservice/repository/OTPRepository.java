package com.socialnetworking.userservice.repository;

import com.socialnetworking.userservice.model.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OTPRepository extends JpaRepository<OTP, Long> {
    OTP findOTPByOtpAndEmail(String otp, String email);
}
