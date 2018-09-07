package org.gbutil.fsm.node;

import org.gbutil.fsm.IState;

import java.util.Optional;
import java.util.Set;

public interface ISMConnectedNode<L extends Enum<L>, S extends IState> extends ISMNode<S> {
    Optional<ISMNode<S>> getConnectedNode(L langElement);

    Optional<ISMNode<S>> setConnectedNode(L langElement, ISMNode<S> node);

    boolean isConnectedTo(ISMNode<S> node);
    boolean hasConnectedNode(L langElement);

    Set<L> getConnectorsTo(ISMNode<S> node);
    L parse(S state);

    default Optional<ISMNode<S>> getConnectedNode(S state) {
        return getConnectedNode(parse(state));
    }

    default boolean activateNextNode(S state) {
        Optional<ISMNode<S>> nextNode = getConnectedNode(parse(state));
        return nextNode.isPresent() && nextNode.get().execute(state);
    }

    @Override
    default boolean execute(S state) {
        return activateNextNode(state);
    }
}
