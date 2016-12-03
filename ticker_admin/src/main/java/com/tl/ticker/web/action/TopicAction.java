package com.tl.ticker.web.action;

import com.tl.rpc.common.ServiceToken;
import com.tl.rpc.consumer.Consumer;
import com.tl.rpc.consumer.ConsumerService;
import com.tl.rpc.topic.SearchTopicResult;
import com.tl.rpc.topic.Topic;
import com.tl.rpc.topic.TopicService;
import com.tl.ticker.web.action.entity.PageResult;
import com.tl.ticker.web.action.entity.TopicResult;
import com.tl.ticker.web.util.StrFunUtil;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @Autowired
    private TopicService topicService;

    @Autowired
    private ConsumerService consumerService;

}
