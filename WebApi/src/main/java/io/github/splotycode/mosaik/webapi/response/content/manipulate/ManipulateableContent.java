package io.github.splotycode.mosaik.webapi.response.content.manipulate;

import io.github.splotycode.mosaik.webapi.response.content.ResponseContent;

public interface ManipulateableContent<T extends ManipulateableContent> extends ResponseContent {

    ResponseManipulator manipulate();

    default T setManipulationDataCashing(boolean cashing) {
        manipulate().setCashing(cashing);
        return self();
    }

    T self();

}
