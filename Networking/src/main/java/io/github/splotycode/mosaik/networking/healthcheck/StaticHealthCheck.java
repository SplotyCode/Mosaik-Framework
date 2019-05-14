package io.github.splotycode.mosaik.networking.healthcheck;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class StaticHealthCheck implements HealthCheck {

    private boolean online;

}
