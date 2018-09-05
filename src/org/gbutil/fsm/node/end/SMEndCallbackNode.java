package org.gbutil.fsm.node.end;

import org.gbutil.fsm.IState;

import java.util.function.Consumer;

public class SMEndCallbackNode<S extends IState> extends SMEndNode<S> {
    private Consumer<S> mCallback;

    public SMEndCallbackNode(String name, boolean value, Consumer<S> callback) {
        super(name, value);
        mCallback = callback;
    }

    @Override
    public boolean execute(S state) {
        mCallback.accept(state);
        return asBoolean();
    }
}
