package org.gbutil.fsm.node.connected;

import org.gbutil.fsm.IState;
import org.gbutil.fsm.node.ISMNode;

import java.util.Optional;

public interface ISMConnectedNode<L extends Enum<L>, S extends IState> extends ISMNode<S> {
    Optional<ISMNode<S>> getConnectedNode(L langElement);
    boolean setConnectedNode(L langElement, ISMNode<S> node);
    boolean hasConnectedNode(L langElement);
    L parse(S state);

    default boolean activateNextNode(S state) {
        Optional<ISMNode<S>> nextNode = getConnectedNode(parse(state));
        return nextNode.isPresent() && nextNode.get().execute(state);
    }

    @Override
    default boolean execute(S state) {
        return activateNextNode(state);
    }
}
