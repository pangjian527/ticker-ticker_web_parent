package com.tl.ticker.web.action;

import com.tl.rpc.topic.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by pangjian on 16-11-30.
 */
@Controller
public class TopicAction {

    @RequestMapping("/topics")
    public String list(){
        return "topic/topiclist";
    }

    @Autowired
    private TopicService topicService;

}
