package org.gbutil.fsm.node.connected.bridge;

import org.gbutil.fsm.IState;

import java.util.function.Function;

public final class SMBridgeNodeImpl<L extends Enum<L>, S extends IState> extends SMBridgeNode<L, S> {
    private Function<S, L> mParser;

    public SMBridgeNodeImpl(String name, Function<S, L> parser) {
        super(name, parser);
        mParser = parser;
    }

    @Override
    public L parse(S state) {
        return mParser.apply(state);
    }
}
