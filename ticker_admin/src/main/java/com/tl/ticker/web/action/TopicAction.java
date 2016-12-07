package com.tl.ticker.web.action;

import com.tl.rpc.common.ServiceToken;
import com.tl.rpc.consumer.Consumer;
import com.tl.rpc.consumer.ConsumerService;
import com.tl.rpc.topic.*;
import com.tl.ticker.web.action.entity.PageResult;
import com.tl.ticker.web.action.entity.ResultJson;
import com.tl.ticker.web.action.entity.TopicResult;
import com.tl.ticker.web.util.StrFunUtil;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by pangjian on 16-11-30.
 */
@Controller
public class TopicAction {

    @RequestMapping("/admin/topics")
    public String search(Model model , HttpServletRequest request) throws Exception{

        int offset = StrFunUtil.valueInt(request.getParameter("offset"),0);
        int limit = StrFunUtil.valueInt(request.getParameter("limit"),15);

        ServiceToken token = new ServiceToken();

        SearchTopicResult result = topicService.searchTopic(token, limit, offset, null);

        List<TopicResult> listResult = new LinkedList<TopicResult>();
        for (Topic topic : result.getResult()) {

            Consumer consumer = consumerService.getById(token, topic.getUserId());
            TopicResult topicResult = TopicResult.fromTopicResult(topic);
            topicResult.mobile = consumer.getMobile();

            listResult.add(topicResult);
        }

        String url = "/admin/topics";
        model.addAttribute("listResult",listResult);
        model.addAttribute("pageResult",new PageResult(result.getTotalCount(),limit,offset,url));

        return "topic/topiclist";
    }

    @RequestMapping("/admin/topic/post")
    public String postTopic(Model model,String topicId) throws Exception{

        if(StringUtils.isNotBlank(topicId)){
            Topic topic = topicService.getByTopicId(new ServiceToken(), topicId);
            model.addAttribute("topic",topic);
        }else{
            model.addAttribute("topic",new Topic());
        }

        return "topic/post_topic";
    }

    @RequestMapping("/admin/topic/save")
    public String save(Model model,String title,int year,long balance,int stage,String content,String id) throws Exception{

        Topic topic = null;
        ServiceToken token = new ServiceToken();
        if(StringUtils.isNotBlank(id)){
            topic = topicService.getByTopicId(token,id);

        }else{
            topic = new Topic();
            topic.setCreateTime(new Date().getTime());
            topic.setType(TOPICTYPE.CHARGE);
            topic.setStatus(TOPICSTATUS.OPEN);
            topic.setUserId("1");
        }

        topic.setContent(content);
        topic.setYear(year);
        topic.setStage(stage);
        topic.setTitle(title);
        topic.setBalance(balance);

        topicService.saveTopic(token,topic);

        return ResultJson.returnSuccess("发表成功",model);
    }

    @Autowired
    private TopicService topicService;

    @Autowired
    private ConsumerService consumerService;

}
