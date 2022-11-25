package com.laiyz.demo.controller;

import com.laiyz.demo.utils.IpUtil;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/demo")
public class GetAccessIpController {

    @GetMapping("/echoIp")
    public ResponseEntity<String> echoIp(HttpServletRequest request){
        String ipAddr = IpUtil.getIpAddr(request);
        Instant instant = Instant.now();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneOffset.of("+8"));
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String accessTime = localDateTime.format(dateTimeFormatter);
        System.out.println(String.format("%s : %s", accessTime, ipAddr));
        return new ResponseEntity<>(ipAddr, HttpStatus.OK);
    }

}
