package com.adobe.core.raven.config;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Configuration
public class MongoConfig {

	public MongoClient mongoClient() {

		String user = encode("");// encode(psdkPassword.getUserName());
		String password = encode("");// encode(psdkPassword.getContent());
		return MongoClients.create("mongodb://" +"localhost:27017");

		//return MongoClients.create("mongodb://" + user + ":" + password + "@10.42.77.14:27017");
		//return MongoClients.create("mongodb://" + user + ":" + password + "@10.42.77.218:27017");
	}

	public @Bean MongoTemplate mongoTemplate() {
		//return new MongoTemplate(mongoClient(), "kaleidoscope-raven-db");
		return new MongoTemplate(mongoClient(), "local");
	}


	public static String encode(String url) {
		try {
			String encodeURL = URLEncoder.encode(url, "UTF-8");
			return encodeURL;
		} catch (UnsupportedEncodingException e) {
			return "Issue while encoding" + e.getMessage();
		}
	}

}
