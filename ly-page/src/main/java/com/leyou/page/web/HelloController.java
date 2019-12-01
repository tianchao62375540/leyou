package com.leyou.page.web;

import com.leyou.page.page.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;

/**
 * @Auther: tianchao
 * @Date: 2019/12/1 00:38
 * @Description:
 */
@Deprecated
@Controller
public class HelloController {

    @GetMapping("hello")
    public String toHello(Model model){
        User user = new User();
        user.setAge(21);
        user.setName("tian chao");
        user.setFriend(new User("李小龙",33,null));
        model.addAttribute("user",user);
        User user1 = new User("liSi", 22, null);
        model.addAttribute("users", Arrays.asList(user,user1));
        return "hello";
    }
}
