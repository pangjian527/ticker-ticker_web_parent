package com.tl.ticker.web.action;

import com.tl.rpc.common.RpcException;
import com.tl.rpc.common.ServiceToken;
import com.tl.rpc.consumer.CONSUMERSTATUS;
import com.tl.rpc.consumer.Consumer;
import com.tl.rpc.consumer.ConsumerService;
import com.tl.ticker.web.action.entity.ResultJson;
import com.tl.ticker.web.common.Constant;
import com.tl.ticker.web.util.JsonUtil;
import com.tl.ticker.web.util.ValidateCodeUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * Created by pangjian on 16-12-19.
 */
@Controller
public class RegisterAction {

    @RequestMapping("/portal/register")
    public String execute(Model model, HttpSession session){

        ValidateCodeUtil valiCode = new ValidateCodeUtil(130,40,4,150);

        model.addAttribute("valiBase64Image","data:image/jpg;base64"+valiCode.getBase64Code());
        session.setAttribute(Constant.VALID_CODE,valiCode.getCode());

        return "register";
    }

    @ResponseBody
    @RequestMapping("/portal/register/submit")
    public String register(HttpSession session, String mobile, String pwd, String validCode,String msgCode,String confirmPwd) throws Exception {

        if(StringUtils.isBlank(mobile)){
            return JsonUtil.toString(new ResultJson(false,"手机号码必填"));
        }else  if(StringUtils.isBlank(pwd)){
            return JsonUtil.toString(new ResultJson(false,"请填写密码"));
        }else if(StringUtils.isBlank(validCode)) {
            System.out.println(JsonUtil.toString(new ResultJson(false, "请填写验证码")));
            return JsonUtil.toString(new ResultJson(false, "请填写验证码"));
        }

        String code = session.getAttribute(Constant.VALID_CODE).toString();

        if(!code.equalsIgnoreCase(validCode)){
            return JsonUtil.toString(new ResultJson(false, "验证码不正确"));
        }

        if(!pwd.equals(confirmPwd)){
            return JsonUtil.toString(new ResultJson(false, "两次密码输入不匹配"));
        }

        Consumer consumer = null ;
        try{
            consumer = consumerService.getByMobile(new ServiceToken(),mobile);
        }catch (Exception e){
            e.printStackTrace();
        }

        if(consumer != null ){
            return JsonUtil.toString(new ResultJson(false, "用户已经存在请登录"));
        }

        consumer = new Consumer();
        consumer.setBalance(0);
        consumer.setStatus(CONSUMERSTATUS.EFFECTIVE);
        consumer.setCreateTime(new Date().getTime());
        consumer.setPwd(pwd);
        consumer.setMobile(mobile);
        consumer.setUpdateTime(new Date().getTime());

        consumerService.saveConsumer(new ServiceToken(),consumer);

        return JsonUtil.toString(new ResultJson(true));
    }

    @Autowired
    private ConsumerService consumerService;
}
