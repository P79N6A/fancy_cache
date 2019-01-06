package pers.fancy.tools.http;

import lombok.Data;

import java.util.Map;


@Data
public class RespContext {

    private String resp;

    private AbstractHttpClient client;

    private String path;

    private Map<String, String> params;
}
