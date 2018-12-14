package me.david.webapi.request;

import me.david.webapi.response.Response;

import java.util.Collection;
import java.util.Map;

public interface Request {

    String getPath();
    Method getMethod();

    Response getResponse();

    byte[] getBody();

    boolean isKeepAlive();

    String getHeader(String name);
    String getHeader(RequestHeaders header);

    Collection<String> getGetParameter(String name);
    Collection<String> getPostParameter(String name);

    String getFirstGetParameter(String name);
    String getFirstPostParameter(String name);

    boolean isGet();
    boolean isPost();

    Map<String, ? extends Collection<String>> getGet();
    Map<String, ? extends Collection<String>> getPost();
    void setGet(Map<String, ? extends Collection<String>> get);
    void setPost(Map<String, ? extends Collection<String>> post);

    Map<String, String> getCookies();

}
