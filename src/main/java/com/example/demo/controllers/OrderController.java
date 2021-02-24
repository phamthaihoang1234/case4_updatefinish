package com.example.demo.controllers;


import com.example.demo.model.Order_Session;
import com.example.demo.service.OrderSession.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;


    private String getPrincipal(){
        String userName = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails)principal).getUsername();

        } else {
            userName = principal.toString();
        }
        return userName;
    }

    @GetMapping
    public String order(Model model){
       List<Order_Session> orderSessions = orderService.findAllBySellernameContains(getPrincipal());
       model.addAttribute("orders", orderSessions);
        return "/order";
    }

}
