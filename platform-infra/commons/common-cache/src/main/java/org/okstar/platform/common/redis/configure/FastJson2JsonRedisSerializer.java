//package org.okstar.platform.common.redis.configure;
//
//import com.alibaba.fastjson2.JSON;
//import com.fasterxml.jackson.databind.JavaType;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.type.TypeFactory;
//import org.okstar.platform.common.core.constant.Constants;
//import org.apache.commons.lang3.SerializationException;
//import org.springframework.util.Assert;
//
//import java.nio.charset.Charset;
//
///**
// *
// * Redis使用FastJson序列化
// *
// *
// */
//public class FastJson2JsonRedisSerializer<T> implements RedisSerializer<T>
//{
//    @SuppressWarnings("unused")
//    private ObjectMapper objectMapper = new ObjectMapper();
//
//    public static final Charset DEFAULT_CHARSET = Charset.forName(Constants.UTF8);
//
//    private Class<T> clazz;
//
//    static
//    {
////        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
//    }
//
//    public FastJson2JsonRedisSerializer(Class<T> clazz)
//    {
//        super();
//        this.clazz = clazz;
//    }
//
//    @Override
//    public byte[] serialize(T t) throws SerializationException
//    {
//        if (t == null)
//        {
//            return new byte[0];
//        }
//        return JSON.toJSONString(t).getBytes(DEFAULT_CHARSET);
//    }
//
//    @Override
//    public T deserialize(byte[] bytes) throws SerializationException
//    {
//        if (bytes == null || bytes.length <= 0)
//        {
//            return null;
//        }
//        String str = new String(bytes, DEFAULT_CHARSET);
//
//        return JSON.parseObject(str, clazz);
//    }
//
//    public void setObjectMapper(ObjectMapper objectMapper)
//    {
//        OkAssert.notNull(objectMapper, "'objectMapper' must not be null");
//        this.objectMapper = objectMapper;
//    }
//
//    protected JavaType getJavaType(Class<?> clazz)
//    {
//        return TypeFactory.defaultInstance().constructType(clazz);
//    }
//}
