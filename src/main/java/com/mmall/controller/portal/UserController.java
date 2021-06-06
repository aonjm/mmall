package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/user/")
public class UserController {
    @Autowired
    private IUserService iUserService;
    /**
     * 登陆接口
     */
    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    //自动序列化json
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session){
        //service==>mabatis==>dao
        ServerResponse<User> response=iUserService.login(username,password);
        if(response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return null;
    }

    /**
     * 登出接口
     */
    @RequestMapping(value = "lofinout.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> loginout(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.creatBySuccess();
    }

    /**
     * 注册接口
     */
    @RequestMapping(value = "register.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> register(User user){
        return iUserService.register(user);
    }

    /**
     * 校验接口
     * @param str
     * @param type
     * @return
     */
    @RequestMapping(value = "checkvalid.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> checkValid(String str,String type){
       return iUserService.checkValid(str,type);
    }

    /**
     * 获取用户信息接口
     * @param session
     * @return
     */
    @RequestMapping(value = "getuserinfo.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if (user!=null){
            return ServerResponse.creatBySuccess(user);
        }
        return ServerResponse.creatByErrorMessage("用户未登陆");
    }

    /**
     * 获取用户密码找回问题
     * @param username
     * @return
     */
    @RequestMapping(value = "forgetquestion.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username){
        return iUserService.selectQuestion(username);
    }

    /**
     * 找回密码
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @RequestMapping(value = "forgetcheckanswer.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer){
        return iUserService.checkAnswer(username,question,answer);
    }

    @RequestMapping(value = "forgetresetpassword.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgettoken){
        return iUserService.forgetResetPassword(username,passwordNew,forgettoken);
    }

}
