package org.gbutil.fsm.node.connected.active;

import org.gbutil.fsm.IState;

import java.util.function.Function;

public abstract class SMActiveNode<L, S extends IState> extends SMAbstractActiveNode<L, S> {
    private Function<S, L> mParser;

    public SMActiveNode(String name, Function<S, L> parser) {
        super(name);
        this.mParser = parser;
    }

    @Override
    public L parse(S state) {
        return mParser.apply(state);
    }

}
