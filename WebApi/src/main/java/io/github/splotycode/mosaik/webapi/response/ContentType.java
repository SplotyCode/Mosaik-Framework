package io.github.splotycode.mosaik.webapi.response;

public enum ContentType {

    TEXT_PLAIN,
    TEXT_HTML,
    APPLICATION_XML,
    APPLICATION_JSON,
    APPLICATION_OCTETSTREAM {
        @Override
        public String value() {
            return "application/octet-stream";
        }
    },
    APPLICATION_RAR {
        @Override
        public String value() {
            return "application/x-rar-compressed";
        }
    },
    APPLICATION_ZIP {
        @Override
        public String value() {
            return "application/zip";
        }
    };

    public String value() {
        return name().toLowerCase().replace('_', '/');
    }

}
