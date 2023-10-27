package com.jance.api.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 青龙工具类
 */
public class QlUtils {
    private static final Logger log = LoggerFactory.getLogger(QlUtils.class);

    public static String QL_ADDR = "http://ql.jancehui.top";
    public static String CLIENT_ID = "Y4f70zeZBMg_";
    public static String CLIENT_SECRET = "Bj53k_i1Zc4YIm8XqE_7D1kh";

    /**
     * 登录青龙面板
     * @param clientId 青龙面板应用id（未传则使用默认值）
     * @param clientSecret 青龙面板应用密钥（未传则使用默认值）
     * @param qlAddr 青龙面板地址（未传则使用默认值）
     * @return 登录成功后的token
     */
    public static String login(String clientId, String clientSecret, String qlAddr) {
        log.info("进入登录青龙面板接口");
        if (StrUtil.isNotEmpty(clientId)) {
            log.info("使用自定义青龙面板应用id：" + clientId);
        } else {
            log.info("使用默认青龙面板应用id：" + CLIENT_ID);
        }
        if (StrUtil.isNotEmpty(clientSecret)) {
            log.info("使用自定义青龙面板应用密钥：" + clientSecret);
        } else {
            log.info("使用默认青龙面板应用密钥：" + CLIENT_SECRET);
        }
        if (StrUtil.isNotEmpty(qlAddr)) {
            log.info("使用自定义青龙面板地址：" + qlAddr);
        } else {
            log.info("使用默认青龙面板地址：" + QL_ADDR);
        }
        // 登录url
        String qlAddrUrl = StrUtil.isNotEmpty(qlAddr) ? qlAddr : QL_ADDR;
        String loginUrl = qlAddrUrl + "/open/auth/token";
        // 请求参数
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("client_id", StrUtil.isNotEmpty(clientId) ? clientId : CLIENT_ID);
        paramMap.put("client_secret", StrUtil.isNotEmpty(clientSecret) ? clientSecret : CLIENT_SECRET);
        // 发请求
        String result = HttpUtil.get(loginUrl, paramMap);
        // 解析结果，获取auth
        JSONObject resultObj = JSON.parseObject(result);
        JSONObject resultData = resultObj.getJSONObject("data");
        String auth = resultData.getString("token_type")
                + " " + resultData.getString("token");
        log.info("青龙面板登录成功，auth：" + auth);
        return auth;
    }

    /**
     * 获取环境变量
     * @param auth 登录成功后返回的auth
     * @param qlAddr 青龙面板地址（未传则使用默认值）
     * @return 所有环境变量(id、name、value、remarks、status、createdAt、updatedAt、position、timestamp)
     */
    public static JSONArray getEnvs(String auth, String qlAddr) {
        log.info("进入青龙面板获取环境变量接口");
        if (StrUtil.isNotEmpty(qlAddr)) {
            log.info("使用自定义青龙面板地址：" + qlAddr);
        } else {
            log.info("使用默认青龙面板地址：" + QL_ADDR);
        }
        // 获取环境变量url
        String qlAddrUrl = StrUtil.isNotEmpty(qlAddr) ? qlAddr : QL_ADDR;
        String getEnvsUrl = qlAddrUrl + "/open/envs?searchValue=";
        // 发请求
        String result = HttpRequest.get(getEnvsUrl)
                .header("Authorization", auth)
                .execute().body();
        // 解析结果，获取环境变量列表
        System.out.println(result);
        JSONObject resultObj = JSON.parseObject(result);
        JSONArray envList = resultObj.getJSONArray("data");
        log.info("青龙面板获取环境变量成功");
        return envList;
    }

    /**
     * 新增环境变量
     * @param auth 登录成功后返回的auth
     * @param qlAddr 青龙面板地址（未传则使用默认值）
     * @param envInfo 环境变量信息（name、value、remarks）
     * @return 新增后的环境变量信息
     */
    public static JSONArray insertEnv(String auth, String qlAddr, JSONObject envInfo) {
        log.info("进入青龙面板新增环境变量接口");
        if (StrUtil.isNotEmpty(qlAddr)) {
            log.info("使用自定义青龙面板地址：" + qlAddr);
        } else {
            log.info("使用默认青龙面板地址：" + QL_ADDR);
        }
        // 新增环境变量url
        String qlAddrUrl = StrUtil.isNotEmpty(qlAddr) ? qlAddr : QL_ADDR;
        String insertEnvsUrl = qlAddrUrl + "/open/envs";
        // 请求参数
        JSONArray envInfos = new JSONArray();
        envInfos.add(envInfo);
        // 发请求
        String result = HttpRequest.post(insertEnvsUrl)
                .header("Authorization", auth)
                .header("content-type", "application/json")
                .body(JSON.toJSONString(envInfos))
                .execute().body();
        // 解析结果
        JSONObject resultObj = JSON.parseObject(result);
        JSONArray envList = resultObj.getJSONArray("data");
        log.info("青龙面板新增环境变量成功");
        return envList;
    }

    /**
     * 更新环境变量
     * @param auth 登录成功后返回的auth
     * @param qlAddr 青龙面板地址（未传则使用默认值）
     * @param envInfo 环境变量信息（id、name、value、remarks）
     * @return 更新后的环境变量信息
     */
    public static JSONObject updateEnv(String auth, String qlAddr, JSONObject envInfo) {
        log.info("进入青龙面板更新环境变量接口");
        if (StrUtil.isNotEmpty(qlAddr)) {
            log.info("使用自定义青龙面板地址：" + qlAddr);
        } else {
            log.info("使用默认青龙面板地址：" + QL_ADDR);
        }
        // 更新环境变量url
        String qlAddrUrl = StrUtil.isNotEmpty(qlAddr) ? qlAddr : QL_ADDR;
        String updateEnvsUrl = qlAddrUrl + "/open/envs";
        // 请求参数
        JSONObject param = new JSONObject();
        param.put("id", envInfo.get("id"));
        param.put("name", envInfo.get("name"));
        param.put("value", envInfo.get("value"));
        param.put("remarks", envInfo.get("remarks"));
        // 发请求
        String result = HttpRequest.put(updateEnvsUrl)
                .header("Authorization", auth)
                .header("content-type", "application/json")
                .body(JSON.toJSONString(param))
                .execute().body();
        // 解析结果
        JSONObject resultObj = JSON.parseObject(result);
        JSONObject envObj = resultObj.getJSONObject("data");
        log.info("青龙面板更新环境变量成功");
        return envObj;
    }

    /**
     * 删除环境变量
     * @param auth 登录成功后返回的auth
     * @param qlAddr 青龙面板地址（未传则使用默认值）
     * @param envInfo 环境变量信息(id)
     */
    public static void deleteEnv(String auth, String qlAddr, JSONObject envInfo) {
        log.info("进入青龙面板删除环境变量接口");
        if (StrUtil.isNotEmpty(qlAddr)) {
            log.info("使用自定义青龙面板地址：" + qlAddr);
        } else {
            log.info("使用默认青龙面板地址：" + QL_ADDR);
        }
        // 更新环境变量url
        String qlAddrUrl = StrUtil.isNotEmpty(qlAddr) ? qlAddr : QL_ADDR;
        String deleteEnvsUrl = qlAddrUrl + "/open/envs";
        // 请求参数
        List<String> paramList = new ArrayList<>();
        paramList.add(envInfo.getString("id"));
        // 发请求
        String result = HttpRequest.delete(deleteEnvsUrl)
                .header("Authorization", auth)
                .header("content-type", "application/json")
                .body(JSON.toJSONString(paramList))
                .execute().body();
        // 解析结果
        log.info("青龙面板删除环境变量成功");
    }

    /**
     * 启用环境变量
     * @param auth 登录成功后返回的auth
     * @param qlAddr 青龙面板地址（未传则使用默认值）
     * @param envInfo 环境变量信息(id)
     */
    public static void enableEnv(String auth, String qlAddr, JSONObject envInfo) {
        log.info("进入青龙面板启用环境变量接口");
        if (StrUtil.isNotEmpty(qlAddr)) {
            log.info("使用自定义青龙面板地址：" + qlAddr);
        } else {
            log.info("使用默认青龙面板地址：" + QL_ADDR);
        }
        // 更新环境变量url
        String qlAddrUrl = StrUtil.isNotEmpty(qlAddr) ? qlAddr : QL_ADDR;
        String enableEnvsUrl = qlAddrUrl + "/open/envs/enable";
        // 请求参数
        List<String> paramList = new ArrayList<>();
        paramList.add(envInfo.getString("id"));
        // 发请求
        String result = HttpRequest.put(enableEnvsUrl)
                .header("Authorization", auth)
                .header("content-type", "application/json")
                .body(JSON.toJSONString(paramList))
                .execute().body();
        // 解析结果
        log.info("青龙面板启用环境变量成功");
    }

    /**
     * 禁用环境变量
     * @param auth 登录成功后返回的auth
     * @param qlAddr 青龙面板地址（未传则使用默认值）
     * @param envInfo 环境变量信息(id)
     */
    public static void disableEnv(String auth, String qlAddr, JSONObject envInfo) {
        log.info("进入青龙面板禁用环境变量接口");
        if (StrUtil.isNotEmpty(qlAddr)) {
            log.info("使用自定义青龙面板地址：" + qlAddr);
        } else {
            log.info("使用默认青龙面板地址：" + QL_ADDR);
        }
        // 更新环境变量url
        String qlAddrUrl = StrUtil.isNotEmpty(qlAddr) ? qlAddr : QL_ADDR;
        String disableEnvsUrl = qlAddrUrl + "/open/envs/disable";
        // 请求参数
        List<String> paramList = new ArrayList<>();
        paramList.add(envInfo.getString("id"));
        // 发请求
        String result = HttpRequest.put(disableEnvsUrl)
                .header("Authorization", auth)
                .header("content-type", "application/json")
                .body(JSON.toJSONString(paramList))
                .execute().body();
        // 解析结果
        log.info("青龙面板禁用环境变量成功");
    }
}
