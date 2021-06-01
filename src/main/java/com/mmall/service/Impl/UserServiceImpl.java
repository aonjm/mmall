package com.mmall.service.Impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("IUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    public ServerResponse<User> login(String username, String password) {

        //1.检查用户名是否存在
        int resultCount;
        resultCount=userMapper.checkUsername(username);
        if (resultCount==0){
            return ServerResponse.creatByErrorMessage("用户名不存在");
        }
        //密码登陆MD5
        //todo
        String md5Password=MD5Util.MD5EncodeUtf8(password);
        User user= userMapper.selectLogin(username,md5Password);
        if(user==null){
            return ServerResponse.creatByErrorMessage("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);

        return ServerResponse.creatBySuccess("登陆成功",user);
    }

    public ServerResponse<String> register(User user){
        //校验用户名和Email
        int resultCount;
//        resultCount=userMapper.checkUsername(user.getUsername());
//        if (resultCount>=0){
//            return ServerResponse.creatByErrorMessage("用户名已经存在");
//        }
        ServerResponse validResponse= this.checkValid(user.getUsername(),Const.USERNAME);
        if (!validResponse.isSuccess()){
            return validResponse;
        }
        resultCount=userMapper.checkEmail(user.getEmail());

//        if (resultCount>=0){
//            return ServerResponse.creatByErrorMessage("email已经存在");
//        }
        validResponse=this.checkValid(user.getEmail(),Const.USEREMAIL);
        if (!validResponse.isSuccess()){
            return validResponse;
        }
        user.setRole(Const.Role.ROLE_CUSTOMER);

        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        resultCount=userMapper.insert(user);
        if (resultCount==0){
            return ServerResponse.creatByErrorMessage("注册失败");
        }
        return ServerResponse.creatBySuccess("注册成功");

    }

    /**
     *
     * @param str
     * @param type
     * @return
     */
    public ServerResponse<String> checkValid(String str, String type){
        if (StringUtils.isNoneBlank(type)){
            //开始校验
            if (Const.USERNAME.equals(type)){
                int resultCount=userMapper.checkUsername(str);
                if (resultCount>0){
                    return ServerResponse.creatByErrorMessage("用户名已经存在");
                }
            }

            if (Const.USEREMAIL.equals(type)){
                int resultCount=userMapper.checkEmail(str);
                if (resultCount>0){
                    return ServerResponse.creatByErrorMessage("email已经存在");
                }
            }
        }else {
            return ServerResponse.creatByErrorMessage("参数错误");
        }
        return ServerResponse.creatBySuccess("校验成功");
    }

    public ServerResponse selectQuestion(String username){
//        int resultCount=userMapper.checkUsername(username);
        ServerResponse validResponse=this.checkValid(username,Const.USERNAME);
        if(validResponse.isSuccess()){
            //用户存在
            return ServerResponse.creatByErrorMessage("用户名不存在");
        }
        String question=userMapper.selectQuestionByUsername(username);
        if (StringUtils.isNoneBlank()){
            return ServerResponse.creatBySuccess(question);
        }
        return ServerResponse.creatByErrorMessage("找回密码为空");
    }
}