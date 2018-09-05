package org.gbutil.fsm.node.connected.active;

import org.gbutil.fsm.IState;

import java.util.function.Consumer;
import java.util.function.Function;

public final class SMActiveNodeImpl<L extends Enum<L>, S extends IState> extends SMActiveNode<L, S> {
    private Consumer<S> mCallback;

    public SMActiveNodeImpl(String name, Function<S, L> parser, Consumer<S> callback) {
        super(name, parser);
        mCallback = callback;
    }

    @Override
    public void executeNode(S state) {
        mCallback.accept(state);
    }
}
