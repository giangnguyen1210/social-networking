package com.socialnetworking.userservice.service.impl;
import com.socialnetworking.shared_service.dto.response.BaseResponse;
import com.socialnetworking.userservice.config.JwtService;
import com.socialnetworking.userservice.dto.request.LoginRequest;
import com.socialnetworking.userservice.dto.request.OTPVerificationRequest;
import com.socialnetworking.userservice.dto.request.RegisterRequest;
import com.socialnetworking.userservice.dto.request.UserRequest;
import com.socialnetworking.userservice.dto.response.AuthResponse;
import com.socialnetworking.userservice.model.OTP;
import com.socialnetworking.userservice.model.Role;
import com.socialnetworking.userservice.model.User;
import com.socialnetworking.userservice.model.UserStatus;
import com.socialnetworking.userservice.repository.OTPRepository;
import com.socialnetworking.userservice.repository.RoleRepository;
import com.socialnetworking.userservice.repository.UserRepository;
import com.socialnetworking.userservice.repository.UserStatusRepository;
import com.socialnetworking.userservice.service.AuthService;
import com.socialnetworking.userservice.support.SupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtGenerator;

    @Autowired
    private SupportService supportService;
    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private UserStatusRepository userStatusRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private OTPRepository otpRepository;
    private static final String SECRET_KEY = "sign-in"; // Thay thế bằng khoá bí mật thực tế của bạn
    @Override
    public AuthResponse login(LoginRequest request) {
        AuthResponse authResponse = new AuthResponse("");
        UserStatus inactiveStatus = userStatusRepository.findById(3L).orElse(null);
        if(request.getUsername()==null || request.getUsername().isEmpty()){
            authResponse.setErrorCode(HttpStatus.BAD_REQUEST.name());
            authResponse.setErrorDesc("Bạn cần phải nhập email hoặc username để đăng nhập");
            return authResponse;
        }else if(request.getPassword()==null || request.getPassword().isEmpty()){
            authResponse.setErrorCode(HttpStatus.BAD_REQUEST.name());
            authResponse.setErrorDesc("Mật khẩu không được rỗng");
            return authResponse;
        }
        User user = userRepository.findUserByUsername(request.getUsername());
        System.out.println(user);

        if(user==null){
            authResponse.setErrorCode(HttpStatus.NOT_FOUND.name());
            authResponse.setErrorDesc("Không tìm thấy tài khoản");
        }else {
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
                );
                if(user.getStatus()==inactiveStatus){
                    authResponse.setErrorCode(HttpStatus.BAD_REQUEST.name());
                    authResponse.setErrorDesc("Tài khoản chưa được active, bạn cần phải active bằng cách nhập OTP");
                }else {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    authResponse.setErrorCode(HttpStatus.OK.name());
                    String token = jwtGenerator.generateToken(request.getUsername());
                    authResponse.setAccessToken(token);
                    authResponse.setTokenType("Bearer "+token);
                }

            } else {
                authResponse.setErrorCode(HttpStatus.BAD_REQUEST.name());
                authResponse.setErrorDesc("Sai mật khẩu");
            }
        }
        return authResponse;
    }

    @Override
    public String generateToken(String username) {
        return jwtGenerator.generateToken(username);
    }
    @Override
    public BaseResponse register(RegisterRequest request) {
        User user = new User();
        BaseResponse baseResponse = new BaseResponse();
        User accountUsername = userRepository.findUserByUsername(request.getUsername());
        User accountEmail = userRepository.findByEmail(request.getEmail());
        UserStatus inactiveStatus = userStatusRepository.findById(3L).orElse(null);
        Role roleUser = roleRepository.findById(2L).orElse(null);

        if(request.getUsername()==null || request.getUsername().isEmpty()){
            baseResponse.setErrorCode(HttpStatus.BAD_REQUEST.name());
            baseResponse.setErrorDesc("Tên người dùng không được rỗng");
            return baseResponse;
        }else if(request.getName()==null || request.getName()==""){
            baseResponse.setErrorCode(HttpStatus.BAD_REQUEST.name());
            baseResponse.setErrorDesc("Họ tên không được rỗng");
            return baseResponse;
        }else if(request.getPassword()==null || request.getPassword()==""){
            baseResponse.setErrorCode(HttpStatus.BAD_REQUEST.name());
            baseResponse.setErrorDesc("Mật khẩu tên không được rỗng");
            return baseResponse;
        }
        if(accountUsername!=null){
            baseResponse.setErrorCode(HttpStatus.BAD_REQUEST.name());
            baseResponse.setErrorDesc("Tên người dùng đã tồn tại");
            return baseResponse;
        }else if(accountEmail!=null){
            baseResponse.setErrorCode(HttpStatus.BAD_REQUEST.name());
            baseResponse.setErrorDesc("Email đã tồn tại");
            return baseResponse;
        }
        else{
            String id = "user-";
            int getNextId = 0;
            int totalRecords = Math.toIntExact(userRepository.count());
            if(totalRecords>0){
                Optional<User> userLatest = userRepository.findFirstByOrderByUserIdDesc();
                getNextId = Integer.parseInt(userLatest.get().getUserId().substring(userLatest.get().getUserId().length() - 4))+1;
            }
            String pad = supportService.padLeft(String.valueOf(getNextId), 4, "0");
            request.setUserId((id+pad).trim());
            user.setUserId((id+pad).trim());
            user.setUsername(request.getUsername());
            Date date = new Date();
            user.setCreatedDate(date);
            user.setStatus(inactiveStatus);
            user.setRole(roleUser);
            user.setName(request.getName());
            user.setBirthday(request.getBirthday());
            String hashedPassword = passwordEncoder.encode(request.getPassword());
            user.setPassword(hashedPassword);
            user.setEmail(request.getEmail());
            userRepository.save(user);
            baseResponse.setData(request);
            baseResponse.setErrorCode(HttpStatus.OK.name());
        }
        return baseResponse;
    }
    @Override
    public BaseResponse sendEmailOTP(RegisterRequest request) throws NoSuchAlgorithmException {
        BaseResponse baseResponse = new BaseResponse();
        // check email exist:
        User checkEmailExist = userRepository.findByEmail(request.getEmail());
        if(checkEmailExist==null){
            baseResponse.setErrorCode(HttpStatus.NOT_FOUND.name());
            baseResponse.setErrorDesc("Email không tồn tại");
            return baseResponse;
        }else{
            String otp = generateOTP();
            LocalDateTime expiryTime = LocalDateTime.now().plusSeconds(30); // Thời gian hết hạn: 30 giây
            long currentCounter = System.currentTimeMillis() / 30000;
            OTP otpEntity = new OTP();
            otpEntity.setOtp(otp);
            otpEntity.setExpiryTime(expiryTime);
            otpEntity.setEmail(checkEmailExist.getEmail());
            otpEntity.setCounter(currentCounter);

            otpRepository.save(otpEntity);
            System.out.println(otp);
            sendEmail(request.getEmail(), "OTP ", "Your OTP is: " + otp + " will expire in "+30+ "s");
            baseResponse.setErrorCode(HttpStatus.OK.name());
            baseResponse.setData("Gửi email thành công!, mời bạn nhập mã OTP");
            return baseResponse;
        }
    }
    @Override
    public BaseResponse verifyEmail(OTPVerificationRequest request) {
        BaseResponse baseResponse = new BaseResponse();
        UserStatus activeStatus = userStatusRepository.findById(1L).orElse(null);
        if(validateOtp(request.getOtp(), request.getEmail())){
            userRepository.updateUserStatusByEmail(request.getEmail(), activeStatus);
            baseResponse.setErrorCode(HttpStatus.OK.name());
            baseResponse.setErrorDesc("Tài khoản của bạn đã active");
            return baseResponse;
        }
        baseResponse.setErrorCode(HttpStatus.BAD_REQUEST.name());
        baseResponse.setErrorDesc("OTP đã hết hạn");
        return baseResponse;
    }

    @Override
    public BaseResponse resetPassword(UserRequest request) {
        BaseResponse baseResponse = new BaseResponse();

        return null;
    }

    public static String generateOTP() throws NoSuchAlgorithmException {
        byte[] secretKeyBytes = SECRET_KEY.getBytes();
        long counter = System.currentTimeMillis() / 120000; // Mỗi 30 giây tăng một lần

        byte[] counterBytes = new byte[8];
        for (int i = 7; i >= 0; i--) {
            counterBytes[i] = (byte) counter;
            counter >>= 8;
        }

        byte[] data = new byte[secretKeyBytes.length + counterBytes.length];
        System.arraycopy(secretKeyBytes, 0, data, 0, secretKeyBytes.length);
        System.arraycopy(counterBytes, 0, data, secretKeyBytes.length, counterBytes.length);

        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        byte[] hash = digest.digest(data);

        // Chỉ sử dụng 4 byte cuối cùng của hash
        int offset = hash[hash.length - 1] & 0xF;
        int binary = ((hash[offset] & 0x7F) << 24) | ((hash[offset + 1] & 0xFF) << 16) | ((hash[offset + 2] & 0xFF) << 8) | (hash[offset + 3] & 0xFF);

        // Chuyển đổi thành chuỗi 6 số
        int otp = binary % 1_000_000;
        return String.format("%06d", otp);
    }

    private boolean validateOtp(String otp, String email) {
        LocalDateTime now = LocalDateTime.now();
        OTP otpEntity = otpRepository.findOTPByOtpAndEmail(otp, email);

        if(otpEntity!=null){
            Duration duration = Duration.between(otpEntity.getExpiryTime(), now);

            double seconds = (double) duration.toMillis() / 1000.0;

//        System.out.println("Seconds: " + seconds);
            if (seconds<60) {
                // Calculate the current 30-second interval
                return true;
            }
        }
        return false;
    }
    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

}
