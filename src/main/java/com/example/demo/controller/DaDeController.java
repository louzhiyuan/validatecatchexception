package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.piccfs.dt.apiadpter.common.DaDeConstants;
import com.piccfs.dt.apiadpter.model.Request;
import com.piccfs.dt.apiadpter.model.Response;
import com.piccfs.dt.apiadpter.utils.DaDe.TokenManager;
import com.piccfs.dt.apiadpter.utils.httpclient.HttpGetUtils;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Size;
import java.util.Set;

//日志

@RestController
@Validated
@RequestMapping("/RBJF")
public class DaDeController {

    private static final Logger logger = LoggerFactory.getLogger(DaDeController.class);

    String Edu_result;
    
    //get请求
    @RequestMapping(value="/checkEducation", produces="application/json;charset=UTF-8", method= RequestMethod.GET)
                           //@Size判断字符长度
                           //@NotBlank判断字符串是否为空 @NotEmpty @NotNull
                           //@Max int类型的判断
                           //返回的字符串必须是jsonObject类型
    public Response getE2(@Size(min = 15,max = 18,message = "{'resCode':'1005','resMsg':'身份证号码必须是15位或18位'}") @RequestParam("idNo")String idNo,
                          @NotBlank(message = "{'resCode':'1005','resMsg':'姓名不能为空'}")  @RequestParam("name")String name){

        Response response = new Response();
        //把姓名过滤，自动排除空格
        String data = getE(idNo,name.replace(" ",""));
        JSONObject thirdResJson = JSONObject.parseObject(data);
        System.out.println("学历:"+thirdResJson.toString());

        logger.info("学历:"+thirdResJson.toString());

        String code = thirdResJson.getString("code");
        switch(code) {
            case "200":
                response.setResCode("0000");
                response.getData().setStatusCode("2012");
                response.getData().setResult(thirdResJson.getJSONObject("data").getJSONArray("value"));
                break;
            case "400":
                //参数错误
                response.setResCode("1005");
                response.getData().setStatusCode("2012");
                break;
            case "411":
            case "412":
            case "413":
            case "414":
                //1001没有此接口的访问权限
                response.setResCode("1001");
                response.getData().setStatusCode("2006");
                break;
            default:
                response.setResCode("0000");
                //2013系统内部错误
                response.getData().setStatusCode("2013");
                break;
        }
        //日志响应
        log.info("响应数据:{}", response);
        return response;
    }

    //封装的getEdu的方法
    public String getE(String idNo,String name){
        StringBuilder edu = new StringBuilder();
        edu.append(DaDeConstants.protocol).append(DaDeConstants.HOST).append(":").append(DaDeConstants.PORT);
        //针对姓名的空格问题，参数直接处理为自动剔除空格
        edu.append("/restful").append("/"+DaDeConstants.product).append("/"+DaDeConstants.module).append("/"+DaDeConstants.method).append("/"+DaDeConstants.APIKEY).append("/"+ TokenManager.getInstance().getToken()+".json?"+"idNo="+idNo+"&name="+name);
        Edu_result = HttpGetUtils.getResponseContext(edu.toString());

        return Edu_result;
    }

    

    //异常处理，捕获validate验证失败的异常，返回异常的信息解析
    @ExceptionHandler(value = { ConstraintViolationException.class })
    @ResponseBody
    public String handleResourceNotFoundException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuilder strBuilder = new StringBuilder();
        for (ConstraintViolation<?> violation : violations ) {
            strBuilder.append(violation.getMessage() + "\n");
        }
        return strBuilder.toString();
    }

}
