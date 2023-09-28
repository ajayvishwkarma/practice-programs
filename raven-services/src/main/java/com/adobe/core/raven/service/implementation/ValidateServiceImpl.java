package com.adobe.core.raven.service.implementation;

import com.adobe.core.raven.constants.QAConstant;
import com.adobe.core.raven.repository.MasterInfoRepository;
import com.adobe.core.raven.service.interfaces.DataService;
import com.adobe.core.raven.service.interfaces.URLService;
import com.adobe.core.raven.service.interfaces.ValidateService;
import com.adobe.core.raven.service.interfaces.WorkfrontService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ValidateServiceImpl implements ValidateService {

    @Autowired
    URLService urlService;

    @Autowired
    WorkfrontService workfrontService;

    @Autowired
    MasterInfoRepository masterInfoRepository;



    @Autowired
    DataService dataService;



    public Boolean validateImageSource(Element image) {
        String status = urlService.validate(image.attr("src"));
        String[] statusCode = status.split(QAConstant.UNDERSCORE);

        return statusCode[0].equals(QAConstant.VALID);

    }

    public Boolean validateImageAltText(Element image) {

        return !(image.attr("alt") == null || image.attr("alt").equals(""));

    }

    public Boolean compare(String val1, String val2){
        return val1.equals(val2);

    }

    public void validateOptOutLink(String optOutLink, String optOutLinkFromRepo){
        if (!optOutLink.equals(optOutLinkFromRepo)) {
            System.out.println("Subject Line does not match with Cgen");
        }

    }

    public Boolean validateFromAddress(String fromAddressFromMail, String workfrontId,
                                       String local) {


        String fromAddressFromWorkfront = workfrontService.fetchFromAddress(workfrontId);

        if (fromAddressFromWorkfront == null) {
            return false;
        }
        Boolean isSame = false;
        String fromAddressWf = fromAddressFromWorkfront.split("<")[0].trim();
        String fromAddressM = fromAddressFromMail.split("<")[0].trim();
        // normal case
        if (!fromAddressFromWorkfront.contains("for")) {
            //String fromAddress = fromAddressFromWorkfront.split("<")[1].split(">")[0];

            return fromAddressWf.equals(fromAddressM);
        } else {
            // where from name contains for
            String fromName = dataService.getFromName(fromAddressFromWorkfront, local);
            if (fromName == null) {
                isSame = false;
            } else {

                 //completeFromName = fromName + " <" + fromAddressFromWorkfront.split("<")[1];

                isSame = fromName.equals(fromAddressM);
            }
        }
        return isSame;
    }


}



