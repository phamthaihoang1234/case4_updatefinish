package com.example.demo.controllers;

import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.repository.IProductssRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
public class ProductsController {
    @Autowired
    IProductssRepository iProductssRepository;

    @Value("E:/Codegym123/file/")
    private String fileUpload;

    @Autowired
    ProductService productService;



    @GetMapping("/product")
    public String showPage(Model model, @RequestParam(defaultValue = "0") int page, @PageableDefault(value =3) Pageable pageable) {
//        model.addAttribute("data",
//                iProductssRepository.findBySellername(getPrincipal(),PageRequest.of(page, 4)));



        model.addAttribute("data",
               iProductssRepository.findBySellername(getPrincipal(),pageable));





//        model.addAttribute("data",productService.findAllBySellernameContain(getPrincipal()));
//        model.addAttribute("currentPage", page);
        return "shop";
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




    @PostMapping(value = "/saves")
//    @PostMapping(value = "/saves", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String save(@ModelAttribute Product product) {
        System.out.println("1");
        MultipartFile multipartFile = product.getImage();
        String fileName = multipartFile.getOriginalFilename();

        try {
            FileCopyUtils.copy(product.getImage().getBytes(), new File(fileUpload + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        product.setImgSrc(fileName);
        product.setSellername(getPrincipal());

        iProductssRepository.save(product);
        return "redirect:/product";
    }



    @GetMapping("/delete")
    public String deleteProduct(long id) {
//        List<Country> countries = new ArrayList<>();
//        countries = countryRepository.findAll();
//        for (long i=countries.size(); i>= (id+1); countries.size() ){
//            countryRepository.getOne(i).setId(i-1);
//        }
        iProductssRepository.deleteById(id);
        return "redirect:/product";
    }

    @GetMapping("/findOne")
    @ResponseBody
    public Product findOne(long id) {
        return iProductssRepository.findById(id).get();
    }


    @GetMapping("/produc/{id}")
    public String viewDetail(@PathVariable("id") Long id, Model model ){
        Product product = iProductssRepository.findById(id).get();
        model.addAttribute("produc",product);
        return "products_detal";
    }

}
