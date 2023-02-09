package com.develonity.user.controller;

import com.develonity.user.dto.LogInRequest;
import com.develonity.user.dto.SignupRequest;
import com.develonity.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;
    //private final UserDetailsServiceImpl userDetailsServiceImpl;

    @PostMapping("/signup")
    public String signUp(@RequestBody @Valid SignupRequest signupRequest, Errors errors, Model model) {

        if(errors.hasErrors()){
            model.addAttribute("signup", signupRequest);

            //Map<String, String> errorMap = userDetailsServiceImpl.validateHandling(errors);
            //for(String key : errorMap.keySet()) {
                //model.addAttribute(key, errorMap.get(key));
            }

            return "/signup";
        }

        userService.signUp(signup);
        return "회원가입성공";
    }

    @PostMapping("/login")
    public String logIn(@RequestBody LogInRequest logInRequest, HttpServletResponse response){
        userService.login(logInRequest, response);
        return "로그인성공";
    }

    @GetMapping("/signup")
    public ModelAndView signup() {
        return new ModelAndView("signup");
    }

    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    //내가쓴 댓글조회(comment), 내가 쓴 게시물 조회(board)
}
