package io.github.splotycode.mosaik.networkingweb.debug.handlers;

import io.github.splotycode.mosaik.annotations.visibility.Invisible;
import io.github.splotycode.mosaik.networking.host.Host;
import io.github.splotycode.mosaik.networking.statistics.HostStatistics;
import io.github.splotycode.mosaik.networking.statistics.StatisticalHost;
import io.github.splotycode.mosaik.networkingweb.debug.DebugService;
import io.github.splotycode.mosaik.networkingweb.localdebug.LocalDebugService;
import io.github.splotycode.mosaik.util.Pair;
import io.github.splotycode.mosaik.util.StringUtil;
import io.github.splotycode.mosaik.webapi.handler.anotation.check.Mapping;
import io.github.splotycode.mosaik.webapi.response.content.ResponseContent;
import io.github.splotycode.mosaik.webapi.response.content.manipulate.ManipulateableContent;
import lombok.AllArgsConstructor;

import java.text.DecimalFormat;

@Invisible
@AllArgsConstructor
public class HostHandler {

    private static final DecimalFormat CPU_FORMAT = new DecimalFormat("#.#");

    private DebugService service;

    @Mapping("hosts")
    public ResponseContent view() {
        int debugPort = service.cloudKit().getConfig(LocalDebugService.PORT);

        ManipulateableContent content =  service.getUnpackingHelper().response("hosts.html");
        for (Host host : service.cloudKit().hostMap().values()) {
            String cpu = "-";
            String freeRam = "-";
            if (host instanceof StatisticalHost) {
                HostStatistics statistics = ((StatisticalHost) host).getStatistics();
                if (statistics != null) {
                    cpu = CPU_FORMAT.format(statistics.getCpu());
                    freeRam = StringUtil.humanReadableBytes(statistics.getFreeRam());
                }
            }
            content.manipulate().patternCostomName("hosts",
                    new Pair<>("address", host.address()),
                    new Pair<>("debugPort", debugPort),
                    new Pair<>("cpu", cpu),
                    new Pair<>("freeRam", freeRam));
        }
        return content;
    }

}
