package com.adobe.raven.db.queries;

import com.adobe.raven.MailUtils;
import com.adobe.raven.Utils;
import com.adobe.raven.dto.geoReviewer.GeoReviewer;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Repository
public class GeoReviewerQueries {

    @Autowired
    public MongoTemplate mongoTemplate;

    public GeoReviewer insert(GeoReviewer geoReviewer) {
        GeoReviewer insertedGeoReviewer = this.mongoTemplate.save(geoReviewer, "geoReviewer");

        return insertedGeoReviewer;
    }



    public boolean update(GeoReviewer geoReviewer) {

        boolean isUpdated = false;
        GeoReviewer insertedGeoReviewer = this.mongoTemplate.save(geoReviewer);
        if (insertedGeoReviewer != null) {

            isUpdated = true;
        } else {

            isUpdated = false;
        }
        return isUpdated;
    }

    public GeoReviewer get(String id) {

        GeoReviewer geoReviewer = mongoTemplate.findById(id, GeoReviewer.class);

        return geoReviewer;
    }

    public GeoReviewer getByEmail(String emailId) {

        List<GeoReviewer> reviewers = generalGetList("email", emailId);
        GeoReviewer geoReviewer =  reviewers != null && reviewers.size() > 0 ? reviewers.get(0):null;

        return geoReviewer;
    }

//    public List<GeoReviewer> getByEmails(ArrayList<String> emailIds) {
//
// //      String  emailId=MailUtils.changeListToString(emailIds);
//        List<GeoReviewer> reviewers = generalGetLists("email", emailIds);
//  //      GeoReviewer geoReviewer =  reviewers != null && reviewers.size() > 0 ? reviewers.get(0):null;
//
//        return reviewers;
//    }

    public List<GeoReviewer> getAllList() {

        return mongoTemplate.findAll(GeoReviewer.class);
    }

    public void insertGeoReviewers(List<GeoReviewer> reviewers) {

        for(GeoReviewer reviewer : reviewers) {

            GeoReviewer insertedReviewer = getReviewer("email", reviewer.getEmail(),
                    "language", reviewer.getLanguage());

            if(insertedReviewer == null) {

                reviewer.setId(Utils.createUUID());
                reviewer.setStatus("enabled");
                String reviewerType = reviewer.getGeoReviewerType().toLowerCase(Locale.ROOT);
                reviewer.setGeoReviewerType(reviewerType);
                this.insert(reviewer);
            }
        }

    }

    public GeoReviewer getReviewer(String key1, String value1, String key2, String value2) {
        Query query = new Query();

        Criteria criteria = new Criteria();
        criteria.and(key1).is(value1);
        criteria.and(key2).is(value2);

        query.addCriteria(criteria);
        List<GeoReviewer> reviewers = mongoTemplate.find(query, GeoReviewer.class);

        return reviewers != null && reviewers.size() > 0 ? reviewers.get(0) : null;
    }

    public List<GeoReviewer> generalGetList(String key, String value) {
        Query query = new Query();

        Criteria criteria = new Criteria();
        criteria.and(key).is(value);

        query.addCriteria(criteria);
        List<GeoReviewer> users = mongoTemplate.find(query, GeoReviewer.class);

        return users;
    }

//    public List<GeoReviewer> generalGetLists(String key, ArrayList<String> emailIds) {
//
////         Query query = new Query();
////
////       Criteria criteria = new Criteria();
////        criteria.and(key).in(value);
//
// //       query.addCriteria(criteria);
//
//  //      mongoTemplate.;
//  //      List<GeoReviewer> users = mongoTemplate.getCollection('GeoReviewer.class').find({"email":{"$in":[("kmehra@adobe.com"),("vkumarrajapu@adobe.com")]}});
//
////        String s = "abc.com"+","+"bcd.com"+","+"xyz.com";
// //       BasicDBObject gtQuery = new BasicDBObject();
// //       gtQuery.put("email",new BasicDBObject("$in",s));
//
//
// //       FindIterable<Document> document=mongoTemplate.getCollection("GeoReviewer.class").find(gtQuery);
////        List<GeoReviewer> users = mongoTemplate.find(query, GeoReviewer.class);
//
//
//        Query query = new Query();
//        List<Criteria> criteria = new ArrayList<>();
//        for(String emailId:emailIds){
//            criteria.add(Criteria.where(key).is(emailId));
//        }
//// you can add all your fields here as above
//
//        query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])));
//        List<GeoReviewer> geoReviewers = mongoTemplate.find(query, GeoReviewer.class);
//
//        return geoReviewers;
//    }
}
