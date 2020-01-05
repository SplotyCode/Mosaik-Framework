package io.github.splotycode.mosaik.util;

import lombok.*;

/**
 * Holds tree Objects
 */
@Data
@NoArgsConstructor
public class Tribble<A, B, C> {

    private A a;
    private B b;
    private C c;

}
