package org.gbutil.fsm.node.connected;

import org.gbutil.fsm.IState;
import org.gbutil.fsm.node.ISMNode;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public abstract class SMConnectedNode<L, S extends IState> implements ISMConnectedNode<L, S> {
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
    public Optional<ISMNode<S>> setConnectedNode(L langElement, ISMNode<S> node) {
        return Optional.ofNullable(node == null ? mNodeMap.remove(langElement) : mNodeMap.put(langElement, node));
    }

    @Override
    public boolean isConnectedTo(ISMNode<S> node) {
        return mNodeMap.containsValue(node);
    }

    @Override
    public boolean hasConnectedNode(L langElement) {
        return mNodeMap.containsKey(langElement);
    }

    @Override
    public Collection<ISMNode<S>> getAllConnected() {
        return mNodeMap.values();
    }

    @Override
    public final boolean isEndNode() {
        return false;
    }

    @Override
    public String getNodeName() {
        return mName;
    }
}
