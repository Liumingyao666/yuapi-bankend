package com.example.yuapiinterface.controller;

import com.example.yuapiclientsdk.model.User;
import com.example.yuapiclientsdk.utils.SignUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 名称API
 *
 */
@RestController
@RequestMapping("/name")
public class NameController {

    @GetMapping("/get")
    public String getNameByGet(String name, HttpServletRequest request){
        System.out.println(request.getHeader("yupi"));
        return "GET 你的名字是" + name;
    }

    @PostMapping("/post")
    public String getNameByPost(@RequestParam String name){
        return "POST 你的名字是" + name;
    }

    @PostMapping("/user")
    public String getUsernameByPost(@RequestBody User user, HttpServletRequest request){
        //@todo 实际上是从数据库中查出来是否已分配给用户
//        String accessKey = request.getHeader("accessKey");
//        String nonce = request.getHeader("nonce");
//        String body = request.getHeader("body");
//        String sign = request.getHeader("sign");
//        String timestamp = request.getHeader("timestamp");
//        String secretKey = request.getHeader("secretKey");

//        if (!accessKey.equals("yupi")) {
//            throw new RuntimeException("无权限");
//        }
//
//        if (Long.parseLong(nonce) > 10000) {
//            throw new RuntimeException("无权限");
//        }
//
//        //@todo 实际上是从数据库中查出secret
//        String serverSign = SignUtils.getSign(body, "qwer");
//        if (!sign.equals(serverSign)){
//            throw new RuntimeException("无权限");
//        }
        return "POST 你的名字是" + user.getUsername();
    }

}
