package org.gbutil.fsm.node;

import org.gbutil.fsm.IState;

import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

public final class SMConnectedImmutableNode<L extends Enum<L>, S extends IState> implements ISMConnectedNode<L, S> {

    private final ISMConnectedNode<L, S> mWrapped;

    public SMConnectedImmutableNode(ISMConnectedNode<L, S> node) {
        mWrapped = node;
    }

    @Override
    public String getNodeName() {
        return mWrapped.getNodeName();
    }

    @Override
    public Optional<ISMNode<S>> getConnectedNode(L langElement) {
        return mWrapped.getConnectedNode(langElement).map(
                node -> node instanceof SMEndNode ? node
                        : new SMConnectedImmutableNode<>((ISMConnectedNode<L, S>) node));
    }

    @Override
    public Optional<ISMNode<S>> setConnectedNode(L langElement, ISMNode<S> node) {
        return Optional.empty();
    }

    @Override
    public boolean hasConnectedNode(L langElement) {
        return mWrapped.hasConnectedNode(langElement);
    }

    @Override
    public boolean isConnectedTo(ISMNode<S> node) {
        return mWrapped.isConnectedTo(node);
    }

    @Override
    public Set<L> getConnectorsTo(ISMNode<S> node) {
        return new TreeSet<>();
    }

    @Override
    public L parse(S state) {
        return mWrapped.parse(state);
    }

    @Override
    public boolean activateNextNode(S state) {
        return mWrapped.activateNextNode(state);
    }

    @Override
    public boolean execute(S state) {
        return mWrapped.execute(state);
    }
}
