package io.github.splotycode.mosaik.argparserimpl;

import io.github.splotycode.mosaik.argparser.Parameter;
import io.github.splotycode.mosaik.runtime.LinkBase;
import io.github.splotycode.mosaik.runtime.Links;
import io.github.splotycode.mosaik.runtime.startup.StartUpInvoke;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArgParserTest {

    @Parameter(name = "neo")
    private boolean neo;

    @Test
    public void testParseArgs() {
        if (StartUpInvoke.invokeTestSuite()) {
            LinkBase.getInstance().getLink(Links.ARG_PARSER).parseArgs(this, new String[]{"-neo"});
            assertTrue(neo);
        }
    }

}
