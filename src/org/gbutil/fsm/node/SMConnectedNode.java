package org.gbutil.fsm.node;

import org.gbutil.fsm.IState;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

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
    public Optional<ISMNode<S>> setConnectedNode(L langElement, ISMNode<S> node) {
        return Optional.ofNullable(node == null ? mNodeMap.remove(langElement) : mNodeMap.put(langElement, node));
    }

    @Override
    public boolean isConnectedTo(ISMNode<S> node) {
        return mNodeMap.containsValue(node);
    }

    @Override
    public Set<L> getConnectorsTo(ISMNode<S> node) {
        return mNodeMap.keySet().stream().filter(langElement -> mNodeMap.get(langElement) == node).
                collect(Collectors.toSet());
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
