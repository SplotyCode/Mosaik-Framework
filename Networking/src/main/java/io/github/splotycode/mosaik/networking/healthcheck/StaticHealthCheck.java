package io.github.splotycode.mosaik.networking.healthcheck;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaticHealthCheck implements HealthCheck {

    public static final StaticHealthCheck TRUE = new StaticHealthCheck(true);
    public static final StaticHealthCheck FALSE = new StaticHealthCheck(false);

    private boolean online;

}
