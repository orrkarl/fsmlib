package org.gbutil.fsm.node.end;

import org.gbutil.fsm.node.ISMNode;

/**
 * An end node, which ends a given chain. This node doesn't call any other
 * one at it's own execution
 *
 * @param <S> State argument (type of the argument the fsm receives)
 */
public abstract class SMEndNode<S> implements ISMNode<S> {
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
