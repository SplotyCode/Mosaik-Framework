package io.github.splotycode.mosaik.runtime.application;

public interface ApplicationHandle {

    Application getApplication();

    void configurise();
    void start();


}
