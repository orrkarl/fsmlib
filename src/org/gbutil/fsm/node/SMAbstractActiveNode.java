package org.gbutil.fsm.node;

import org.gbutil.fsm.IState;

public abstract class SMAbstractActiveNode<L extends Enum<L>, S extends IState> extends SMConnectedNode<L, S> {

    public SMAbstractActiveNode(String name) {
        super(name);
    }

    @Override
    public final boolean activateNextNode(S state) {
        return super.activateNextNode(state);
    }

    @Override
    public boolean execute(S state) {
        executeNode(state);
        return super.execute(state);
    }

    public abstract void executeNode(S state);
}
