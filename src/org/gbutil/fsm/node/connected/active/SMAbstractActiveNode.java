package org.gbutil.fsm.node.connected.active;

import org.gbutil.fsm.node.connected.SMConnectedNode;

/**
 * A node with a callback, which will be executed when the node is called
 *
 * @param <L> Language set (type of possible connections)
 * @param <S> State argument (type of the argument the fsm receives)
 */
public abstract class SMAbstractActiveNode<L, S> extends SMConnectedNode<L, S> {

    public SMAbstractActiveNode(String name) {
        super(name);
    }

    @Override
    public boolean execute(S state) {
        executeNode(state);
        return super.execute(state);
    }

    public abstract void executeNode(S state);
}
