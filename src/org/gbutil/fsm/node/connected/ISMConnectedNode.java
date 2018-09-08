package org.gbutil.fsm.node.connected;

import org.apache.logging.log4j.LogManager;
import org.gbutil.fsm.IState;
import org.gbutil.fsm.node.ISMNode;

import java.util.Collection;
import java.util.Optional;

public interface ISMConnectedNode<L, S extends IState> extends ISMNode<S> {
    Optional<ISMNode<S>> getConnectedNode(L langElement);

    Optional<ISMNode<S>> setConnectedNode(L langElement, ISMNode<S> node);

    boolean isConnectedTo(ISMNode<S> node);
    boolean hasConnectedNode(L langElement);

    Collection<ISMNode<S>> getAllConnected();

    L parse(S state);


    default boolean activateNextNode(S state) {
        L parsed = parse(state);
        Optional<ISMNode<S>> nextNode = getConnectedNode(parsed);
        LogManager.getRootLogger().trace("parsing: '" + state + "' -> '" + parsed + "'");
        if (!nextNode.isPresent())
            throw new DetachedStateException(getNodeName(), parsed.toString());
        return nextNode.get().execute(state);
    }

    @Override
    default boolean execute(S state) {
        LogManager.getRootLogger().trace("executing node '" + getNodeName() + "'");
        return activateNextNode(state);
    }
}
