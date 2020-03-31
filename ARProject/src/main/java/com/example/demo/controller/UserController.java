package com.example.demo.controller;


import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;


@Controller
public  class UserController {
    @Autowired
    private UserRepository userRepository;
    @GetMapping(path = "/register")
    public @ResponseBody String addNewUser (@RequestParam String username, @RequestParam String password, User user){
        List<User> users =userRepository.findByUsername(username);
        if (username=="")
        {
            return "用户名不能为空 ";
        }else
            {
            for (User u:users)
            {
                if (u.getUsername().equals(username))
                {
                    return "用户名已存在";
                }
            }
            if (password=="")
            {
                return "密码不能为空";
            }else
                {
                user.setUsename(username);
                user.setPassword(password);
                userRepository.save(user);
                return "success";
                }

            }

        }

    @GetMapping(path = "/login")
    public @ResponseBody String login(@RequestParam String username, @RequestParam String password, Model model){
        List<User> users =userRepository.findByUsername(username);
        //如果数据库中未查到该账号
        if (users.isEmpty()){
            return  "该用户不存在！请检查用户名是否正确！";
        }else {
            User user =users.get(0);
            if (password == ""){//密码为空
            return "密码不能为空！";
        }else {
            if (user.getPassword().equals(password)){
                model.addAttribute("username",user.getUsername());
                return username+"登录成功！";
            }else {
                model.addAttribute("username","fail");
                return "用户名或密码错误！";
            }
        }
        }

    }
    @GetMapping(path = "/updatePassword")
    public @ResponseBody String updatePassword(@RequestParam String password,@RequestParam String newpassword,@RequestParam String username){
        List<User> users =userRepository.findByUsername(username);

        //如果数据库中未查到该账号
        if (users.isEmpty()){
            return  "该用户不存在";
        }else {
            User user =users.get(0);
            if (password =="") {
                return "原密码不能为空！";
            }
            if (newpassword =="") {
                return "新密码不能为空！";
            }
            if (password.equals(newpassword)){
                return "新密码不能与原密码相同！";
            }
            if (user.getPassword().equals(password)){
                userRepository.delete(user);
                user.setPassword(newpassword);
                userRepository.save(user);
                return "修改密码成功！";
            }else {
                return "原密码不正确！";
            }
        }
        
    }

}
