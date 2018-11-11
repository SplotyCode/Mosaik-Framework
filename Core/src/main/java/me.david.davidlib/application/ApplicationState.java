package me.david.davidlib.application;

public enum ApplicationState {

    /** Initial State - Not found by Application Management */
    UNFOUND,
    /** Found by Application Management */
    FOUND,
    /** Application gets configurised */
    CONFIGURISED,
    /** Application start was skipped */
    SKIPED,
    /** Application is starting */
    STARTING,
    /** Application started */
    STARTED

}
