package com.example.demo.model;


/**
 * Created by lzy on 2017/11/30.
 */
//@Data标签自动提供get／set方法  依赖lombokjar包
@lombok.Data
public class Response {

    private String resCode;

    private String resMsg;

    private Data data = new Data();

    public void setResCode(String resCode) {
        this.resCode = resCode;
        switch(resCode) {
            case "0000":
                this.resMsg = "提交成功";
                break;
            case "1001":
                this.resMsg = "没有此接口访问权限";
                break;
            case "1005":
                this.resMsg = "请求参数为空或格式错误";
                break;
        }
    }

}
