package com.spring.security.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.spring.security.entity.Product;


@Controller
public class AppController {

	@RequestMapping(value = {"/userpage"}, method = RequestMethod.GET)
    public String userPage(Model model) {

        if (isAdminPage())
            return adminPage(model);
//        Product product = new Product();
//        ModelAndView model = new ModelAndView("user","command",product);
//        model.addObject("title", "Spring Security Hello World");
//        model.addObject("user", getUser());
//        model.setViewName("user");
//        model.addObject("product", new Product());
        model.addAttribute("product", new Product());
        model.addAttribute("title", "Spring Security Hello World");
        model.addAttribute("user", getUser());
        return "user";
    }

//    @RequestMapping(value = "/adminpage", method = RequestMethod.GET)
//    public ModelAndView adminPage() {
//            ModelAndView model = new ModelAndView();
//            model.addObject("title", "Spring Security Hello World");
//            model.addObject("user", getUser());
//            model.setViewName("admin");
//            return model;
//    }
	
	@RequestMapping(value = "/adminpage", method = RequestMethod.GET)
    public String adminPage(Model model) {
//            Model model = new ModelAndView();
//            model.addObject("title", "Spring Security Hello World");
//            model.addObject("user", getUser());
//            model.setViewName("admin");
//            model.addAttribute("product", new Product());
            model.addAttribute("title", "Spring Security Hello World");
            model.addAttribute("user", getUser());
            return "admin";
    }
    
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public ModelAndView uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
    	ModelAndView model = new ModelAndView();
        model.addObject("title", "File Uploaded");
        model.addObject("user", file.getOriginalFilename());
        model.setViewName("uploadSuccess");
        return model;
    }
    
//    @RequestMapping(value = "/product-input-form")
//    public String inputProduct(Model model) {
//        model.addAttribute("product", new Product());
//        return "productForm";
//    }
    
    @RequestMapping("/save-product")
    public String uploadResources( HttpServletRequest servletRequest,
                                 @ModelAttribute(value = "product") Product product,
                                 Model model) throws IOException
    {
        //Get the uploaded files and store them
        List<MultipartFile> files = product.getImages();
        List<String> fileNames = new ArrayList<String>();
        if (null != files && files.size() > 0)
        {
            for (MultipartFile multipartFile : files) {
 
                String fileName = multipartFile.getOriginalFilename();
                fileNames.add(fileName);
                
                HttpSession session = servletRequest.getSession();       
                ServletContext sc = session.getServletContext();

 
//                File imageFile = new File(sc.getRealPath("/image"), fileName);
                String uploadPath = sc.getRealPath("/image") + File.separator + "temp" + File.separator;
                FileCopyUtils.copy(multipartFile.getBytes(), new File(uploadPath+multipartFile.getOriginalFilename()));
//                try
//                {
//                    multipartFile.transferTo(imageFile);
//                } catch (IOException e)
//                {
//                    e.printStackTrace();
//                }
            }
        }
        model.addAttribute("product", product);
        return "viewProductDetail";
    }


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
        ModelAndView model = new ModelAndView();
        model.addObject("title", "Login Page");
        model.setViewName("login");
        return model;
    }

//    @RequestMapping(value = "/logout", method = RequestMethod.GET)
//    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth != null) {
//            new SecurityContextLogoutHandler().logout(request, response, auth);
//        }
//        return login(request,response);
//    }

    @RequestMapping(value = {"/accessdenied"}, method = RequestMethod.GET)
    public ModelAndView accessDeniedPage() {
        ModelAndView model = new ModelAndView();
        model.addObject("message", "Either username or password is incorrect.");
        model.setViewName("accessdenied");
        return model;
    }
    
    @RequestMapping(value = {"/verifyOTP"}, method = RequestMethod.GET)
    public ModelAndView verifyOTP() {
        ModelAndView model = new ModelAndView();
        model.addObject("message", "going to send otp");
        model.setViewName("accessdenied");
        return model;
    }

    private String getUser() {
        String userName = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }


    private boolean isAdminPage() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            Collection<? extends GrantedAuthority> authorities = ((UserDetails) principal).getAuthorities();
            if (authorities.size() == 1) {
                final Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
                GrantedAuthority grantedAuthority = iterator.next();
                if (grantedAuthority.getAuthority().equals("ADMIN")) {
                    return true;
                }
            }
        }
        return false;
    }
}
