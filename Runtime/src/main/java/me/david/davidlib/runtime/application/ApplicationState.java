package me.david.davidlib.runtime.application;

public enum ApplicationState {

    /** Initial State - Not found by Application Management */
    NOT_FOUND,
    /** Found by Application Management */
    FOUND,
    /** Application gets configurised */
    CONFIGURISED,
    /** Application start was skipped */
    SKIPPED,
    /** Application is starting */
    STARTING,
    /** Application started */
    STARTED

}
