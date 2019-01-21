package io.github.splotycode.mosaik.webapi.response;

public enum ContentType {

    TEXT_PLAIN,
    TEXT_HTML;

    public String value() {
        return name().toLowerCase().replace('_', '/');
    }

}
