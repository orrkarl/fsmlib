package org.gbutil.fsm.node.connected;

import org.apache.logging.log4j.LogManager;
import org.gbutil.fsm.node.ISMNode;

import java.util.Collection;
import java.util.Optional;

/**
 * A connected node, i.e node which is connected to others (unlike an {@link org.gbutil.fsm.node.end.SMEndNode end node})
 * This is the most important part of any chain
 * <p>
 * <p>
 * this node receives the state argument in {@link ISMConnectedNode#execute(Object)}, uses {@link ISMConnectedNode#parse}
 * to get the next node, and than returns the next node's {@link ISMNode#execute(Object)} result
 * </p>
 *
 * @param <L> Language set (type of possible connections)
 * @param <S> State argument (type of the argument the fsm receives)
 */
public interface ISMConnectedNode<L, S> extends ISMNode<S> {
    /**
     *
     * @param langElement option which leads to the node
     * @return the node matching {@code langElement}, null otherwise
     */
    Optional<ISMNode<S>> getConnectedNode(L langElement);

    /**
     * @param langElement option which leads to the node
     * @param node        node which will be matched to given option
     * @return previous node which matched this option (null if there was none)
     */
    Optional<ISMNode<S>> setConnectedNode(L langElement, ISMNode<S> node);

    /**
     * @param node other node
     * @return is this node connected to other
     */
    boolean isConnectedTo(ISMNode<S> node);

    /**
     *
     * @param langElement option which is checked
     * @return does given option has attached node
     */
    default boolean hasConnectedNode(L langElement) {
        return getConnectedNode(langElement).isPresent();
    }

    /**
     * @return all node which this one may lead to
     */
    Collection<ISMNode<S>> getAllConnected();

    /**
     * The most important function here. The parer which converts given state to the option, and thus to the next node
     * @param state given state argument
     * @return option parsed from given state
     */
    L parse(S state);

    /**
     * Calls the node deduced from given state, throws {@link DetachedStateException} if none was found.
     * @see ISMConnectedNode#parse(Object)
     * @param state state argument
     * @return result of execution of the next node
     */
    default boolean activateNextNode(S state) {
        L parsed = parse(state);
        Optional<ISMNode<S>> nextNode = getConnectedNode(parsed);
        LogManager.getLogger("gbutil.fsm").trace("parsing: '" + state + "' -> '" + parsed + "'");
        if (!nextNode.isPresent())
            throw new DetachedStateException(getNodeName(), parsed.toString());
        return nextNode.get().execute(state);
    }

    @Override
    default boolean execute(S state) {
        LogManager.getLogger("gbutil.fsm").trace("executing node '" + getNodeName() + "'");
        return activateNextNode(state);
    }
}
