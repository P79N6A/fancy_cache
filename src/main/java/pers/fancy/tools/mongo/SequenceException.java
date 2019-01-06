package pers.fancy.tools.mongo;

import java.io.Serializable;


public class SequenceException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -7383087459057215862L;

    public SequenceException(String message) {
        super(message);
    }
}
