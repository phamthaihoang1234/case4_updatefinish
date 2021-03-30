package com.example.demo.controllers;

import com.example.demo.common.GooglePojo;
import com.example.demo.common.GoogleUtils;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.service.OrderSession.OrderService;
import com.example.demo.service.product.ProductService;
import com.example.demo.service.role.IRoleService;
import com.example.demo.service.user.IUserService;
import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class SecurityController {
    @Autowired
    IUserService userService;

    @Autowired
    private GoogleUtils googleUtils;

    @Autowired
    IRoleService roleService;

    @Autowired
    OrderService orderService;

    @Autowired
    ProductService productService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String homepage(Model model){
        model.addAttribute("productss", productService.findAll());
        return "index";
    }

    @RequestMapping("/login-google")
    public String loginGoogle(HttpServletRequest request) throws ClientProtocolException, IOException {
        String code = request.getParameter("code");

        if (code == null || code.isEmpty()) {
            return "redirect:/login?google=error";
        }

        String accessToken = googleUtils.getToken(code);

        GooglePojo googlePojo = googleUtils.getUserInfo(accessToken);
        UserDetails userDetail = googleUtils.buildUser(googlePojo);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetail, null,
                userDetail.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return "redirect:/";
    }

    @GetMapping("/shop")
    public String createshop(){
        return "shop";
    }

    @GetMapping("/account")
    public String accountPage(){
        return "account";
    }

    @GetMapping("/cart")
    public String cartPage(){
        return "carts";
    }

    @GetMapping("/products")
    public String productsPage(Model model){
        model.addAttribute("productss", productService.findAll());
        return "products";
    }

    @GetMapping("/products_detal")
    public String products_detalPage(){
        return "products_detal";
    }



    @PostMapping("/save")
    public String save(@ModelAttribute("user") User user){
        List<Role> roles = (List<Role>) roleService.findAll();

        Role roleUser = new Role();
        roleUser.setName("ROLE_USER");
        roleService.save(roleUser);


        String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(password));

        Set<Role> roles2 = new HashSet<>();
        roles2.add(roleUser);
        user.setRoles(roles2);
        userService.save(user);

        return "redirect:/";
    }

    @PostMapping("/save2")
    public String saveSeller(@ModelAttribute("seller") User seller){
//        List<Role> roles = (List<Role>) roleService.findAll();

        Role roleSeller = new Role();
        roleSeller.setName("ROLE_SELLER");
        roleService.save(roleSeller);


        String password = seller.getPassword();
        seller.setPassword(passwordEncoder.encode(password));

        Set<Role> roles2 = new HashSet<>();
        roles2.add(roleSeller);
        seller.setRoles(roles2);
        userService.save(seller);



        return "redirect:/";
    }



    @GetMapping("/register")
    public String registerForm(Model model){
        model.addAttribute("user",new User());
        return "register";
    }

    @GetMapping("/registerSeller")
    public String registerFormSeller(Model model){
        model.addAttribute("seller",new User());
        return "registerSeller";
    }



    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/logout")
    public String logout(){return "/";}





}
