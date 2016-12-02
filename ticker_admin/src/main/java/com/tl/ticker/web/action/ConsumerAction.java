package com.tl.ticker.web.action;

import com.tl.rpc.common.ServiceToken;
import com.tl.rpc.consumer.Consumer;
import com.tl.rpc.consumer.ConsumerService;
import com.tl.rpc.consumer.SearchResult;
import com.tl.ticker.web.action.entity.ConsumerResult;
import com.tl.ticker.web.action.entity.PageResult;
import com.tl.ticker.web.util.StrFunUtil;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by pangjian on 16-12-2.
 */
@Controller
public class ConsumerAction {

    @RequestMapping("/admin/consumer/search")
    public String search(Model model, HttpServletRequest request) throws Exception{

        int offset = StrFunUtil.valueInt(request.getParameter("offset"),0);
        int limit = StrFunUtil.valueInt(request.getParameter("limit"),15);

        SearchResult searchResult = consumerService.searchConsumer(new ServiceToken(),limit, offset );

        List<ConsumerResult> listResult = new LinkedList<ConsumerResult>();
        for (Consumer consumer : searchResult.getResult()) {
            ConsumerResult result = ConsumerResult.fromConsuemr(consumer);
            listResult.add(result);
        }

        model.addAttribute("listResult",listResult);
        model.addAttribute("pageResult",new PageResult(searchResult.getTotalCount(),limit,offset));

        return "consumer/consumer_list";
    }

    @Autowired
    private ConsumerService consumerService;

}
