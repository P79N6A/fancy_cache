package pers.fancy.tools.serializer.support;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import pers.fancy.tools.serializer.AbstractSerializer;

import java.io.ByteArrayOutputStream;


public class KryoSerializer extends AbstractSerializer {

    @Override
    protected byte[] doSerialize(Object obj) throws Throwable {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             Output output = new Output(bos)) {
            new Kryo().writeClassAndObject(output, obj);
            output.flush();

            return bos.toByteArray();
        }
    }

    @Override
    protected Object doDeserialize(byte[] bytes) {
        try (Input input = new Input(bytes)) {
            return new Kryo().readClassAndObject(input);
        }
    }
}
