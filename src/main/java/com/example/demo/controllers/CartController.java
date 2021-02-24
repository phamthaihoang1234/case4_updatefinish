package com.example.demo.controllers;


import com.example.demo.model.Cart;

import com.example.demo.model.Order_Session;
import com.example.demo.model.Product;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.OrderSession.OrderService;
import com.example.demo.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.jws.WebParam;
import java.util.List;

@RequestMapping("/carts")
@Controller
@SessionAttributes("carts")
public class CartController {

    int check =0;



    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderService orderService;

    @Autowired
    ProductService productService;

    @ModelAttribute("carts")
    public Cart getCart(){return new Cart();
    }

    @GetMapping
    public String showCart(@ModelAttribute("carts") Cart cart, Model model){
        if(check == 1){
            model.addAttribute("message", "New products was added succeeded!!!");
        }else if(check == 2){
            model.addAttribute("message", "Product was deleted succeeded!!!");
        }else if(check ==3) {
            model.addAttribute("message", "You have successfully purchased");
        }else {
            model.addAttribute("message", "");
        }

        return "carts";
    }

    @GetMapping("/{id}")
    public String orderProduct(@PathVariable("id") Long id , @ModelAttribute("carts") Cart cart, Model model){
        Product product = productService.findById(id).get();
        Order_Session itemLine = cart.getOrderSession(product);
        itemLine.setQuantity(itemLine.getQuantity()+1);
        if(itemLine.getQuantity()>=1){
            check = 1;
        }else {
            check = 0;
        }

        return "redirect:/carts";

    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id, @ModelAttribute("carts") Cart cart, Model model){

        Product product = productService.findById(id).get();
        if(cart.getOrderSession(product).getQuantity() > 1){
            cart.getOrderSession(product).setQuantity(cart.getOrderSession(product).getQuantity()-1);
            check = 2;
        }else{
            cart.removeOrderSessionByProduct(product);
            check = 2;
        }



        return "redirect:/carts";
    }


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

    @GetMapping("/order")
    public String orderConfirm(@ModelAttribute("carts") Cart cart, Model model){

        List<Order_Session> orderSessions = cart.getOrderSessions();

        for(Order_Session order_session: orderSessions){
            order_session.setUsername(getPrincipal());
            order_session.setNameOfProduct(order_session.getProduct().getName());
            order_session.setQuantity(order_session.getQuantity());
            order_session.setPrice(order_session.getProduct().getPrice());
            order_session.setSubtotal(order_session.getPrice()*order_session.getQuantity());
            order_session.setSellername(order_session.getProduct().getSellername());
            orderService.save(order_session);
            check = 3;
        }
        return "redirect:/carts";

    }

//    @GetMapping("/deleteorder")
//    public String deleteOrder(){
//
//        orderService.deleteOrder_SessionsByUsername(getPrincipal());
//        return "redirect:/carts";
//
//
//    }

}
