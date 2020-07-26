package io.github.splotycode.mosaik.runtime.startup.condition;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class TestResult {
    public static final TestResult SUCCESSFUL = new TestResult(true, null);

    public static TestResult failed(String message) {
        return new TestResult(false, message);
    }

    private final boolean successful;
    private final String message;
}
