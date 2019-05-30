package com.sy.controller.common;

import com.sy.model.common.User;
import com.sy.service.common.MenuService;
import com.sy.service.common.UserService;
import com.sy.tools.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private MenuService menuService;

    //1.公告Service查询公告列表

    //2.资讯Service查询咨讯列表



    @RequestMapping("/")
    public String toLogin(){

        return "index";
    }

    @RequestMapping("/main.html")
    public String toMain(){

        return "main";
    }

    @RequestMapping("/login.html")
    @ResponseBody
    public String doLogin(User user, HttpSession session){

        try {
            User findUser = userService.getLoginUser(user);
            if(findUser!=null){
                session.setAttribute(Constants.SESSION_USER, findUser);
                //当前用户能够访问的菜单
                String json = menuService.makeMenus(findUser.getRoleId());
                session.setAttribute("menus", json);
                //当前用户能够访问的所有的url
                menuService.makeFunctions(findUser.getRoleId());
                return Constants.LOGIN_SUCCESS;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return Constants.LOGIN_FAILED;

    }


}
