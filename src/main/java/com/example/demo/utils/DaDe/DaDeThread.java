package com.example.demo.utils.DaDe;

import com.bfd.util.MD5Utils;
import com.piccfs.dt.apiadpter.common.DaDeConstants;
import com.piccfs.dt.apiadpter.utils.httpclient.HttpGetUtils;
import net.sf.json.JSONObject;

public class DaDeThread implements Runnable{


    @Override
    public void run() {
        String token = getToken();
        TokenManager.getInstance().putToken(token);

    }

    //获取authCode
    public String authCode(){
        StringBuilder sb = new StringBuilder();
        sb.append(DaDeConstants.protocol).append(DaDeConstants.HOST).append(":").append(DaDeConstants.PORT);
        sb.append("/restful/system/publicKey.json?apiKey="+DaDeConstants.APIKEY);
        //解析得到authCode
        String aCode = HttpGetUtils.getResponseContext(sb.toString());
        JSONObject auth = JSONObject.fromObject(aCode);
        //返回的结果截取data
        String data = auth.getString("data");
        return data;
    }
    //生成sign  apikey+password+authCode
    public String getSign(){

        return MD5Utils.genMd5(DaDeConstants.APIKEY.concat(DaDeConstants.SECRETKEY).concat(authCode()));
    }
    //获取tokenID
    public String getToken(){
        StringBuilder token = new StringBuilder();
        token.append(DaDeConstants.protocol).append(DaDeConstants.HOST).append(":").append(DaDeConstants.PORT);
        token.append("/restful/system/token.json?apiKey="+DaDeConstants.APIKEY+"&authCode="+authCode()+"&sign="+getSign());

        String Token = HttpGetUtils.getResponseContext(token.toString());;
        JSONObject t = JSONObject.fromObject(Token);
        JSONObject to = t.getJSONObject("data");
        String data = to.getString("token");

        return data;
    }

}
