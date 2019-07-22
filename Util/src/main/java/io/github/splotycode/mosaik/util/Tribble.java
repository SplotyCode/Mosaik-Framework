package io.github.splotycode.mosaik.util;

import lombok.*;

/**
 * Holds tree Objects
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Tribble<A, B, C> {

    private A a;
    private B b;
    private C c;

}
