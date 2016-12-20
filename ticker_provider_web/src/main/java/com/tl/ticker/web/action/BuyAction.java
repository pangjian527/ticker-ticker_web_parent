package com.tl.ticker.web.action;

import com.tl.rpc.common.ServiceToken;
import com.tl.rpc.product.Product;
import com.tl.rpc.product.ProductService;
import com.tl.ticker.web.common.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * Created by pangjian on 16-12-20.
 */
@Controller
public class BuyAction {

    @RequestMapping("/portal/buy")
    public String execute(String id, Model model, HttpSession session) throws Exception{

        Object consumer = session.getAttribute(Constant.SESSION_USER);

        Product product = productService.getByProductId(new ServiceToken(), id);

        model.addAttribute("product",product);
        model.addAttribute("consumer",consumer);
        return "buy";
    }

    @Autowired
    private ProductService productService;
}
