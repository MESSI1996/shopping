package com.tz.service.impl;
import com.tz.common.Const;
import com.tz.common.RedisPool;
import com.tz.common.ServerResponse;
import com.tz.dao.UserInfoMapper;
import com.tz.pojo.UserInfo;
import com.tz.service.IUserService;

import com.tz.utils.MD5Utils;
import com.tz.utils.RedisPoolUtils;
import com.tz.utils.TokenCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService implements IUserService {

    @Autowired
    UserInfoMapper userInfoMapper;


    @Override
    public ServerResponse login(String username, String password) {
        //1：参数的非空校验
        if (username==null||username.equals("")){
            return ServerResponse.serverResponseByError("用户名不能为空");

        }
        if (password==null||password.equals("")){
            return ServerResponse.serverResponseByError("密码不能为空");

        }

        //2：检查用户是否存在
        int result=userInfoMapper.checkUsername(username);
        if (result==0){
            return ServerResponse.serverResponseByError("用户名不存在");
        }
        //3：根据用户名和密码查询信息
        UserInfo userInfo=userInfoMapper.selectUserByUsernameAndPassword(username,MD5Utils.getMD5Code(password));
        if (userInfo==null){
            return  ServerResponse.serverResponseByError("密码错误");
        }
        //4：返回结果
        return ServerResponse.serverResponseBySuccess(userInfo);
    }

    @Override
    public ServerResponse register(UserInfo userInfo) {
        //1：参数的非空校验

        if (userInfo == null || userInfo.equals("")) {
            return ServerResponse.serverResponseByError("参数必须");}

            //2：检查用户是否存在
            int result = userInfoMapper.checkUsername(userInfo.getUsername());
            if (result>0) {
                return ServerResponse.serverResponseByError("用户名存在");
            }

            //2：检查邮箱是否存在
            int result_emal = userInfoMapper.checkEmail(userInfo.getEmail());
            if (result_emal>0) {
                return ServerResponse.serverResponseByError("邮箱存在");
            }

            //4：注册
            userInfo.setRole(Const.RoleEnum.ROLE_CUSTOMER.getCode());
            userInfo.setPassword(MD5Utils.getMD5Code(userInfo.getPassword()));
          int count=  userInfoMapper.insert(userInfo);

            //返回结果
            if (count>0){
                return  ServerResponse.serverResponseBySuccess("注册成功");
            }
            return ServerResponse.serverResponseByError("注册失败");
        }

    @Override
    public ServerResponse forgot_get_question(String username) {
       //1:参数校验
        if (username==null||username.equals("")){
            return  ServerResponse.serverResponseByError("用户名不能为空");
        }
        //2:校验用户名
        int result=userInfoMapper.checkUsername(username);
        if (result==0){
            return ServerResponse.serverResponseByError("用户名不存在");
        }

        //3:查找密保问题
        String question=userInfoMapper.selectQuestionByUsername(username);
        if (question==null||question.equals("")){
            return  ServerResponse.serverResponseByError("密保问题为空");
        }
        return  ServerResponse.serverResponseBySuccess(question);

    }

    @Override
    public ServerResponse forgot_check_answer(String username, String question, String answer) {
       //1:参数校验
        if (username==null||username.equals("")){
            return  ServerResponse.serverResponseByError("用户名不能为空");
        }
        if (question==null||question.equals("")){
            return  ServerResponse.serverResponseByError("问题不能为空");
        }
        if (answer==null||answer.equals("")){
            return  ServerResponse.serverResponseByError("答案不能为空");
        }
        //2：根据username，question，answer查询
       int result= userInfoMapper.selectByUsernameAndQuestionAndAnswer(username,question,answer);
        if (result==0){//答案错误
           return ServerResponse.serverResponseByError("答案错误");
        }
        //3：服务端生成一个token保存并返回给客户端
        String forgettoken=UUID.randomUUID().toString();
        //guava ache
        //TokenCache.set(username,forgettoken);

        RedisPoolUtils.set(username,forgettoken);


        return ServerResponse.serverResponseBySuccess(forgettoken);
    }

    @Override
    public ServerResponse forgot_reset_password(String username, String passwordNEW, String forgettoken) {
        //1:参数校验
        if (username==null||username.equals("")){
            return  ServerResponse.serverResponseByError("用户名不能为空");
        }
        if (passwordNEW==null||passwordNEW.equals("")){
            return  ServerResponse.serverResponseByError("问题不能为空");
        }
        if (forgettoken==null||forgettoken.equals("")){
            return  ServerResponse.serverResponseByError("答案不能为空");
        }

        //2：token校验
        //String token=TokenCache.get(username);

        String token=RedisPoolUtils.get(username);
        if (token==null){
            ServerResponse.serverResponseByError("token过期");
        }
        if (!token.equals(forgettoken)){
            ServerResponse.serverResponseByError("无效token");
        }
        //3：修改密码
        int result=userInfoMapper.updateUserPassword(username,MD5Utils.getMD5Code(passwordNEW));

        if (result>0){
            return  ServerResponse.serverResponseBySuccess("修改成功");
        }
        return  ServerResponse.serverResponseByError("修改失败");
    }

    @Override
    public ServerResponse check_valid(String str, String type) {
        //1:参数非空校验

        if (str == null || str.equals("")) {
            ServerResponse.serverResponseByError("用户名或者邮箱不能为空");
        }
        if (type == null || type.equals("")) {
            ServerResponse.serverResponseByError("娇艳的参数类型不能为空");
        }


        //2：校验用户名和校验邮箱
        if (type.equals("username")) {
            int result = userInfoMapper.checkUsername(str);
            if (result > 0) {
               return ServerResponse.serverResponseByError("用户已经存在");
            } else {
               return ServerResponse.serverResponseBySuccess();
            }
        } else if (type.equals("email")) {
            int result = userInfoMapper.checkEmail(str);
            if (result > 0) {
              return   ServerResponse.serverResponseByError("邮箱已经存在");
            } else {
             return    ServerResponse.serverResponseBySuccess();
            }
        } else {
            return ServerResponse.serverResponseByError("参数类型错误");
        }

    }

    @Override
    public ServerResponse reset_password(String username,String passworOLD, String passwordNEW) {
       //1：参数的非空校验
        if (username==null||username.equals("")){
            return  ServerResponse.serverResponseByError("用户名不能为空");
        }
        if (passworOLD==null||passworOLD.equals("")){
            return  ServerResponse.serverResponseByError("旧密码不能为空");
        }
        if (passwordNEW==null||passwordNEW.equals("")){
            return  ServerResponse.serverResponseByError("新密码不能为空");
        }

        //2：根据usernam和passwordold查找哦用户
        UserInfo userInfo=userInfoMapper.selectUserByUsernameAndPassword(username,MD5Utils.getMD5Code(passworOLD));
        if (userInfo == null) {
            return  ServerResponse.serverResponseByError("密码错误");
        }

        //3:查到用户后修改密码
        userInfo.setPassword(MD5Utils.getMD5Code(passwordNEW));
        int result=userInfoMapper.updateByPrimaryKey(userInfo);
        if (result>0){
            return  ServerResponse.serverResponseBySuccess("修改成功");
        }

        return ServerResponse.serverResponseByError("修改失败");
    }

    @Override
    public ServerResponse update_information(UserInfo user) {
        //1:参数校验
        if (user==null){
            return  ServerResponse.serverResponseByError("参数不能为空");
        }

        //2：跟新用户信息
        int result=userInfoMapper.updateUserBySelectActive(user);
        if (result>0){
            return  ServerResponse.serverResponseBySuccess();

        }

        //3：

        return ServerResponse.serverResponseByError("更新用户信息失败");
    }

    @Override
    public UserInfo findUserInfoByUserid(Integer id) {
     return    userInfoMapper.selectByPrimaryKey(id);


    }

    @Override
    public int updateTokenByUserId(Integer userId, String token) {


        return userInfoMapper.updateTokenByUserId(userId,token);
    }

    @Override
    public UserInfo findUserInfoByToken(String token) {
        if (token==null||token.equals("")){
            return  null;
        }

        return userInfoMapper.findUserInfoByToken(token);
    }

}
