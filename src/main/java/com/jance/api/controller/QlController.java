package com.jance.api.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jance.api.utils.QlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("v1/ql/")
public class QlController {
    private static final Logger log = LoggerFactory.getLogger(QlController.class);

    /**
     * 更新京东ck
     * @param in 京东ck信息
     *       {
     *          clientId-青龙应用id（支持自定义，不传则默认）
     *          clientSecret-青龙应用密钥（支持自定义，不传则默认）
     *          qlAddr-青龙应用地址（支持自定义，不传则默认）
     *          ck-京东ck
     *       })
     * @return 更新结果
     */
    @RequestMapping("updateJdCk")
    public String updateJdCk(@RequestBody Map<String, String> in) {
        log.info("进入更新京东ck接口");
        // 1.登录青龙
        String clientId = in.get("clientId");
        String clientSecret = in.get("clientSecret");
        String qlAddr = in.get("qlAddr");
        String auth = QlUtils.login(clientId, clientSecret, qlAddr);

        // 2.解析传来的ck
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

        // 3.获取环境变量
        JSONArray envList = QlUtils.getEnvs(auth, qlAddr);

        // 4.遍历出所有jdck
        JSONArray jdCKList = new JSONArray();
        for (int i = 0; i < envList.size(); i++) {
            JSONObject object = envList.getJSONObject(i);
            if (object.getString("name").equals("JD_COOKIE")) {
                jdCKList.add(object);
            }
        }

        // 5.新增/更新环境变量（已有pt_pin更新，没有pt_pin新增）
        for (int i = 0; i < jdCKList.size(); i++) {
            JSONObject object = jdCKList.getJSONObject(i);
            String value = object.getString("value");
            if (value.contains(pt_pin)) {
                // 已有pt_pin更新
                JSONObject envInfo = new JSONObject();
                envInfo.put("id", object.getString("id"));
                envInfo.put("name", object.getString("name"));
                envInfo.put("value", newCk);
                envInfo.put("remarks", object.getString("remarks"));
                QlUtils.updateEnv(auth, qlAddr, envInfo);
                log.info("更新ck成功");

                // ck更新成功后，若status状态为1，表示被禁用，需启用此变量
                if ("1".equals(object.getString("status"))) {
                    log.info("该ck被禁用，调用环境变量启用接口");
                    QlUtils.enableEnv(auth, qlAddr, envInfo);
                }
                return "更新ck成功";
            }
        }
        // 没有pt_pin新增
        JSONObject envInfo = new JSONObject();
        envInfo.put("name", "JD_COOKIE");
        envInfo.put("value", newCk);
        envInfo.put("remarks", pt_pin);
        QlUtils.insertEnv(auth, qlAddr, envInfo);
        log.info("新增ck成功");
        return "新增ck成功";
    }
}
