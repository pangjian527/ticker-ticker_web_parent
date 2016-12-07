package com.tl.ticker.web.action;

import com.tl.rpc.common.ServiceToken;
import com.tl.rpc.consumer.Consumer;
import com.tl.rpc.consumer.ConsumerService;
import com.tl.rpc.order.Order;
import com.tl.rpc.order.OrderResult;
import com.tl.rpc.order.OrderService;
import com.tl.rpc.product.Product;
import com.tl.rpc.product.ProductService;
import com.tl.ticker.web.action.entity.OrderItemResult;
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
 * Created by pangjian on 16-12-7.
 */
@Controller
public class OrderAction {

    @RequestMapping("/admin/order/search")
    public String search(Model model, HttpServletRequest request) throws Exception{

        int offset = StrFunUtil.valueInt(request.getParameter("offset"),0);
        int limit = StrFunUtil.valueInt(request.getParameter("limit"),15);


        ServiceToken token = new ServiceToken();
        OrderResult orderResult = orderService.searchOrder(token, limit, offset);

        List<OrderItemResult> listResult = new LinkedList<OrderItemResult>();

        for (Order order :orderResult.getResult()) {

            OrderItemResult itemResult = OrderItemResult.fromOrder(order);

            Product product = productService.getByProductId(token, order.getProductId());
            itemResult.setTitle(product.getTitle());

            Consumer consumer = consumerService.getById(token, order.getUserId());
            itemResult.setMobile(consumer.getMobile());

            listResult.add(itemResult);
        }

        String url = "/admin/order/search";
        model.addAttribute("listResult",listResult);
        model.addAttribute("pageResult",new PageResult(orderResult.getTotalCount(),limit,offset,url));

        return "/order/order_list";
    }

    @Autowired
    private OrderService orderService;

    @Autowired
    private ConsumerService consumerService;

    @Autowired
    private ProductService productService;

}
