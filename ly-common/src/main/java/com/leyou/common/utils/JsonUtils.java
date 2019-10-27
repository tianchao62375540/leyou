package com.leyou.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * json工具类
 */
public class JsonUtils {

    public static final ObjectMapper mapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    /**
     * 序列化成string
     * @param obj
     * @return
     */
    @Nullable
    public static String serialize(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj.getClass() == String.class) {
            return (String) obj;
        }
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("json序列化出错：" + obj, e);
            return null;
        }
    }

    /**
     * 反序列化转换成bean
     * @param json
     * @param tClass
     * @param <T>
     * @return
     */
    @Nullable
    public static <T> T parseBean(String json, Class<T> tClass) {
        try {
            return mapper.readValue(json, tClass);
        } catch (IOException e) {
            logger.error("json解析出错：" + json, e);
            return null;
        }
    }

    /**
     * 反序列化转换成list<T></>
     * @param json
     * @param eClass
     * @param <E>
     * @return
     */
    @Nullable
    public static <E> List<E> parseList(String json, Class<E> eClass) {
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, eClass));
        } catch (IOException e) {
            logger.error("json解析出错：" + json, e);
            return null;
        }
    }

    /**
     * 反序列化转换成Map
     * @param json
     * @param kClass
     * @param vClass
     * @param <K>
     * @param <V>
     * @return
     */
    @Nullable
    public static <K, V> Map<K, V> parseMap(String json, Class<K> kClass, Class<V> vClass) {
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructMapType(Map.class, kClass, vClass));
        } catch (IOException e) {
            logger.error("json解析出错：" + json, e);
            return null;
        }
    }

    /**
     *
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    @Nullable
    public static <T> T nativeRead(String json, TypeReference<T> type) {
        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
            logger.error("json解析出错：" + json, e);
            return null;
        }
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    static class User{
        String name;
        Integer age;
    }
    public static void main(String[] args) {
        final User user = new User("Jack",21);
        String json = serialize(user);
        System.out.println("json:"+json);
        //反序列化 toBean
        User user1 = parseBean(json,User.class);
        System.out.println(user1);
        //反序列化 toList
        String json2  = null;
        json2 = "[20,15,20,-1]";
        List<Integer> integers = parseList(json2, Integer.class);
        System.out.println(integers);
        //反序列化 toMap
        Map<String, Object> stringObjectMap = parseMap(json, String.class, Object.class);
        System.out.println(stringObjectMap);
        /**
         * json:{"name":"Jack","age":21}
         * JsonUtils.User(name=Jack, age=21)
         * [20, 15, 20, -1]
         * {name=Jack, age=21}
         */
        String json3 ="[{\"name\":\"Jack\",\"like\":\"football\"},{\"name\":\"Tom\",\"like\":\"basketBall\"}]";
        //List<Map> maps = parseList(json3, Map.class);
        List<Map<String, String>> maps = nativeRead(null, new TypeReference<List<Map<String, String>>>() {
        });
        System.out.println(maps);
    }
}
