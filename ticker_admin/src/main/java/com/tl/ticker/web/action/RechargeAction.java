package com.tl.ticker.web.action;

import com.tl.rpc.common.ServiceToken;
import com.tl.rpc.consumer.Consumer;
import com.tl.rpc.consumer.ConsumerService;
import com.tl.rpc.recharge.Recharge;
import com.tl.rpc.recharge.RechargeService;
import com.tl.rpc.recharge.SearchResult;
import com.tl.ticker.web.action.entity.PageResult;
import com.tl.ticker.web.action.entity.RechargeResult;
import com.tl.ticker.web.util.StrFunUtil;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by pangjian on 16-12-8.
 */
@Controller
public class RechargeAction {

    @RequestMapping("/admin/recharge/search")
    public String search(Model model, HttpServletRequest request) throws Exception{

        int offset = StrFunUtil.valueInt(request.getParameter("offset"),0);
        int limit = StrFunUtil.valueInt(request.getParameter("limit"),15);

        ServiceToken token = new ServiceToken();
        SearchResult searchResult = rechargeService.searchRecharge(token, limit, offset, null);

        List<RechargeResult> listResult = new LinkedList<RechargeResult>();
        for (Recharge recharge :searchResult.getResult()) {

            RechargeResult rechargeResult = RechargeResult.formRecharge(recharge);
            Consumer consumer = consumerService.getById(token, recharge.getUserId());

            rechargeResult.mobile = consumer.getMobile();
            listResult.add(rechargeResult);
        }

        String url = "/admin/recharge/search";
        model.addAttribute("listResult",listResult);
        model.addAttribute("pageResult",new PageResult(searchResult.getTotalCount(),limit,offset,url));


        return "/recharge/assets";
    }


    @Autowired
    private RechargeService rechargeService;
    @Autowired
    private ConsumerService consumerService;

}
