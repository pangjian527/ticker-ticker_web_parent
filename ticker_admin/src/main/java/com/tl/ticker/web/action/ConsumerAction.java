package com.tl.ticker.web.action;

import com.tl.rpc.common.ServiceToken;
import com.tl.rpc.consumer.CONSUMERSTATUS;
import com.tl.rpc.consumer.Consumer;
import com.tl.rpc.consumer.ConsumerService;
import com.tl.rpc.consumer.SearchResult;
import com.tl.rpc.recharge.Recharge;
import com.tl.rpc.recharge.RechargeService;
import com.tl.ticker.web.action.entity.ConsumerResult;
import com.tl.ticker.web.action.entity.PageResult;
import com.tl.ticker.web.action.entity.ResultJson;
import com.tl.ticker.web.util.JsonUtil;
import com.tl.ticker.web.util.StrFunUtil;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
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

        String url ="/admin/consumer/search";
        model.addAttribute("listResult",listResult);
        model.addAttribute("pageResult",new PageResult(searchResult.getTotalCount(),limit,offset,url));

        return "consumer/consumer_list";
    }

    /* 用户充值 */
    @RequestMapping("/admin/consumer/recharge")
    public String recharge(HttpServletRequest request,Model model,String userId ) throws Exception{


        String balance = request.getParameter("balance");
        String giveAmount = request.getParameter("giveAmount");

        long gold = Long.valueOf(balance);
        long giveGold = Long.valueOf(giveAmount);


        ServiceToken token = new ServiceToken();
        Consumer consumer = consumerService.getById(token, userId);
        consumer.setBalance(consumer.getBalance() + gold+ giveGold);
        consumerService.saveConsumer(token,consumer);

        rechargeService.saveRecharge(token,createRecharge(gold,giveGold,userId));

        return ResultJson.returnSuccess("充值成功",model);
    }

    @ResponseBody
    @RequestMapping("/admin/consumer/disable")
    public String disable(Model model, HttpServletRequest request) throws Exception{
        String userId = request.getParameter("userId");
        return updateConsumer(CONSUMERSTATUS.INVALID,userId);
    }

    @ResponseBody
    @RequestMapping("/admin/consumer/enable")
    public String enable(Model model, HttpServletRequest request) throws Exception{
        String userId = request.getParameter("userId");
        return updateConsumer(CONSUMERSTATUS.EFFECTIVE,userId);
    }

    private String updateConsumer(CONSUMERSTATUS status,String userId) throws Exception{

        ServiceToken token = new ServiceToken();
        Consumer consumer = consumerService.getById(token, userId);

        consumer.setStatus(status);
        consumerService.saveConsumer(token,consumer);
        return JsonUtil.toString(new ResultJson(true));
    }

    @RequestMapping("/admin/consumer/detail")
    public String consumerDetail(Model model, String userId) throws Exception{

        ServiceToken token = new ServiceToken();
        Consumer consumer = consumerService.getById(token, userId);
        model.addAttribute("consumer",consumer);

        return "consumer/balance_recharge";
    }

    private Recharge createRecharge(long balance,long giveAmount,String userId){
        Recharge recharge = new Recharge();
        recharge.setCreateTime(new Date().getTime());
        recharge.setAmount(balance);
        recharge.setGiveAmount(giveAmount);
        recharge.setSource("sys");
        recharge.setUserId(userId);

        return recharge;
    }

    @Autowired
    private ConsumerService consumerService;

    @Autowired
    private RechargeService rechargeService;

}
