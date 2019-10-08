package io.github.splotycode.mosaik.networking.errorhandling;

import io.github.splotycode.mosaik.util.ExceptionUtil;
import lombok.Data;
import lombok.Getter;

import java.util.HashMap;

@Getter
@Data
public class ErrorEntry {

    private ErrorEntryID id;
    private ErrorEntryID previous;

    private HashMap<String, String> log = new HashMap<>();

    public ErrorEntry(ErrorEntryID id, ErrorEntryID previous) {
        this.id = id;
        this.previous = previous;
    }

    public ErrorEntry log(String title, String message) {
        log.put(title, message);
        return this;
    }

    public ErrorEntry log(String title, Throwable throwable) {
        return log(title, ExceptionUtil.toString(throwable));
    }

}
