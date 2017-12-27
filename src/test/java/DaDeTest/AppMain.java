package DaDeTest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 运行类
 * 改实现方案仅供参考。
 */
public class AppMain 
{
    public static void main( String[] args ) throws Exception
    {
        //此处是定时获取token信息
        //参数16 是16分钟，目前是token有效时间减去4分钟,此处只是demo，仅供参考，最大不超过20分钟
        //时间通过配置文件读取方式，灵活控制，因为bdcsc2系统的token有效时间后续会有所调整。 
        //也可以根据 返回结果的  validTime 值（ token 有效时间）做适当调整
        //{"code":200,"status":"SUCCEED","message":"","time":"2016-09-02 14:02:57","trace":"","data":{"token":"c2bw5R879WkUcvkSiTQsdpKNXr6w4W0e","validTime":86340000}}
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new TokenHandler(), 0, 16, TimeUnit.MINUTES); 
        
        Thread.sleep(10000); //此处等待是为了让线程执行完毕 获取到token

        //测试学历
        String product="rp-label";  //产品
        String module="statusdd"; //模块
        String method="_getEdu";//方法
        
        //传递的参数id+name
        Map<String,String>  paramMap= new HashMap<String,String>();
        paramMap.put("idNo", "370481198912208219");
        paramMap.put("name", "李超");

        String url= TokenHandler.getReqUrl(product,module,method);
        UHttpClient.Res doRequest = UHttpClient.doRequest(UHttpClient.Method.get, url, paramMap, "UTF-8", true);
        
        System.out.println("学历查询"+doRequest.content);
        
    }
}
