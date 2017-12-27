package com.example.demo.model;

/**
 * Created by lzy on 2017/11/30.
 */
@lombok.Data
public class Data {

    private String statusCode;

    private String statusMsg;

    private Object result;

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
        switch (statusCode){
            case "2005":
                this.statusMsg = "一致";
                break;
            case "2006":
                this.statusMsg = "不一致";
                break;
            case "2007":
                this.statusMsg = "本数据库中未查得";
                break;
            case "2012":
                this.statusMsg = "查询成功";
                break;
            case "2013":
                this.statusMsg = "系统内部错误";
                break;
        }
    }
}
