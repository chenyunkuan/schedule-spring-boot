package tk.mybatis.springboot.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;


/**
 * @author administrator
 *
 * Http Client工具类
 * 发起http client 请求
    //获取单例对象，并设置默认的页面编码
    public static HttpClientUtil getInstance(String urlCharset);

    //设置编码
    public void setUrlCharset(String urlCharset);

    //获取字符串型返回结果，通过发起http post请求
    public String getResponseBodyAsString(String targetUrl,Map params)throws Exception;

    //获取字符数组型返回结果，通过发起http post请求
    public byte[] getResponseBodyAsByteArray(String targetUrl,Map params)throws Exception;

    //将response的返回流写到outputStream中，通过发起http post请求
    public void getResponseBodyAsStream(String targetUrl,Map params,OutputStream outputStream)throws Exception;
 */
public final class HttpClientUtil {

    private static final String TYPE_STRING = "string";

    private static final String TYPE_BYTEARRAY = "byte[]";

    private static final String TYPE_STREAM = "stream";

    private static HttpClientUtil instance;

    private HttpClientUtil(){}

    /**
     * 使用默认的页面请求编码：utf-8
     * @return
     */
    public static HttpClientUtil getInstance(){
        return getInstance("UTF-8");
    }

    public static HttpClientUtil getInstance(String urlCharset){
        if(instance == null){
            instance = new HttpClientUtil();
        }
        //设置默认的url编码
        instance.setUrlCharset(urlCharset);
        return instance;
    }

    /**
     * 请求编码，默认使用utf-8
     */
    private String urlCharset = "UTF-8";

    /**
     * @param urlCharset 要设置的 urlCharset。
     */
    public void setUrlCharset(String urlCharset) {
        this.urlCharset = urlCharset;
    }
    /**
     * 获取字符串型返回结果，通过发起http post请求
     * @param targetUrl
     * @param params
     * @return
     * @throws Exception
     */
    public String getResponseBodyAsString(String targetUrl,Map params)throws Exception{

        return (String)setPostRequest(targetUrl,params,TYPE_STRING,null);
    }

    /**
     * 获取字符数组型返回结果，通过发起http post请求
     * @param targetUrl
     * @param params
     * @return
     * @throws Exception
     */
    public byte[] getResponseBodyAsByteArray(String targetUrl,Map params)throws Exception{

        return (byte[])setPostRequest(targetUrl,params,TYPE_BYTEARRAY,null);
    }

    /**
     * 将response的返回流写到outputStream中，通过发起http post请求
     * @param targetUrl					请求地址
     * @param params					请求参数<paramName,paramValue>
     * @param outputStream				输出流
     * @throws Exception
     */
    public void getResponseBodyAsStream(String targetUrl,Map params,OutputStream outputStream)throws Exception{
        if(outputStream == null){
            throw new IllegalArgumentException("调用HttpClientUtil.setPostRequest方法，targetUrl不能为空!");
        }

        //response 的返回结果写到输出流
        setPostRequest(targetUrl,params,TYPE_STREAM,outputStream);
    }

    /**
     * 利用http client模拟发送http post请求
     * @param targetUrl					请求地址
     * @param params					请求参数<paramName,paramValue>
     * @return	Object					返回的类型可能是：String 或者 byte[] 或者 outputStream
     * @throws Exception
     */
    private Object setPostRequest(String targetUrl,Map params,String responseType,OutputStream outputStream)throws Exception{
        if(StringUtils.isBlank(targetUrl)){
            throw new IllegalArgumentException("调用HttpClientUtil.setPostRequest方法，targetUrl不能为空!");
        }

        Object responseResult = null;
        HttpClient client = null;
        PostMethod post = null;
        NameValuePair[] nameValuePairArr = null;
        SimpleHttpConnectionManager connectionManager = null;
        try{
            connectionManager =  new SimpleHttpConnectionManager(true);
            //连接超时,单位毫秒
            connectionManager.getParams().setConnectionTimeout(3000);
            //读取超时,单位毫秒
            connectionManager.getParams().setSoTimeout(60000);

            //设置获取内容编码
            connectionManager.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,urlCharset);
            client = new HttpClient(new HttpClientParams(),connectionManager);

            post = new PostMethod(targetUrl);
            //设置请求参数的编码
            post.getParams().setContentCharset(urlCharset);

            //服务端完成返回后，主动关闭链接
            post.setRequestHeader("Connection","close");
            if(params != null){
                nameValuePairArr = new NameValuePair[params.size()];

                Set key = params.keySet();
                Iterator keyIte = key.iterator();
                int index = 0;
                while(keyIte.hasNext()){
                    Object paramName = keyIte.next();
                    Object paramValue = params.get(paramName);
                    if(paramName instanceof String && paramValue instanceof String){
                        NameValuePair pair = new NameValuePair((String)paramName,(String)paramValue);
                        nameValuePairArr[index] = pair;
                        index++;
                    }
                }

                post.addParameters(nameValuePairArr);
            }

            int sendStatus = client.executeMethod(post);

            if(sendStatus == HttpStatus.SC_OK){
                System.out.println("HttpClientUtil.setPostRequest()-responseType:"+responseType);

                if(StringUtils.equals(TYPE_STRING,responseType)){
                    responseResult = post.getResponseBodyAsString();
                }else if(StringUtils.equals(TYPE_BYTEARRAY,responseType)){
                    responseResult = post.getResponseBody();
                }else if(StringUtils.equals(TYPE_STREAM,responseType)){
                    InputStream tempStream = post.getResponseBodyAsStream();
                    byte[] temp = new byte[1024];
                    int index = -1;
                    while((index = tempStream.read(temp)) != -1){
                        outputStream.write(temp);
                    }
                }
            }else{
                System.err.println("***************************");
                System.err.println("HttpClientUtil.setPostRequest()-请求url："+targetUrl+" 出错\n请求参数有："+ GsonUtils.toJson(params)+"！！！");
                System.err.println("***************************");
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            //释放链接
            if(post != null){
                post.releaseConnection();
            }

            //关闭链接
            if(connectionManager != null){
                connectionManager.shutdown();
            }
        }

        return responseResult;
    }


}