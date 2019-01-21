package io.github.splotycode.mosaik.webapi.session.impl;

import io.github.splotycode.mosaik.webapi.session.SessionEvaluator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import io.github.splotycode.mosaik.webapi.session.SessionCreator;
import io.github.splotycode.mosaik.webapi.session.SessionMatcher;
import io.github.splotycode.mosaik.webapi.session.SessionSystem;

@Getter
@AllArgsConstructor
public class SessionSystemImpl implements SessionSystem {

    protected SessionMatcher sessionMatcher;
    protected SessionCreator sessionCreator;
    protected SessionEvaluator sessionEvaluator;

}
