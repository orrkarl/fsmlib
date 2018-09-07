package org.gbutil.fsm.node;

import org.gbutil.fsm.IState;

import java.util.function.Function;

public abstract class SMBridgeNode<L extends Enum<L>, S extends IState> extends SMConnectedNode<L, S> {

    public SMBridgeNode(String name) {
        super(name);
    }

    public SMBridgeNode(String name, Function<S, L> parser) {
        super(name);
    }


    @Override
    public final boolean activateNextNode(S state) {
        return super.activateNextNode(state);
    }

    @Override
    public final boolean execute(S state) {
        return super.execute(state);
    }
}
