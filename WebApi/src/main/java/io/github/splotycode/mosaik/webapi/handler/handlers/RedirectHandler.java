package io.github.splotycode.mosaik.webapi.handler.handlers;

import io.github.splotycode.mosaik.annotations.visibility.Invisible;
import io.github.splotycode.mosaik.util.Pair;
import io.github.splotycode.mosaik.util.datafactory.DataKey;
import io.github.splotycode.mosaik.webapi.handler.HttpHandler;
import io.github.splotycode.mosaik.webapi.handler.UrlPattern;
import io.github.splotycode.mosaik.webapi.request.HandleRequestException;
import io.github.splotycode.mosaik.webapi.request.Request;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Invisible
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RedirectHandler implements HttpHandler {

    private static final DataKey<Pair<String, Integer>> REDIRECT = new DataKey<>(RedirectHandler.class.getName() + ".redirect");

    private HashMap<UrlPattern, Pair<String, Integer>> redirects = new HashMap<>();
    @Getter private boolean blockIfRedirect;

    public static RedirectHandler createSimple(Pair<String, String>... redirects) {
        RedirectHandler handler = new RedirectHandler();
        for (Pair<String, String> redirect : redirects) {
            handler.redirects.put(new UrlPattern(redirect.getOne()), new Pair<>(redirect.getTwo(), 307));
        }
        return handler;
    }

    public static RedirectHandler createSimple(boolean permanent, Pair<String, String>... redirects) {
        RedirectHandler handler = new RedirectHandler();
        for (Pair<String, String> redirect : redirects) {
            handler.redirects.put(new UrlPattern(redirect.getOne()), new Pair<>(redirect.getTwo(), permanent ? 308 : 307));
        }
        return handler;
    }

    public static RedirectHandler createSimple(boolean permanent, String origin, String destination) {
        RedirectHandler handler = new RedirectHandler();
        handler.redirects.put(new UrlPattern(origin), new Pair<>(destination, permanent ? 308 : 307));
        return handler;
    }

    public static RedirectHandler create(Pair<String, Pair<String, Integer>>... redirects) {
        RedirectHandler handler = new RedirectHandler();
        for (Pair<String, Pair<String, Integer>> redirect : redirects) {
            handler.redirects.put(new UrlPattern(redirect.getOne()), redirect.getTwo());
        }
        return handler;
    }

    public static RedirectHandler create(String origin, String destination) {
        return new RedirectHandler(origin, destination);
    }

    public RedirectHandler(String origin, String destination) {
        this(origin, destination, 307);
    }

    public RedirectHandler(String origin, String destination, int errorCode) {
        redirects.put(new UrlPattern(origin), new Pair<>(destination, errorCode));
    }

    public RedirectHandler setBlockIfRedirect(boolean blockIfRedirect) {
        this.blockIfRedirect = blockIfRedirect;
        return this;
    }

    @Override
    public boolean valid(Request request) throws HandleRequestException {
        for (Map.Entry<UrlPattern, Pair<String, Integer>> redirect : redirects.entrySet()) {
            if (redirect.getKey().match(request.getPath()).isMatch()) {
                request.getDataFactory().putData(REDIRECT, redirect.getValue());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean handle(Request request) throws HandleRequestException {
        Pair<String, Integer> redirect = request.getDataFactory().getData(REDIRECT);
        request.getResponse().redirect(redirect.getOne(), redirect.getTwo());
        return blockIfRedirect;
    }
}
