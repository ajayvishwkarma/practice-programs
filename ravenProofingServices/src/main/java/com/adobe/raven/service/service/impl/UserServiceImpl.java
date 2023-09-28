package com.adobe.raven.service.service.impl;

import com.adobe.raven.Utils;
import com.adobe.raven.db.queries.UserInfoQueries;
import com.adobe.raven.dto.user.UserInfo;
import com.adobe.raven.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserInfoQueries userInfoQueries;

    public boolean createUser(UserInfo userInfo){
        UserInfo userInfo1=new UserInfo();
        Utils u=new Utils();
        userInfo1.setName(userInfo.getName());
        userInfo1.setRole(userInfo.getRole());
        userInfo1.setEmailId(userInfo.getEmailId());
        userInfo1.setGuidId(u.createUUID());
        return userInfoQueries.update(userInfo1);
    }
}

