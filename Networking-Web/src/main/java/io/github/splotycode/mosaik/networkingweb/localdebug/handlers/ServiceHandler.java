package io.github.splotycode.mosaik.networkingweb.localdebug.handlers;

import io.github.splotycode.mosaik.annotations.visibility.Invisible;
import io.github.splotycode.mosaik.networking.service.Service;
import io.github.splotycode.mosaik.networkingweb.localdebug.LocalDebugService;
import io.github.splotycode.mosaik.util.Pair;
import io.github.splotycode.mosaik.webapi.handler.anotation.check.Mapping;
import io.github.splotycode.mosaik.webapi.response.content.ResponseContent;
import io.github.splotycode.mosaik.webapi.response.content.manipulate.ManipulateableContent;
import lombok.AllArgsConstructor;

@Invisible
@AllArgsConstructor
public class ServiceHandler {

    private LocalDebugService service;

    @Mapping("services")
    public ResponseContent view() {
        ManipulateableContent content =  service.getUnpackingHelper().response("services.html");
        for (Service service : service.cloudKit().getServices()) {
            content.manipulate().patternCostomName("services",
                    new Pair<>("name", service.displayName()),
                    new Pair<>("status", service.getStatus()),
                    new Pair<>("message", service.statusMessage()));
        }
        return content;
    }

}
