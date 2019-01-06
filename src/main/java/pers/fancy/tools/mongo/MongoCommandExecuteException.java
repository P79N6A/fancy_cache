package pers.fancy.tools.mongo;

import lombok.Getter;
import lombok.Setter;


public class MongoCommandExecuteException extends RuntimeException {

    private static final long serialVersionUID = -8181715563606319069L;

    @Getter
    @Setter
    private int errorCode;

    @Getter
    @Setter
    private String codeName;

    @Getter
    @Setter
    private int ok;

    @Getter
    @Setter
    private String errmsg;

    @Override
    public String toString() {
        return "MongoCommandExecuteException{" +
                "errorCode=" + errorCode +
                ", codeName='" + codeName + '\'' +
                ", ok=" + ok +
                ", errmsg='" + errmsg + '\'' +
                '}';
    }
}
