package me.david.webapi.session.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.david.webapi.session.SessionCreator;
import me.david.webapi.session.SessionEvaluator;
import me.david.webapi.session.SessionMatcher;
import me.david.webapi.session.SessionSystem;

@Getter
@AllArgsConstructor
public class SessionSystemImpl implements SessionSystem {

    protected SessionMatcher sessionMatcher;
    protected SessionCreator sessionCreator;
    protected SessionEvaluator sessionEvaluator;

}
