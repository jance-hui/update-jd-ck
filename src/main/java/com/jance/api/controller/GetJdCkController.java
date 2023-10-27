package com.jance.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 剪切京东ck（值来自于手机alook浏览器提取）
 */
@RestController
@RequestMapping("v1/getJdCk/")
public class GetJdCkController {
    private static final Logger log = LoggerFactory.getLogger(GetJdCkController.class);


    @RequestMapping("getCk")
    public String getCk(@RequestBody Map<String, String> in) {
        log.info("进入剪切京东ck接口");
        String cks = in.get("ck");
        String pt_key = "";
        String pt_pin = "";
        String[] temp = cks.split(";");
        for (String s : temp) {
            s = s.trim();
            if (s.startsWith("pt_key=")) {
                pt_key = s;
            }
        }
        for (String s : temp) {
            s = s.trim();
            if (s.startsWith("pt_pin=")) {
                pt_pin = s;
            }
        }
        String newCk = pt_key + "; " + pt_pin + ";";
        log.info("剪切京东ck完成，" + newCk);
        return newCk.trim();
    }
}
