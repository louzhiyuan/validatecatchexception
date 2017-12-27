/*********************************************************************
 * 
 * CHINA TELECOM CORPORATION CONFIDENTIAL
 * ______________________________________________________________
 * 
 *  [2015] - [2020] China Telecom Corporation Limited, 
 *  All Rights Reserved.
 * 
 * NOTICE:  All information contained herein is, and remains
 * the property of China Telecom Corporation and its suppliers,
 * if any. The intellectual and technical concepts contained 
 * herein are proprietary to China Telecom Corporation and its 
 * suppliers and may be covered by China and Foreign Patents,
 * patents in process, and are protected by trade secret  or 
 * copyright law. Dissemination of this information or 
 * reproduction of this material is strictly forbidden unless prior 
 * written permission is obtained from China Telecom Corporation.
 **********************************************************************/

package com.example.demo.utils.DaDe;


import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 李超(l_chao@189.cn)
 */
public class TokenManager {

    private ConcurrentHashMap<String, String> token = new ConcurrentHashMap<String, String>();


    private TokenManager() {
    }

    private static TokenManager instance = new TokenManager();


    public static TokenManager getInstance() {
        return instance;
    }


    public String getToken() {
        if (token.size() > 0) {
            return token.get("token");
        } else {
            return "";
        }
    }

    public void putToken(String tokenID) {
        token.put("token", tokenID);
    }
}
