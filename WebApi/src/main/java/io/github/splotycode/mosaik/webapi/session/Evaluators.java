package io.github.splotycode.mosaik.webapi.session;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Evaluators {

    public static SessionEvaluator getTimeEvaluator(long maxDelay) {
        return (session, request) -> {
            long delay = System.currentTimeMillis() - session.lastRefresh();
            return delay <= maxDelay;
        };
    }

    public static SessionEvaluator getAndEvaluator(SessionEvaluator... evaluators) {
        return (session, request) -> Arrays.stream(evaluators).allMatch(evaluator -> evaluator.valid(session, request));
    }

    public static SessionEvaluator getOrEvaluator(SessionEvaluator... evaluators) {
        return (session, request) -> Arrays.stream(evaluators).anyMatch(evaluator -> evaluator.valid(session, request));
    }

    public static SessionEvaluator TRUE_EVALUATOR = (session, request) -> true;
    public static SessionEvaluator IP_CHANGE = (session, request) -> !request.getIpAddress().equals(session.startedIpAddress());

}
