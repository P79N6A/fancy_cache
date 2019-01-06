package pers.fancy.tools.serializer.support;

import pers.fancy.tools.serializer.AbstractSerializer;
import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;


public class JdkSerializer extends AbstractSerializer {

    @Override
    protected byte[] doSerialize(Object obj) {
        return SerializationUtils.serialize((Serializable) obj);
    }

    @Override
    protected Object doDeserialize(byte[] bytes) {
        return SerializationUtils.deserialize(bytes);
    }
}
