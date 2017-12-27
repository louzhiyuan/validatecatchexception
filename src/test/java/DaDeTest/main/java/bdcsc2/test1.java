package DaDeTest.main.java.bdcsc2;


import com.alibaba.fastjson.JSONObject;

public class test1 {

    public static void main(String[] args) {
        String res = "{'code':200,'status':'SUCCEED','message':'','time':'2017-12-05 13:43:13','trace':'','data':{'value':[{'enrollDate':'2012','degree':'本科','school':'郑州大学','degreeType':'普通','gradDate':'2016','gradResult':'毕业','major':'计算机科学与技术(金融信息化方向)'}]}}";
        JSONObject thirdResJson = JSONObject.parseObject(res);

        String code = (String) thirdResJson.getString("code");

        System.out.println(code);
    }
}
