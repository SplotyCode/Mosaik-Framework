package io.github.splotycode.mosaik.webapi.config;

import io.github.splotycode.mosaik.annotations.visibility.VisibilityLevel;
import io.github.splotycode.mosaik.util.datafactory.DataKey;
import io.github.splotycode.mosaik.webapi.response.content.ResponseContent;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WebConfig {

    public static final DataKey<ResponseContent> NO_CONTENT_RESPONSE = new DataKey<>("web.no_content_response");

    public static final DataKey<VisibilityLevel> SEARCH_HANDLERS_VISIBILITY = new DataKey<>("web.static_handlers.search");
    public static final DataKey<VisibilityLevel> SEARCH_ANNOTATION_HANDLERS_VISIBILITY = new DataKey<>("web.annotation_handlers.search");

    public static final DataKey<Boolean> IGNORE_NO_SSL_RECORD = new DataKey<>("web.inore_no_ssl");
    public static final DataKey<Boolean> FORCE_HTTPS = new DataKey<>("web.force_https");

    public static final DataKey<Integer> NETTY_THREADS = new DataKey<>("web.netty.threads");

}
