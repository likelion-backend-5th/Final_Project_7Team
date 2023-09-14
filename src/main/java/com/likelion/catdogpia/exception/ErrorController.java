package com.likelion.catdogpia.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class ErrorController {

    @RequestMapping("/admin/error-page/404")
    public String errorPage404(HttpServletRequest request, HttpServletResponse response){
        log.info("404-error");
        return "page/admin/error-page/404";
    }

    @RequestMapping("/admin/error-page/500")
    public String errorPage500(HttpServletRequest request, HttpServletResponse response){
        log.info("500-error");
        return "page/admin/error-page/500";
    }
}
