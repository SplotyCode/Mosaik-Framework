package io.github.splotycode.mosaik.webapi.response.content.manipulate;

import io.github.splotycode.mosaik.webapi.response.content.ResponseContent;

public interface ManipulateableContent extends ResponseContent {

    ResponseManipulator manipulate();

}
