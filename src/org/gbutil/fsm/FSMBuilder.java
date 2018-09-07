package org.gbutil.fsm;

import org.gbutil.Tuple.Tuple2;
import org.gbutil.fsm.node.ISMConnectedNode;
import org.gbutil.fsm.node.ISMNode;
import org.gbutil.fsm.node.SMEndNode;
import org.gbutil.graph.TrajanAlgorithm;

import java.util.*;

public class FSMBuilder<L extends Enum<L>, S extends IState> {
    private FiniteStateMachine<L, S> mInstance;
    private Map<ISMConnectedNode<L, S>, Set<L>> mUnclosedEnds = new TreeMap<>();
    private Map<ISMConnectedNode<L, S>, Set<L>> mEndNodes = new TreeMap<>();
    private List<Set<ISMNode<S>>> mLastCycleError = null;
    private final L[] values;

    public FSMBuilder(String name, Class<L> cls, ISMConnectedNode<L, S> initialNode) {
        mInstance = new FiniteStateMachine<>(name, initialNode);
        values = cls.getEnumConstants();
        mUnclosedEnds.put(initialNode, new TreeSet<>(Arrays.asList(values)));
        mEndNodes.put(initialNode, new TreeSet<>());
    }

    public boolean isEndNodePresentAt(List<L> path) {
        assert (path.size() != 0);

        ISMConnectedNode<L, S> current = mInstance.mInitialNode;
        int index = 0;

        while (!isEndNode(current, path.get(index))) {
            Optional<ISMNode<S>> next = current.getConnectedNode(path.get(index));

            if (!next.isPresent())
                return false;

            index++;
            current = (ISMConnectedNode<L, S>) next.get();

            if (index == path.size())
                throw new IllegalArgumentException("path is too short");
        }

        return true;
    }

    public boolean isEndNodePresentAt(S state) {
        ISMConnectedNode<L, S> current = mInstance.mInitialNode;

        while (!isEndNode(current, current.parse(state))) {
            Optional<ISMNode<S>> next = current.getConnectedNode(state);

            if (!next.isPresent()) return false;

            current = (ISMConnectedNode<L, S>) next.get();
        }

        return true;
    }

    public boolean attach(ISMConnectedNode<L, S> base, L langElement, ISMConnectedNode<L, S> attached, boolean force) {
        boolean status = addAt(base, langElement, attached, force);
        if (status) updateEndsRegistry();
        return status;
    }

    public boolean end(ISMConnectedNode<L, S> base, L langElement, SMEndNode<S> ending, boolean force) {

    }

    public Optional<List<Set<ISMNode<S>>>> getLastCyclesError() {
        return Optional.ofNullable(mLastCycleError);
    }

    private boolean addAt(ISMConnectedNode<L, S> base, L langElement, ISMConnectedNode<L, S> attached, boolean force) {
        assert (mUnclosedEnds.containsKey(base));
        if (base.hasConnectedNode(langElement) && !force) return false;

        ISMNode<S> swapped;
        boolean isEnd = false;
        if (!base.hasConnectedNode(langElement)) {
            swapped = base.setConnectedNode(langElement, attached).get();
        } else {
            if (isEndNode(base, langElement)) {
                mEndNodes.get(base).remove(langElement);
                isEnd = true;
            }
            swapped = base.setConnectedNode(langElement, attached).get();
            registerConnection((ISMConnectedNode<L, S>) swapped);
        }
        if (!isEnd) {
            if (!tryModifyingConnection(base, langElement, swapped)) return false;
            deregisterConnection(attached);
        }
        return true;
    }

    private boolean tryModifyingConnection(ISMConnectedNode<L, S> base, L langElement, ISMNode<S> swapped) {
        Tuple2<List<ISMNode<S>>, boolean[][]> trajanRepr = buildTrajanRepresentation();
        List<Set<ISMNode<S>>> stronglyConnectedComponents =
                TrajanAlgorithm.getStronglyConnectedComponents(trajanRepr.first, trajanRepr.second);

        if (stronglyConnectedComponents.size() != 1 || stronglyConnectedComponents.get(0).size() == 0) {
            base.setConnectedNode(langElement, swapped);
            mLastCycleError = stronglyConnectedComponents;
            return false;
        }

        return true;
    }

    private boolean endAt(ISMConnectedNode<L, S> base, L langElement, SMEndNode<S> ending, boolean force) {
        assert (mUnclosedEnds.containsKey(base));

        if (base.hasConnectedNode(langElement) && !force) return false;

        if (!base.hasConnectedNode(langElement) || isEndNode(base, langElement)) {
            base.setConnectedNode(langElement, ending);
        } else {
            ISMConnectedNode<L, S> swapped = (ISMConnectedNode<L, S>) base.setConnectedNode(langElement, ending).get();

        }

        mEndNodes.get(base).add(langElement);
        return true;
    }

    private boolean deregisterConnection(ISMConnectedNode<L, S> node) {
        for (ISMConnectedNode<L, S> other : mUnclosedEnds.keySet())
            if (other.isConnectedTo(node))
                return false;

        mUnclosedEnds.remove(node);
        mEndNodes.remove(node);
        return true;
    }

    private boolean registerConnection(ISMConnectedNode<L, S> node) {
        if (!mUnclosedEnds.containsKey(node) && mEndNodes.containsKey(node)) {
            mUnclosedEnds.put(node, new TreeSet<>(Arrays.asList(values)));
            mEndNodes.put(node, new TreeSet<>());
            return true;
        }
        return false;
    }

    private boolean updateEndsRegistry() {
        return updateEndsRegistry(mInstance.mInitialNode);
    }

    private boolean updateEndsRegistry(ISMConnectedNode<L, S> current) {
        if (mUnclosedEnds.get(current).size() == 0) return true;

        boolean isClosed = true;

        for (L langElement : values) {
            if (!current.hasConnectedNode(langElement)) {
                isClosed = false;
            } else if (!isEndNode(current, langElement)) {
                if (updateEndsRegistry((ISMConnectedNode<L, S>) current.getConnectedNode(langElement).get()))
                    mUnclosedEnds.get(current).remove(langElement);
                else
                    isClosed = false;
            } else {
                mUnclosedEnds.get(current).remove(langElement);
            }
        }

        return isClosed;
    }

    private boolean isEndNode(ISMConnectedNode<L, S> node, L option) {
        return mEndNodes.get(node).contains(option);
    }

    private Tuple2<List<ISMNode<S>>, boolean[][]> buildTrajanRepresentation() {
        boolean[][] ret = new boolean[mUnclosedEnds.keySet().size()][mUnclosedEnds.keySet().size()];
        List<ISMNode<S>> allNodes = new LinkedList<>(mUnclosedEnds.keySet());
        for (int i = 0; i < allNodes.size(); i++) {
            if (allNodes.get(i) instanceof ISMConnectedNode)
                for (int j = 0; j < allNodes.size(); j++)
                    ret[i][j] = ((ISMConnectedNode<L, S>) allNodes.get(i)).isConnectedTo(allNodes.get(j));
        }
        return new Tuple2<>(allNodes, ret);
    }
}
