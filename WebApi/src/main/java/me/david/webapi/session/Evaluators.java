package me.david.webapi.session;

public class Evaluators {

    public static SessionEvaluator getTimeEvaluator(long maxDelay) {
        return (session, request) -> {
            long delay = System.currentTimeMillis() - session.lastRefresh();
            return delay <= maxDelay;
        };
    }

    public static SessionEvaluator TRUE_EVALUATOR = (session, request) -> true;

}
