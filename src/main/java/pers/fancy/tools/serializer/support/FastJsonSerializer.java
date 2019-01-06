package pers.fancy.tools.serializer.support;

import com.alibaba.fastjson.JSON;
import pers.fancy.tools.serializer.AbstractSerializer;


public class FastJsonSerializer extends AbstractSerializer {

    private Class<?> type;

    public FastJsonSerializer(Class<?> type) {
        this.type = type;
    }

    @Override
    protected byte[] doSerialize(Object obj) throws Throwable {
        String json = JSON.toJSONString(obj);
        return json.getBytes("UTF-8");
    }

    @Override
    protected Object doDeserialize(byte[] bytes) throws Throwable {
        String json = new String(bytes, 0, bytes.length, "UTF-8");
        return JSON.parseObject(json, type);
    }
}
