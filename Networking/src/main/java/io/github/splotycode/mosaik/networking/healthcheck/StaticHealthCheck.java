package io.github.splotycode.mosaik.networking.healthcheck;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaticHealthCheck implements HealthCheck {

    private boolean online;

}
