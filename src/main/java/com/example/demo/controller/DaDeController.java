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
@Slf4j
@RestController
@Validated
@RequestMapping("/RBJF")
public class DaDeController {

    private static final Logger logger = LoggerFactory.getLogger(DaDeController.class);

    String Edu_result;

    //学历认证 post请求
    @RequestMapping(value="/getEdu", produces="application/json;charset=UTF-8", method= RequestMethod.POST)
    public Response getEdu(@RequestBody Request request) {
        log.info("收到请求： {}", request);

        Response response = new Response();

        String jsonData = request.getJsonData();

        JSONObject jsonObject = JSON.parseObject(jsonData);
        String idNo = (String) jsonObject.get("idNo");
        String name = (String) jsonObject.get("name");


                Edu_result= getE(idNo,name);

                JSONObject thirdResJson = JSONObject.parseObject(Edu_result);
                String code = thirdResJson.getString("code");
                switch(code) {
                    case "200":
                        response.setResCode("0000");
                        response.getData().setStatusCode("2012");
                        response.getData().setResult(thirdResJson.getJSONObject("data").getJSONArray("value"));
                        break;

                    case "403":
                    case "417":
                    case "418":
                        //1001没有此接口的访问权限
                        response.setResCode("1005");
                        //本数据库未查得2007
                        response.getData().setStatusCode("2007");
                        break;
                    case "400":
                    case "404":
                    case "406":
                    case "411":
                    case "413":
                    case "414":
                        //1005 请求参数未空或者格式错误
                        response.setResCode("1005");
                        //2006 不一致
                        response.getData().setStatusCode("2016");
                        break;
                    case "412":
                    case "10011":
                    case "10021":
                    case "10007":
                    case "10008":
                    case "10009":
                        //1005 请求参数为空或者格式错误
                        response.setResCode("1005");
                        //2013系统内部错误
                        response.getData().setStatusCode("2013");
                        break;
                    default:
                        //1001没有此接口的访问权限
                        response.setResCode("1001");
                        //2013系统内部错误
                        response.getData().setStatusCode("2013");
                        break;
                }
        //日志响应
        log.info("响应数据:{}", response);
        return response;
    }


    //大德对接get请求
    @RequestMapping(value="/checkEducation", produces="application/json;charset=UTF-8", method= RequestMethod.GET)
    public Response getE2(@Size(min = 15,max = 18,message = "身份证号码必须是15位或18位") @RequestParam("idNo")String idNo,
                          @NotBlank(message = "姓名不能为空")  @RequestParam("name")String name){

        Response response = new Response();
        //把姓名过滤
        String data = getE(idNo,name.replace(" ",""));
        JSONObject thirdResJson = JSONObject.parseObject(data);
        System.out.println("学历:"+thirdResJson.toString());

        logger.debug("学历:"+thirdResJson.toString());

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
            case "401":
            case "402":
            case "403":
            case "404":
            case "405":
            case "406":
            case "416":
                //1001没有此接口的访问权限
                response.setResCode("1001");
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
            case "415":
                response.setResCode("0000");
                response.getData().setStatusCode("2007");
                break;
            case "511"://暂时按照超时处理
                response.setResCode("0000");
                response.getData().setStatusCode("2013");
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


    //异常处理
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
