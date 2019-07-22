package io.github.splotycode.mosaik.runtime.application;

/**
 * Handle for Application
 * Can be get thought the StartUpManager
 */
public interface ApplicationHandle {

    Application getApplication();

    void configurise();
    void start();

}
