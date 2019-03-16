
import io.github.splotycode.mosaik.util.logger.Logger;
import io.github.splotycode.mosaik.webapi.handler.anotation.check.Handler;
import io.github.splotycode.mosaik.webapi.handler.anotation.check.Mapping;
import io.github.splotycode.mosaik.webapi.handler.anotation.handle.UrlParam;
import io.github.splotycode.mosaik.webapi.response.Response;
import io.github.splotycode.mosaik.webapi.response.content.file.FileResponseContent;
import io.github.splotycode.mosaik.webapi.response.content.ResponseContent;
import io.github.splotycode.mosaik.webapi.request.Request;


public class MyHandler {

    private Logger logger = Logger.getInstance(getClass());

   // @Handler
    public ResponseContent abs(Request request) {
        FileResponseContent content = new FileResponseContent("test/java/papa/test.html");
        content.manipulate().variable("name", "David");
        return content;
    }

    @Mapping("l/$id$")
    public ResponseContent redirect(@UrlParam("id") String linkId, Response response) {
        logger.info("hey");
        return null;
    }

    @Mapping()
    public boolean two() {
        return false;
    }

}
