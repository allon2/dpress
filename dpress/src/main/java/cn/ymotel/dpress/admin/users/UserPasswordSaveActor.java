package cn.ymotel.dpress.admin.users;

import cn.hutool.crypto.digest.BCrypt;
import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.Message;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.service.OptionsService;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import run.halo.app.exception.BadRequestException;
import run.halo.app.model.entity.User;

import java.util.HashMap;
import java.util.Map;

@ActorCfg(urlPatterns = "/api/admin/users/profiles/password",methods = RequestMethod.PUT)
public class UserPasswordSaveActor implements Actor<Message> {
    @Autowired
    private SqlSession sqlSession;

    @Override
    public Map Execute(Message message) throws Throwable {
        String oldPassword=message.getContextData("oldPassword");
        String newPassword=message.getContextData("newPassword");
        if (oldPassword.equals(newPassword)) {
            throw new BadRequestException("新密码和旧密码不能相同");
        }
        User  user=message.getUser();
        user.getId();
        {
            Map paramsMap=new HashMap();
            paramsMap.put("id",user.getId());
           String password= sqlSession.selectOne("users.qpassword",paramsMap);
            if (!BCrypt.checkpw(oldPassword, password)) {
                throw new BadRequestException("旧密码错误").setErrorData(oldPassword);
            }
        }
        {
            Map paramsMap=new HashMap();
            paramsMap.put("id",user.getId());
            String password = BCrypt.hashpw(message.getContextData("newPassword"), BCrypt.gensalt());
            paramsMap.put("password",password);
            sqlSession.update("users.upassword",paramsMap);
        }
        return new HashMap();
    }
}
