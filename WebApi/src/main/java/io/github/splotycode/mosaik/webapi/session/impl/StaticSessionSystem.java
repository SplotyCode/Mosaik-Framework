package io.github.splotycode.mosaik.webapi.session.impl;

import io.github.splotycode.mosaik.webapi.session.SessionCreator;
import io.github.splotycode.mosaik.webapi.session.SessionEvaluator;
import io.github.splotycode.mosaik.webapi.session.SessionMatcher;
import io.github.splotycode.mosaik.webapi.session.SessionSystem;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class StaticSessionSystem implements SessionSystem {

    protected SessionCreator sessionCreator;
    protected SessionMatcher sessionMatcher;
    protected SessionEvaluator sessionEvaluator;

}
