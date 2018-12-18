package me.david.webapi.session;

import me.david.webapi.request.Request;

public interface SessionCreator {

    Session createSession(Request request);

}
