package com.tl.ticker.web.action;

import com.sun.rmi.rmid.ExecPermission;
import com.tl.rpc.common.ServiceToken;
import com.tl.rpc.sys.SysUser;
import com.tl.rpc.sys.SysUserService;
import com.tl.rpc.topic.SearchTopicResult;
import com.tl.rpc.topic.TOPICSTATUS;
import com.tl.rpc.topic.TopicService;
import com.tl.ticker.web.common.Constant;
import com.tl.ticker.web.entity.ResultJson;
import com.tl.ticker.web.util.JsonUtil;
import com.tl.ticker.web.util.ValidateCodeUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 */
@Controller
public class LoginAction {

    @RequestMapping("/admin")
    public String execute(HttpSession session,Model model){

        ValidateCodeUtil valiCode = new ValidateCodeUtil(130,40,4,150);

        model.addAttribute("valiBase64Image","data:image/jpg;base64"+valiCode.getBase64Code());
        session.setAttribute(Constant.VALID_CODE,valiCode.getCode());

        return "login";
    }

    @ResponseBody
    @RequestMapping("/admin/login")
    public String login(HttpSession session, String mobile, String pwd, String validCode) {

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

        SysUser sysUser = null ;
        try{
            sysUser = sysUserService.getByAccount(new ServiceToken(),mobile);
        }catch (TException e){
            e.printStackTrace();
            return JsonUtil.toString(new ResultJson(false, "用户不存在"));
        }

        if(!pwd.equals(sysUser.getPwd())){
            return JsonUtil.toString(new ResultJson(false, "密码错误"));
        }

        //缓存用户信息
        session.setAttribute(Constant.SESSION_USER,sysUser);
        return JsonUtil.toString(new ResultJson(true));
    }

    @Autowired
    private SysUserService sysUserService;
}
