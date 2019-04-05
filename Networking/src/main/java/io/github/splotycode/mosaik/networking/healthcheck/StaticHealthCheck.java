package io.github.splotycode.mosaik.networking.healthcheck;

import lombok.*;

import java.net.SocketAddress;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class StaticHealthCheck implements HealthCheck {

    private boolean online;
    private SocketAddress address;

}
