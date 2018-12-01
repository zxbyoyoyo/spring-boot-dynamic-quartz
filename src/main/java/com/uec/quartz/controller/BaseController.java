package com.uec.quartz.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * <p>Copyright: All Rights Reserved</p>
 * <p>Company: 北京荣之联科技股份有限公司   http://www.ronglian.com</p>
 * <p>Description:  #{Description}</p>
 * <p>Author:朱晓兵</p>
 */
public class BaseController {

    public String MD5(String str) {
        if (StringUtils.isNotBlank(str)) {
            return DigestUtils.md5DigestAsHex(str.getBytes());
        }
        return null;
    }

    public Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        Map<String, String[]> params = request.getParameterMap();
        Set<Entry<String, String[]>> set = params.entrySet();
        Iterator<Entry<String, String[]>> it = set.iterator();
        while (it.hasNext()) {
            Entry<String, String[]> entry = it.next();
            String key = entry.getKey();
            String values[] = entry.getValue();
//			if (key.contains("[]")) {// 数组
//				key = key.replace("[]", "");
//			}
            // 剔除PageUtil里的参数
            if (!key.equals("page") && !key.equals("rows") && !key.equals("totalPage") && !key.equals("totalRow") && !key.equals("sort") && !key.equals("order") && !key.equals("params")) {
                if (values.length > 0) {
                    if (key.contains("[]")) {// 数组
                        key = key.replace("[]", "");
                        paramMap.put(key, values);
                    } else {
                        if (values.length == 1) {
                            paramMap.put(key, values[0]);
                        } else {
                            paramMap.put(key, values);
                        }
                    }
                }
            }
        }
        return paramMap;
    }

    public String getErrors(Exception e) {
        StringBuilder errors = new StringBuilder();
        StackTraceElement[] stes = e.getStackTrace();
        for (StackTraceElement ste : stes) {
            errors.append(ste.getClassName());
            errors.append("\n");
        }
        return errors.toString();
    }

    public void setHeader(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
        response.setHeader("refresh", "1");
    }
}
