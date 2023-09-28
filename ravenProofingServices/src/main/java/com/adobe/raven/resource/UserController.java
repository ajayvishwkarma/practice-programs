package com.adobe.raven.resource;

import com.adobe.raven.Utils;
import com.adobe.raven.db.queries.GeoReviewerQueries;
import com.adobe.raven.db.queries.UserInfoQueries;
import com.adobe.raven.dto.ImsUserInfo;
import com.adobe.raven.dto.geoReviewer.GeoReviewer;
import com.adobe.raven.dto.user.ImsUserDetails;
import com.adobe.raven.dto.user.UserInfo;
import com.adobe.raven.request.GeoReviewerRequest;
import com.adobe.raven.request.UserRequest;
import com.adobe.raven.response.GeoReviewerResponse;
import com.adobe.raven.response.UserResponse;
import com.adobe.raven.service.service.impl.UserServiceImpl;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value="/v1/user")
public class UserController {

    @Autowired
    private UserInfoQueries userInfoQueries;

    @Autowired
    private GeoReviewerQueries geoReviewerQueries;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @CrossOrigin
    @RequestMapping(value = "/geoReviewer/list", method = RequestMethod.POST)
    public GeoReviewerResponse getGeoReviewers(@RequestBody GeoReviewerRequest geoReviewerRequest) {

        GeoReviewerResponse response = new GeoReviewerResponse();
        ArrayList<GeoReviewer> geoReviewers = new ArrayList<>();
        ArrayList<String> languages = geoReviewerRequest.getLanguages();

        for(String lang : languages) {

            List<GeoReviewer> list = geoReviewerQueries.generalGetList("language",lang);
            geoReviewers.addAll(list);
        }
        response.setResult(geoReviewers);

//        for(GeoReviewer geoReviewer : geoReviewers) {
//            GeoReviewer inReviewer = geoReviewerQueries.getByEmail(geoReviewer.getEmail());
//            System.out.println("Reviewer is :: " + inReviewer.getName());
//        }

        return response;
    }


    @CrossOrigin
    @RequestMapping(value="/geoReviewer/bulkInsert", method = RequestMethod.POST)
    public GeoReviewerResponse insertGeoReviewers(@RequestBody GeoReviewerRequest geoReviewerRequest) {

        geoReviewerQueries.insertGeoReviewers(geoReviewerRequest.getReviewers());

        GeoReviewerResponse geoReviewerResponse = new GeoReviewerResponse();
        geoReviewerResponse.setIsInserted(true);
        return geoReviewerResponse;
    }

    @CrossOrigin
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public UserResponse getUserInfo(@RequestBody UserRequest userRequest) {

        UserResponse userResponse = new UserResponse();
        List<UserInfo> users;

        if(userRequest == null ||
        userRequest.getType() == null) {
            users = userInfoQueries.getAllList();
        } else {
            users = userInfoQueries.generalGetList("role", "CampaignDeveloper");
        }

        userResponse.setResults(users);
        return userResponse;
    }

    @CrossOrigin
    @RequestMapping(value="/email/{mailId}", method = RequestMethod.GET)
    public UserResponse getUserDetail(@PathVariable String mailId) {

        UserResponse userResponse = new UserResponse();
        UserInfo userInfo = userInfoQueries.get(mailId);
        userResponse.setUserInfo(userInfo);
        return userResponse;
    }

    @CrossOrigin
    @RequestMapping(value = "/validateimstoken",method = RequestMethod.POST)
    public String validateImsToken(@RequestBody ImsUserInfo imsUserInfo) {

        String url = imsUserInfo.getUrl() + "validate_token/v1?" + "client_id="
                + imsUserInfo.getClientId() + "&type=" + imsUserInfo.getType()
                + "&token=" + imsUserInfo.getBearerToken();

        String response = null;
        response = Utils.callGetUrl(url);

        return response;
    }

    @CrossOrigin
    @RequestMapping(value = "/imsuserinfo",method = RequestMethod.POST)
    public String getImsUserInfo(@RequestBody ImsUserInfo imsUserInfo) {

        String url = imsUserInfo.getUrl() + "profile/v1?" + "client_id=" + imsUserInfo.getClientId()
                + "&bearer_token=" + imsUserInfo.getBearerToken();
        String response = null;
        try {
            response = Utils.callPostUrl(url ,"{}", null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        ImsUserDetails imsUserDetails = gson.fromJson(response, ImsUserDetails.class);

        UserInfo insertedUserInfo = userInfoQueries.get(imsUserDetails.getEmail());
        if(insertedUserInfo != null) {

            insertedUserInfo.setGuidId(imsUserDetails.getAuthId());
            userInfoQueries.update(insertedUserInfo);
        }

        return response;
    }



    //update user by guidId
    @CrossOrigin
    @RequestMapping(value="/guidId/{guidId}", method = RequestMethod.GET)
    public UserResponse getUserDetailByGuidId(@PathVariable String guidId, @RequestBody UserInfo userInfo) {

        UserResponse userResponse = new UserResponse();
        UserInfo userInfo1 = userInfoQueries.getByGuidID(guidId);
        if(userInfoQueries.getByGuidID(guidId)==null){
            System.out.println("data not found");

        }
        else{
            if(userInfo.getName()!=null){
                userInfo1.setName(userInfo.getName());
            }
            if(userInfo.getEmailId()!=null){
                userInfo1.setEmailId(userInfo.getEmailId());
            }
            if(userInfo.getRole()!=null){
                userInfo1.setRole(userInfo.getRole());
            }
            if(userInfo.getRegion()!=null){
                userInfo1.setRegion(userInfo.getRegion());
            }
            userInfoQueries.update(userInfo1);
        }
        userResponse.setUserInfo(userInfo1);
        return userResponse;
    }


    @CrossOrigin
    @RequestMapping(value = "/createUser",method = RequestMethod.POST)
    public boolean createUser(@RequestBody UserInfo userInfo1){
       // UserInfo userInfo=new UserInfo();
    //userServiceImpl.createUser(userInfo1);
       // userInfoQueries.insert(userInfo);
        return userServiceImpl.createUser(userInfo1);
    }

}