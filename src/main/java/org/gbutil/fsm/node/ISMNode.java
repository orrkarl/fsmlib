package org.gbutil.fsm.node;

import java.io.Serializable;

/**
 * Most abstract node possible.
 *
 * @param <S> State argument (type of the argument the fsm receives)
 */
public interface ISMNode<S> extends Serializable, Comparable<ISMNode<S>> {
    /**
     *
     * @return this node's name
     */
    String getNodeName();

    /**
     *
     * @return does this node have outgoing connections to other nodes
     */
    boolean isEndNode();

    /**
     * executes this node with given argument, may call other nodes
     * @param state state argument
     * @return result of this execution
     */
    boolean execute(S state);

    @Override
    default int compareTo(ISMNode<S> o) {
        return new Integer(hashCode()).compareTo(o.hashCode());
    }
}
