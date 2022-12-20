package com.laiyz.demo.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/sh")
public class ShellController {


    @GetMapping("/freespace")
    public String freeSpace(){
        try {
            Process process = Runtime.getRuntime().exec("df -h /");
            Thread.sleep(2000);
            return readResult(process.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/ls")
    public String ls(){
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("ls /mnt/www/video");
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return readResult(process.getInputStream());
    }

    @GetMapping("/rm")
    public String rm(@RequestParam("file")String file){
        if ("*".equals(file)||".".equals(file)||"/".equals(file)){
            return "error";
        }
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(String.format("rm -rf %s", file));
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return readResult(process.getInputStream());
    }

    @GetMapping("/ps")
    public String ps(){
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("ps -ef");
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return readResult(process.getInputStream());
    }

//    public static void main(String[] args) throws Exception{
//        Scanner scanner = new Scanner(System.in);
//        String nextCmd;
//        while (!"exit".contains(nextCmd = scanner.nextLine())){
//            try {
//                Process process = Runtime.getRuntime().exec(nextCmd);
//                InputStream inputStream = process.getInputStream();
//                int available = inputStream.available();
//                byte[] bytes = new byte[available];
//                inputStream.read(bytes);
//                System.out.println(new String(bytes,"utf-8"));
//            }catch (Exception e){e.printStackTrace();}
//        }
//    }

    private String readResult(InputStream inputStream){
        int available = 0;
        StringBuffer sbf = new StringBuffer();
        try {
            available = inputStream.available();
            byte[] bytes = new byte[available];
            inputStream.read(bytes);
            String result = new String(bytes, "utf-8");
            for (String s : result.split("\n")){
                sbf.append(s+"<br />");
            }
            return sbf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
