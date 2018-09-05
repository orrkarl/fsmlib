package org.gbutil.fsm.node.connected;

import org.gbutil.fsm.IState;
import org.gbutil.fsm.node.ISMNode;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public abstract class SMConnectedNode<L extends Enum<L>, S extends IState> implements ISMConnectedNode<L, S> {
    protected Map<L, ISMNode<S>> mNodeMap;
    protected String mName;

    public SMConnectedNode(String name) {
        mNodeMap = new TreeMap<>();
        mName = name;
    }

    @Override
    public Optional<ISMNode<S>> getConnectedNode(L langElement) {
        return Optional.ofNullable(mNodeMap.get(langElement));
    }

    @Override
    public boolean setConnectedNode(L langElement, ISMNode<S> node) {
        if (node == null) return false;
        mNodeMap.put(langElement, node);
        return true;
    }

    @Override
    public boolean hasConnectedNode(L langElement) {
        return mNodeMap.containsKey(langElement);
    }

    @Override
    public String getNodeName() {
        return mName;
    }
}
