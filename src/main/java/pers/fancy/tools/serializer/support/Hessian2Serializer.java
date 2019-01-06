package pers.fancy.tools.serializer.support;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import pers.fancy.tools.serializer.AbstractSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;


public class Hessian2Serializer extends AbstractSerializer {

    @Override
    protected byte[] doSerialize(Object obj) throws Throwable {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            Hessian2Output out = new Hessian2Output(os);
            out.writeObject(obj);
            out.close();
            return os.toByteArray();
        }
    }

    @Override
    protected Object doDeserialize(byte[] bytes) throws Throwable {
        try (ByteArrayInputStream is = new ByteArrayInputStream(bytes)) {
            Hessian2Input in = new Hessian2Input(is);
            Object result = in.readObject();
            in.close();
            return result;
        }
    }
}
