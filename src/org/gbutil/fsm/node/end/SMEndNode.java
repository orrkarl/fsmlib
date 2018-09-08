package org.gbutil.fsm.node.end;

import org.gbutil.fsm.IState;
import org.gbutil.fsm.node.ISMNode;

public abstract class SMEndNode<S extends IState> implements ISMNode<S> {
    protected boolean mValue;
    protected String mName;

    protected SMEndNode(String name, boolean value) {
        mName = name;
        mValue = value;
    }

    public boolean asBoolean() {
        return mValue;
    }

    @Override
    public final boolean isEndNode() {
        return true;
    }

    @Override
    public String getNodeName() {
        return mName;
    }
}
