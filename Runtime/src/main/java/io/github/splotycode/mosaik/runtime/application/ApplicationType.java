package io.github.splotycode.mosaik.runtime.application;

import io.github.splotycode.mosaik.runtime.startup.BootContext;

public interface ApplicationType extends IApplication {

    /*
     * Do not override this method.
     * Change ApplicationType to you application type
     */
    default void initType(BootContext context, ApplicationType type) {}

}
