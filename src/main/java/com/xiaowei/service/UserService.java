package com.xiaowei.service;

import com.xiaowei.dao.UserDao;
import com.xiaowei.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {
    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void createUserIfNotExist(String tel) {
        if (userDao.getUserByTel(tel) == null) {
            User user = new User();
            user.setTel(tel);
            user.setCreatedAt(new Date());
            user.setUpdatedAt(new Date());
            userDao.insertUser(user);
        }
    }

    public User getUserByTel(String tel) {
        return userDao.getUserByTel(tel);
    }
}
