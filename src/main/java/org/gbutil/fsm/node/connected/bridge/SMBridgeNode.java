package org.gbutil.fsm.node.connected.bridge;

import org.gbutil.fsm.node.connected.SMConnectedNode;

/**
 * @param <L> Language set (type of possible connections)
 * @param <S> State argument (type of the argument the fsm receives)
 * @see org.gbutil.fsm.node.connected.ISMConnectedNode
 * A connected node which executes nothing and just operates as a bridge in a chain
 * Use this where you don't want the node to execute anything
 */
public abstract class SMBridgeNode<L, S> extends SMConnectedNode<L, S> {

    public SMBridgeNode(String name) {
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
