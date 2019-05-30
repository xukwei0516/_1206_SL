package com.sy.interceptor;

import com.sy.model.common.User;
import com.sy.tools.Constants;
import com.sy.tools.RedisAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SysInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisAPI redisAPI;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String forwardUrl = request.getRequestURI();//想去访问的url

        //获取用户的角色,根据角色判断该用户是否具有访问某个url资源的权限.

        User sessionUser = (User)request.getSession().getAttribute(Constants.SESSION_USER);

        String key = "FUN"+sessionUser.getRoleId();

        String value = redisAPI.get(key);

        if(value.indexOf(forwardUrl)>=0){
            return true;
        }else{
            //没有相关权限,跳转401页面
            request.getRequestDispatcher("/WEB-INF/pages/401.jsp").forward(request, response);
            return false;
        }

    }
}
