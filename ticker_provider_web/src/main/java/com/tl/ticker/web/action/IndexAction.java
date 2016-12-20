package com.tl.ticker.web.action;

import com.tl.rpc.base.BaseData;
import com.tl.rpc.base.BaseDataService;
import com.tl.rpc.common.ServiceToken;
import com.tl.rpc.consumer.Consumer;
import com.tl.rpc.consumer.ConsumerService;
import com.tl.rpc.product.Product;
import com.tl.rpc.product.ProductService;
import com.tl.rpc.product.SearchProductResult;
import com.tl.rpc.topic.SearchTopicResult;
import com.tl.rpc.topic.TOPICSTATUS;
import com.tl.rpc.topic.Topic;
import com.tl.rpc.topic.TopicService;
import com.tl.ticker.web.action.entity.ProductResult;
import com.tl.ticker.web.action.entity.TopicResult;
import com.tl.ticker.web.common.Constant;
import com.tl.ticker.web.util.StrFunUtil;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by pangjian on 16-12-17.
 */
@Controller
public class IndexAction {

    @RequestMapping("/portal/index")
    public String execute(Model model, HttpServletRequest request, HttpSession session) throws Exception{

        int offset = StrFunUtil.valueInt(request.getParameter("offset"),0);
        int limit = StrFunUtil.valueInt(request.getParameter("limit"),10);

        //1. 获取商品资料数据
        ServiceToken token = new ServiceToken();
        SearchProductResult productResult = productService.searchProduct(token, limit, offset);

        List<ProductResult> listResult = new LinkedList<ProductResult>();
        for (Product product : productResult.getResult()) {
            listResult.add(ProductResult.fromProductResult(product));
        }

        //2. 获取论坛数据
        SearchTopicResult topicResultList = topicService.searchTopic(token, 30, 0, TOPICSTATUS.OPEN);

        List<TopicResult> listTopicResult = new LinkedList<TopicResult>();
        for (Topic topic : topicResultList.getResult()) {

            Consumer consumer = consumerService.getById(token, topic.getUserId());
            TopicResult topicResult = TopicResult.fromTopicResult(topic);
            topicResult.mobile = consumer.getMobile();

            listTopicResult.add(topicResult);
        }

        //3. 获取基础开奖数据
        List<BaseData> baseDatas = baseDataService.searchBaseData(token, 2016);

        Object object = session.getAttribute(Constant.SESSION_USER);

        if(object != null){
            model.addAttribute("consumer",object);
        }

        model.addAttribute("listResult",listResult);
        model.addAttribute("baseDatas",baseDatas);
        model.addAttribute("listTopicResult",listTopicResult);
        return "index";
    }

    @Autowired
    private ProductService productService;
    @Autowired
    private TopicService topicService;
    @Autowired
    private ConsumerService consumerService;
    @Autowired
    private BaseDataService baseDataService;

}
