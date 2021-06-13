package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
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
    @RequestMapping(value = "lofinout.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> loginout(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.creatBySuccess();
    }

    /**
     * 注册接口
     */
    @RequestMapping(value = "register.do",method = RequestMethod.POST)
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
    @RequestMapping(value = "checkvalid.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str,String type){
       return iUserService.checkValid(str,type);
    }

    /**
     * 获取用户信息接口
     * @param session
     * @return
     */
    @RequestMapping(value = "getuserinfo.do",method = RequestMethod.POST)
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
    @RequestMapping(value = "forgetquestion.do",method = RequestMethod.POST)
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
    @RequestMapping(value = "forgetcheckanswer.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer){
        return iUserService.checkAnswer(username,question,answer);
    }

    @RequestMapping(value = "forgetresetpassword.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgettoken){
        return iUserService.forgetResetPassword(username,passwordNew,forgettoken);
    }

    /**
     * 登陆状态重置密码
     * @param session
     * @param passwordOld
     * @param passwordNew
     * @return
     */
    @RequestMapping(value = "resetpassword.do",method =RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpSession session,String passwordOld,String passwordNew){
        User user=(User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.creatByErrorMessage("用户未登陆");
        }
        return iUserService.resetPassword(passwordOld,passwordNew,user);
    }

    /**
     * 更新用户信息
     * @param session
     * @param user
     * @return
     */
    @RequestMapping(value = "updateInfomation.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateInfomation(HttpSession session,User user){
        User currentUser=(User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser==null){
            return ServerResponse.creatByErrorMessage("用户未登陆");
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        ServerResponse<User> response=iUserService.updateInfomation(user);
        if (response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }

    /**
     * 获取登陆信息
     * @param session
     * @return
     */
    @RequestMapping(value = "getInfomation.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getInfomation(HttpSession session){
        //需要强制登陆
        User currentUser=(User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser==null){
            return ServerResponse.creatByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆");
        }
        return iUserService.getInfomation(currentUser.getId());
    }

}
