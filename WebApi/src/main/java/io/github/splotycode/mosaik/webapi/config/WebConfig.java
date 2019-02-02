package io.github.splotycode.mosaik.webapi.config;

import io.github.splotycode.mosaik.util.datafactory.DataKey;
import io.github.splotycode.mosaik.webapi.response.content.ResponseContent;

public final class WebConfig {

    public static final DataKey<ResponseContent> NO_CONTENT_RESPONSE = new DataKey<>("web.no_content_response");
    public static final DataKey<Boolean> SEARCH_HANDLERS = new DataKey<>("web.static_handlers.search");
    public static final DataKey<Boolean> SEARCH_ANNOTATION_HANDLERS = new DataKey<>("web.annotation_handlers.search");

}
