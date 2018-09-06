package org.gbutil.fsm;

import org.gbutil.fsm.node.connected.ISMConnectedNode;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class FSMBuilder<L extends Enum<L>, S extends IState> {
    private FiniteStateMachine<L, S> mInstance;
    private Map<ISMConnectedNode<L, S>, Set<L>> mUnclosedEnds = new TreeMap<>();
    private L[] values;

    public FSMBuilder(String name, Class<L> cls, ISMConnectedNode<L, S> initialNode) {
        mInstance = new FiniteStateMachine<>(name, initialNode);
        values = cls.getEnumConstants();
    }

    private void updateEndsRegistry() {
        updateEndsRegistryRec(mInstance.mInitialNode);
    }

    private void updateEndsRegistryRec(ISMConnectedNode<L, S> current) {

    }

    private boolean isEndNodePresentAt(ISMConnectedNode<L, S> node, L langElement) {
        return !mUnclosedEnds.get(node).contains(langElement);
    }

    private boolean isOptionOpen(ISMConnectedNode<L, S> node, L langElement) {
        return !isEndNodePresentAt(node, langElement) && !node.hasConnectedNode(langElement);
    }
}
