package tk.mybatis.springboot.util;/**
 * Created by Administrator on 2016/12/5.
 */

/**
 * @author CUTIE
 * @create 2016-12-05 16:31
 **/
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.util.Date;

public class GsonUtils {
    /**
     * @Title: toJson
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param bean
     * @return String 返回类型
     * @throws：
     */
    public static String toJson1(Object bean){
        Gson gson=new GsonBuilder()
                .registerTypeAdapter(java.util.Date.class, new Date())
                .setDateFormat("yyyyMMddhhmmss")
                .create();
        return gson.toJson(bean);
    }

    /**
     * @Title: toJson
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param bean
     * @return String 返回类型
     * @throws：
     */
    public static String toJson(Object bean){
        Gson gson=new Gson();
        return gson.toJson(bean);
    }

    public static String toJson(Object bean,Type type){
        Gson gson=new GsonBuilder()
                .registerTypeAdapter(java.util.Date.class, new Date())
                .setDateFormat("yyyyMMddhhmmss")
                .create();
        return gson.toJson(bean, type);
    }

    /**
     * @Title: fromJson
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param json
     * @param type
     * @return T 返回类型
     * @throws：
     */
    public static Object fromJson(String json,Type type){
        Gson gson=new GsonBuilder()
                .registerTypeAdapter(java.util.Date.class, new Date())
                .setDateFormat("yyyyMMddhhmmss")
                .create();
        return gson.fromJson(json, type);
    }

    /**
     * @Title: fromJson
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param <T>
     * @param json
     * @param classOfT
     * @return T 返回类型
     * @throws：
     */
    public  static <T>T fromJson(String json,Class<T> classOfT){
        Gson gson=new GsonBuilder()
                .registerTypeAdapter(java.util.Date.class, new Date())
                .setDateFormat("yyyyMMddhhmmss")
                .create();
        return gson.fromJson(json, classOfT);
    }
}