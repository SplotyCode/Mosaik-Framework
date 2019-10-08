package io.github.splotycode.mosaik.networking.errorhandling;

import io.github.splotycode.mosaik.networking.util.MosaikAddress;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@AllArgsConstructor
@Data
@Getter
public class ErrorEntryID {

    public static ErrorEntryID fromString(String string) {
        String[] part = string.split(":");
        MosaikAddress address = new MosaikAddress(new String(Base64.getDecoder().decode(part[0]), StandardCharsets.UTF_8));
        return new ErrorEntryID(address, part[1]);
    }

    private MosaikAddress host;
    private String id;

    @Override
    public String toString() {
        return Base64.getEncoder().encodeToString(host.asString().getBytes(StandardCharsets.UTF_8)) + ":" + id;
    }
}
