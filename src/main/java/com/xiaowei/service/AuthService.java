package com.xiaowei.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private UserService userService;
    private VerificationCodeCheckService verificationCodeCheckService;
    private SmsCodeService smsCodeService;

    @Autowired
    public AuthService(UserService userService,
                       VerificationCodeCheckService verificationCodeCheckService,
                       SmsCodeService smsCodeService) {
        this.userService = userService;
        this.verificationCodeCheckService = verificationCodeCheckService;
        this.smsCodeService=smsCodeService;
    }

    public void sendVerificationCode(String tel) {
        userService.createUserIfNotExist(tel);
        String correctCode = smsCodeService.sendSmsCode(tel); //000000
        verificationCodeCheckService.addCode(tel, correctCode);
    }
}
