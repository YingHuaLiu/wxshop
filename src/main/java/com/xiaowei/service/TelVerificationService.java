package com.xiaowei.service;

import com.xiaowei.controller.AuthController;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class TelVerificationService {
    public static Pattern Tel_Pattern = Pattern.compile("1\\d{10}");

    public boolean verifyTelParameter(AuthController.TelAndCode telAndCode) {
        if (telAndCode == null) {
            return false;
        } else if (telAndCode.getTel() == null) {
            return false;
        } else {
            return Tel_Pattern.matcher(telAndCode.getTel()).find();
        }
    }
}
