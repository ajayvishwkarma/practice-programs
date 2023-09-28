package com.adobe.core.raven.steps;

import com.adobe.core.raven.constants.JobConstant;
import com.adobe.core.raven.constants.QAConstant;
import com.adobe.core.raven.dto.SocialLinkEnum;
import com.adobe.core.raven.dto.model.SocialRepository;
import com.adobe.core.raven.dto.qa.Link;
import com.adobe.core.raven.dto.repo.MasterRepository;
import com.adobe.core.raven.dto.repo.SocialLinks;
import com.adobe.core.raven.repository.MasterInfoRepository;
import com.adobe.core.raven.service.interfaces.DataService;
import com.adobe.core.raven.service.interfaces.OutputMapperService;
import com.adobe.core.raven.service.interfaces.URLService;
import com.adobe.core.raven.service.interfaces.ValidateService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ValidateSocialStep implements Tasklet {

    @Autowired
    ValidateService validateService;

    @Autowired
    OutputMapperService outputMapperService;

    @Autowired
    DataService dataService;

    @Autowired
    URLService urlService;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        System.out.println("Social Links Step");
        JobParameters jobParameters = chunkContext.getStepContext().getStepExecution().getJobParameters();
        String stepId = jobParameters.getString(JobConstant.STEP_ID);
        String messageId = jobParameters.getString(JobConstant.MESSAGE_ID);
        String htmlBody = jobParameters.getString(JobConstant.HTML_BODY);
        String locale = dataService.fetchLocale(messageId);
        String bu = dataService.fetchBu(stepId,messageId);
        //String bu = "CC";
        Document parsedHtml = Jsoup.parse(htmlBody, QAConstant.UTF, Parser.xmlParser());
        Elements links = parsedHtml.getElementsByTag(QAConstant.TAG_A);
        links = fetchRedirectedLinks(links);

        ArrayList<Link> socialLinkDetails = new ArrayList<>();

        String state = JobConstant.PASS;
        for (SocialLinkEnum socialLinkEnum : SocialLinkEnum.values()) {
            System.out.println(socialLinkEnum.name());
            Elements socialLinks = links.select(socialLinkEnum.getLinktag());

            if( !socialLinks.isEmpty()){
                Element socialLink = socialLinks.get(0).parent();
                String socialLinkUrl = socialLink.attr("href");

                String socialUrlFromRepo = dataService.fetchSocialLink(locale,bu,socialLinkEnum.getLinkType());
                Link link = new Link();
                if(socialUrlFromRepo != null
                && socialLinkUrl != null
                        && validateService.compare(socialUrlFromRepo.toLowerCase(), socialLinkUrl.toLowerCase())){
                    link.setState(JobConstant.PASS);
                    link.setReason(JobConstant.PASS);
                }else{
                    state = JobConstant.FAIL;
                    link.setState(JobConstant.FAIL);
                    link.setReason(JobConstant.VALIDATE_SOCIAL_REASON);
                }
                link.setLink(socialLinkUrl);
                socialLinkDetails.add(link);
            }

        }
        outputMapperService.mapValidationOutput(JobConstant.VALIDATE_SOCIAL_LABEL, JobConstant.VALIDATE_SOCIAL_TYPE, JobConstant.VALIDATE_SOCIAL_NAME, state, socialLinkDetails,stepId,messageId);


        return RepeatStatus.FINISHED;
    }


    private Elements fetchRedirectedLinks(Elements links) {

        links.forEach(link -> {
            String hrefValue = link.attr(QAConstant.HREF);
            if(!hrefValue.contains("mailto:")) {
                HashMap<String, Integer> responseMap = urlService.getRedirectedUrl(hrefValue);
                System.out.println("here : " + responseMap);
                String url = responseMap.entrySet().stream().iterator().next().getKey();
                link.attr("href", url);
            }

        });

        return links;
    }


}