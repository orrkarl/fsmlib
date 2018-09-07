package org.gbutil.fsm.node;

import org.gbutil.fsm.IState;

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
    public String getNodeName() {
        return mName;
    }
}
