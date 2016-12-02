package com.tl.ticker.web.action;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by pangjian on 16-12-2.
 */
@Controller
public class IndexAction {

    @RequestMapping("/admin/index")
    public String execute(Model model){
        return "index";
    }
}
