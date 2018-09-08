package org.gbutil.fsm.node;

import org.gbutil.fsm.IState;

import java.io.Serializable;

public interface ISMNode<S extends IState> extends Serializable, Comparable<ISMNode<S>> {
    String getNodeName();

    boolean isEndNode();
    boolean execute(S state);

    @Override
    default int compareTo(ISMNode<S> o) {
        return new Integer(hashCode()).compareTo(o.hashCode());
    }
}
