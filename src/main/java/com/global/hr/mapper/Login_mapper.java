package com.global.hr.mapper;

import com.global.hr.models.DTO.Post_Login;
import com.global.hr.models.User;

public class Login_mapper {

    public static Post_Login UserToPostLogin(User user) {
        // تحويل كائن User إلى كائن Post_Login
        return new Post_Login(user.getId(), user.getRole());
    }
}
