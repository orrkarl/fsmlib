package org.gbutil.fsm;


import org.apache.logging.log4j.LogManager;
import org.gbutil.Tuple.Tuple2;
import org.gbutil.fsm.node.ISMNode;
import org.gbutil.fsm.node.NodeFactory;
import org.gbutil.fsm.node.connected.DetachedStateException;
import org.gbutil.fsm.node.connected.ISMConnectedNode;
import org.gbutil.fsm.node.end.SMEndNode;
import org.gbutil.graph.TrajanAlgorithm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;

/**
 * Wrapper for a connected node. represents the state machine itself, and adds some utility beyond a simple node
 * <p>
 * While the basis ({@link FiniteStateMachine#apply(Object)}) is ensured to work as long as you inherit the right
 * interfaces and the code manages to compile, the rest of the features (such as auto-attaching open nodes to a given
 * end node) will not work if an unexpected node type, i.e not {@link ISMConnectedNode} or {@link SMEndNode}, is given.
 * </p>
 * Refer to {@link org.gbutil.fsm} for a general usage guide.
 *
 * @param <L> Language set (type of possible connections)
 * @param <S> State argument (type of the argument the fsm receives)
 * @see org.gbutil.fsm.node.ISMNode
 * @see org.gbutil.fsm
 */
public class FiniteStateMachine<L, S> implements Function<S, Boolean> {
    private ISMConnectedNode<L, S> mInitialNode;
    private String mName;

    /**
     *
     * @param name Name of this fsm
     * @param initialNode The root of this fsm
     */
    public FiniteStateMachine(String name, ISMConnectedNode<L, S> initialNode) {
        mInitialNode = initialNode;
        mName = name;
    }

    /**
     * Imports an fsm from given path
     * @param path path to file which contains this fsm
     * @param <L> Language set (type of possible connections)
     * @param <S> State argument (type of the argument the fsm receives)
     * @return The fsm contained at this file, null if an error occurred
     */
    @SuppressWarnings("unchecked")
    public static <L, S> FiniteStateMachine<L, S> importFSM(String path) {
        FileInputStream fileIn = null;
        ObjectInputStream objIn = null;
        try {
            fileIn = new FileInputStream(path);
            objIn = new ObjectInputStream(fileIn);

            return (FiniteStateMachine<L, S>) objIn.readObject();
        } catch (FileNotFoundException e) {
            LogManager.getLogger("gbutil.fsm").error("FSM resource file at '" + path + "' not found");
        } catch (IOException e) {
            LogManager.getLogger("gbutil.fsm").fatal("Unexpected error occurred while trying to open resource file", e);
        } catch (ClassNotFoundException | ClassCastException e) {
            LogManager.getLogger("gbutil.fsm").error("Unexpected class found in resource file", e);
        } finally {
            try {
                if (fileIn != null) fileIn.close();
                if (objIn != null) objIn.close();
            } catch (IOException e) {
                LogManager.getLogger("gbutil.fsm").fatal(e);
            }
        }

        return null;
    }

    /**
     * Activates this fsm
     * @param state given argument
     * @return result of activating the root on given state
     */
    @Override
    public Boolean apply(S state) {
        LogManager.getLogger("gbutil.fsm").trace("Activating fsm '" + mName + "' with state '" + state + "'");
        try {
            return mInitialNode.execute(state);
        } catch (DetachedStateException e) {
            LogManager.getLogger("gbutil.fsm").error(
                    String.format(
                            "Option '%s' of node '%s', generated by state '%s' from '%s' is detached",
                            e.option, e.nodeName, state, toString()),
                    e);
            return false;
        }
    }

    @Override
    public String toString() {
        return String.format("FiniteStateMachine{mInitialNode=%s, mName='%s'}", mInitialNode, mName);
    }

    /**
     * Connects all of the unclosed nodes in this fsm to a end 'false' node
     * Use it to ensure that your fsm won't throw {@link DetachedStateException}
     * @see FiniteStateMachine#attachOpenedTo(SMEndNode, Object[])
     * @param possibleOptions all of the possible options for a member of the language
     */
    public void attachOpenedTo(L[] possibleOptions) {
        attachOpenedTo(NodeFactory.mkEndNode(false), possibleOptions);
    }

    /**
     * Connects all of the unclosed nodes in this fsm to given node
     * @see FiniteStateMachine#attachOpenedTo(Object[])
     * @param ending given end node
     * @param possibleValues all of the possible options for a member of the language
     */
    public void attachOpenedTo(SMEndNode<S> ending, L[] possibleValues) {
        attachOpenedToRec(mInitialNode, ending, possibleValues, new TreeSet<>());
    }

    /**
     * Finds all the nodes which aren't completely closed
     * @param possibleValues all of the possible options for a member of the language
     * @return List of all open nodes
     */
    public List<ISMConnectedNode<L, S>> findOpenedNodes(L[] possibleValues) {
        List<ISMConnectedNode<L, S>> ret = new LinkedList<>();
        Set<ISMConnectedNode<L, S>> observed = new TreeSet<>();
        findOpenedNodes(mInitialNode, observed, possibleValues, ret);
        return ret;
    }

    /**
     * Finds all the cycles in this fsm
     * @see TrajanAlgorithm
     * @return list of cycles (strongly connected components) in this graph.
     */
    public List<Set<ISMConnectedNode<L, S>>> findCycles() {
        return TrajanAlgorithm.getStronglyConnectedComponents(buildTrajanRepr(mInitialNode));
    }

    /**
     * @see FiniteStateMachine#findCycles()
     * @return are there any cycles in the fsm
     */
    public boolean checkForCycles() {
        return !TrajanAlgorithm.hasStronglyConnectedComponents(buildTrajanRepr(mInitialNode));
    }

    private static <L, S> Tuple2<List<ISMConnectedNode<L, S>>, boolean[][]> buildTrajanRepr(ISMConnectedNode<L, S> initial) {
        List<ISMConnectedNode<L, S>> graph = new LinkedList<>();
        buildGraph(initial, graph);
        boolean[][] connections = new boolean[graph.size()][graph.size()];
        for (int i = 0; i < connections.length; i++)
            for (int j = 0; j < connections.length; j++)
                connections[i][j] = graph.get(i).isConnectedTo(graph.get(j));

        return new Tuple2<>(graph, connections);
    }

    private static <L, S> void findOpenedNodes(ISMConnectedNode<L, S> current, Set<ISMConnectedNode<L, S>> observed, L[] options, List<ISMConnectedNode<L, S>> opened) {
        if (observed.contains(current)) return;
        observed.add(current);

        for (L option : options) {
            if (!current.getConnectedNode(option).isPresent() && !opened.contains(current))
                opened.add(current);
            else if (!current.getConnectedNode(option).get().isEndNode())
                findOpenedNodes((ISMConnectedNode<L, S>) current.getConnectedNode(option).get(), observed, options, opened);
        }
    }

    private static <L, S> void buildGraph(ISMConnectedNode<L, S> current, List<ISMConnectedNode<L, S>> graph) {
        if (graph.contains(current)) return;

        graph.add(current);
        for (ISMNode<S> node : current.getAllConnected())
            if (!node.isEndNode())
                buildGraph((ISMConnectedNode<L, S>) node, graph);
    }

    private static <L, S> void attachOpenedToRec(ISMNode<S> current, SMEndNode<S> ending, L[] options, Set<ISMNode<S>> observed) {
        if (current.isEndNode()) return;
        if (observed.contains(current)) return;

        observed.add(current);

        ISMConnectedNode<L, S> currentNode = (ISMConnectedNode<L, S>) current;

        for (L l : options) {
            if (currentNode.getConnectedNode(l).isPresent())
                attachOpenedToRec(currentNode.getConnectedNode(l).get(), ending, options, observed);
            else
                currentNode.setConnectedNode(l, ending);
        }

    }
}
