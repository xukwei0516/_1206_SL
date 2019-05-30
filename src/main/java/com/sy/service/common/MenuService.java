package com.sy.service.common;


import com.alibaba.fastjson.JSON;
import com.sy.model.common.Authority;
import com.sy.model.common.Function;
import com.sy.model.common.Menu;
import com.sy.tools.RedisAPI;
import jdk.nashorn.internal.runtime.JSONFunctions;
import org.hibernate.validator.internal.constraintvalidators.bv.time.future.FutureValidatorForYear;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuService {

    @Autowired
    private RedisAPI redisAPI;

    @Autowired
    private FunctionService functionService;

    @Autowired
    private UserService userService;


    /**
     * 根据登录用户的roleId返回可以访问菜单(主菜单和子菜单)
     *
     * 把List<Menu>放入redis中
     * @param roleId
     * @return
     */
    public String makeMenus(int roleId) throws Exception{

        String key = "MENUS"+roleId;
        String json = null;
        //3.判断redis缓存中是否有对应的key,如果有,则从redis中读取,否则从数据库中读取
        boolean flag = redisAPI.exist(key);
        if(!flag){
            //1.封装菜单信息
            List<Menu> list =  new ArrayList<>();
            Authority authority = new Authority();
            authority.setRoleId(roleId);
            //主菜单
            List<Function> mains = functionService.getMainFunctionList(authority);
            for(Function mainFun:mains){
                mainFun.setRoleId(roleId);
                Menu menu = new Menu();
                //封装主菜单
                menu.setMainFunction(mainFun);
                //当前主菜单对应的子菜单
                List<Function> subs = functionService.getSubFunctionList(mainFun);
                //封装子菜单
                menu.setSubsFunction(subs);
                list.add(menu);
            }

            //2.把菜单缓存在redis中
            json =JSON.toJSONString(list);
            redisAPI.set(key, json);
        }else{
            //从redis中读取
            json = redisAPI.get(key);
        }

        return json;
    }

    /**
     * 根据登录用户的roleId返回可以访问其他功能
     * 放入redis中
     * 把List<Functions>放入redis中
     * @param roleId
     * @return
     */
   public void makeFunctions(int roleId) throws Exception{

       String key = "FUN"+roleId;

       if(!redisAPI.exist(key)){
           Authority authority = new Authority();
           authority.setRoleId(roleId);
           List<Function> list = functionService.getFunctionListByRoId(authority);

           StringBuffer stringBuffer = new StringBuffer();
           //放入redis缓存
           for(Function function:list){
               String url = function.getFuncUrl();
               stringBuffer.append(url);
           }

           String value = stringBuffer.toString();
           redisAPI.set(key, value);
       }

    }

}
