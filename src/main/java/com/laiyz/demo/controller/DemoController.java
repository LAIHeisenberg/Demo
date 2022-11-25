package com.laiyz.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Random;

@RestController
@RequestMapping("/demo")
public class DemoController {

    private static String action = null;
    private static Long accessTimestamp = null;
    private static String scriptRunStatus = null;
    private static Long scriptStartTimestamp = null;
    private static StringBuffer accessTimeLog = new StringBuffer();
    private static boolean workOnWeekend = false;

    @GetMapping("/getTimestamp")
    public Long getTimestamp(){
        return Instant.now().getEpochSecond();
    }

    @GetMapping("/getTargetTimestamp")
    public Long getTargetTimestamp(){
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.of("+8"));
        int hour = localDateTime.getHour();
        int year = localDateTime.getYear();
        Month month = localDateTime.getMonth();
        int dayOfMonth = localDateTime.getDayOfMonth();
        LocalDateTime targetLocalDateTime;
        if (hour > 9){
            targetLocalDateTime = LocalDateTime.of(year, month.getValue(), dayOfMonth + 1, 8, 55, 0);
        }else{
            targetLocalDateTime = LocalDateTime.of(year, month.getValue(), dayOfMonth, 8, 55, 0);
        }
        long l = targetLocalDateTime.toEpochSecond(ZoneOffset.of("+8"));
        return l;
    }

    @GetMapping("/signIn")
    public String signIn(){
        action = "signIn";
        return action;
    }

    @GetMapping("/getAction")
    public String getAction(){
        Long begin = Instant.now().getEpochSecond();
        Random random = new Random(System.currentTimeMillis());
        try {
            Thread.sleep(random.nextInt(35) * 1000);
        } catch (InterruptedException e) {}
        accessTimestamp = Instant.now().getEpochSecond();
        Instant instant = Instant.ofEpochSecond(accessTimestamp);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneOffset.of("+8"));
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String accessTime = localDateTime.format(dateTimeFormatter);
        accessTimeLog.insert(0,accessTime+"<br />");
        DayOfWeek dayOfWeek = localDateTime.getDayOfWeek();
        if (!workOnWeekend && (DayOfWeek.SATURDAY == dayOfWeek || DayOfWeek.SUNDAY == dayOfWeek)){
            return null;
        }
        Long end = Instant.now().getEpochSecond();
        System.out.println("响应耗时："+(end-begin));
        return action;
    }

    @GetMapping("/clean")
    public void clean(){
        action = null;
        accessTimestamp = null;
        scriptRunStatus = null;
        scriptStartTimestamp = null;
        workOnWeekend = false;
        accessTimeLog = new StringBuffer();
    }

    @GetMapping("/workOnWeekend")
    public boolean workOnWeekend(){
        this.workOnWeekend = true;
        return workOnWeekend;
    }

    @GetMapping("/getAccessTime")
    public String getAccessTime(){
        Instant instant = Instant.ofEpochSecond(accessTimestamp);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneOffset.of("+8"));
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(dateTimeFormatter);
    }

    @GetMapping("/getAccessTimeLog")
    public String getAccessTimeLog(){
        return accessTimeLog.toString();
    }

    @GetMapping("/startScript")
    public void startScript(){
        scriptRunStatus = "Running";
        scriptStartTimestamp = Instant.now().getEpochSecond();
    }

    @GetMapping("/getScriptRunStatus")
    public String getScriptRunStatus(){
        Instant instant = Instant.ofEpochSecond(scriptStartTimestamp);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneOffset.of("+8"));
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return scriptRunStatus + " " + localDateTime.format(dateTimeFormatter);
    }

    @GetMapping("/listCmd")
    public String listCmd(){
        StringBuffer sbf = new StringBuffer();
        for (Method method : this.getClass().getMethods()){
            GetMapping annotation = method.getAnnotation(GetMapping.class);
            if (Objects.nonNull(annotation)){
                sbf.append(annotation.value()[0]+"<br />");
            }
        }
        return sbf.toString();
    }

}
