package org.gbutil.fsm.node.connected;

import org.gbutil.fsm.node.ISMNode;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/**
 * Implementation of a connected node using a node {@link Map}
 *
 * @param <L> Language set (type of possible connections)
 * @param <S> State argument (type of the argument the fsm receives)
 * @see ISMConnectedNode
 */
public abstract class SMConnectedNode<L, S> implements ISMConnectedNode<L, S> {
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
