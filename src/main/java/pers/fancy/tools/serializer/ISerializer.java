package pers.fancy.tools.serializer;


public interface ISerializer {

    <T> byte[] serialize(T obj);

    <T> T deserialize(byte[] bytes);
}
