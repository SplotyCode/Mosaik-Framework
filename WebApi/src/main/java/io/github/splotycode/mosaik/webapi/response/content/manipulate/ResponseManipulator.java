package io.github.splotycode.mosaik.webapi.response.content.manipulate;

public interface ResponseManipulator {

    ResponseManipulator variable(String str, Object obj);
    ResponseManipulator object(Object object);
    ResponseManipulator pattern(String name, Object object);
    ResponseManipulator pattern(Object object);

}
