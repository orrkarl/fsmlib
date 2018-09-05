package org.gbutil.fsm.node;

import org.gbutil.fsm.IState;

import java.io.Serializable;

public interface ISMNode<S extends IState> extends Serializable {
    String getNodeName();
    boolean execute(S state);
}
