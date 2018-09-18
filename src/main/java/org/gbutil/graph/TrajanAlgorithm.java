package org.gbutil.graph;

import org.gbutil.Tuple.Tuple2;

import java.util.*;

/**
 * Implementation of Trajan's algorithm for finding strongly connected components in a directioned graph.
 *
 * @param <T> Type of the objects used as nodes
 * @see <a href="https://en.wikipedia.org/wiki/Tarjan's_strongly_connected_components_algorithm">Wikipedia</a>
 */
public class TrajanAlgorithm<T> {
    private static class TrajanNode {
        int node;
        int index = -1;
        int lowestLink = -1;
        boolean onStack = false;

        TrajanNode(int node) {
            this.node = node;
        }

        boolean isObserved() {
            return index > 0 && lowestLink > 0;
        }
    }

    private List<T> mNodes;
    private TrajanNode[] mTrajanNodes;
    private boolean[][] mConnectionMatrix;
    private int index = 0;
    private Stack<TrajanNode> nodeStack = new Stack<>();
    private List<Set<T>> mStronglyConnected = new LinkedList<>();

    private TrajanAlgorithm(List<T> nodes, boolean[][] connectionMatrix) {
        mNodes = nodes;
        mConnectionMatrix = connectionMatrix;

        mTrajanNodes = new TrajanNode[nodes.size()];
        for (int i = 0; i < nodes.size(); i++) {
            mTrajanNodes[i] = new TrajanNode(i);
        }
    }

    private TrajanAlgorithm(Tuple2<List<T>, boolean[][]> graph) {
        this(graph.first, graph.second);
    }

    /**
     * Executes Trajan's algorithm on a graph represented as a connection matrix.
     * <p>
     * Connection matrix is a boolean matrix which represents the connections in the graph: cell (i,j) in it should be
     * true iff node i is connected to node j (consequentially, it has to be a square matrix of the size of {@code nodes})
     * </p>
     *
     * @param nodes            All of the nodes in the graph
     * @param connectionMatrix The connection matrix representation of the graph
     * @param <T>              Type of graph nodes
     * @return A list of all the strongly connected components of given graph (every component is a set of nodes)
     */
    public static <T> List<Set<T>> getStronglyConnectedComponents(List<T> nodes, boolean[][] connectionMatrix) {
        assert (connectionMatrix.length == nodes.size());
        assert (Arrays.stream(connectionMatrix).filter(arr -> arr.length != nodes.size()).count() == 0);
        TrajanAlgorithm<T> algorithm = new TrajanAlgorithm<>(nodes, connectionMatrix);
        return algorithm.executeAlgorithm();
    }

    /**
     * @see TrajanAlgorithm#getStronglyConnectedComponents(List, boolean[][])
     * @param graph The representation of the graph, as a tuple
     * @param <T> Type of graph nodes
     * @return A list of all the strongly connected components of given graph (every component is a set of nodes)
     */
    public static <T> List<Set<T>> getStronglyConnectedComponents(Tuple2<List<T>, boolean[][]> graph) {
        return getStronglyConnectedComponents(graph.first, graph.second);
    }

    /**
     * @see TrajanAlgorithm#getStronglyConnectedComponents(List, boolean[][])
     * @param nodes All of the nodes in the graph
     * @param connectionMatrix The connection matrix representation of the graph
     * @param <T> Type of graph nodes
     * @return does the graph has any strongly connected components
     */
    public static <T> boolean hasStronglyConnectedComponents(List<T> nodes, boolean[][] connectionMatrix) {
        List<Set<T>> res = getStronglyConnectedComponents(nodes, connectionMatrix);
        return (res.size() == 1 && res.get(0).size() != 0) || res.size() == 0;
    }

    /**
     * @see TrajanAlgorithm#hasStronglyConnectedComponents(List, boolean[][])
     * @param graph The representation of the graph, as a tuple
     * @param <T> Type of graph nodes
     * @return does the graph has any strongly connected components
     */
    public static <T> boolean hasStronglyConnectedComponents(Tuple2<List<T>, boolean[][]> graph) {
        return hasStronglyConnectedComponents(graph.first, graph.second);
    }

    /**
     * Actual execution of the algorithm
     * @param node Current node being run
     */
    private void findStronglyConnectedRec(TrajanNode node) {
        setupNode(node);
        Set<T> ret = new TreeSet<>();
        for (TrajanNode otherNode : mTrajanNodes) {
            if (areNodesConnected(node, otherNode)) {
                if (!otherNode.isObserved()) {
                    findStronglyConnectedRec(otherNode);
                    node.lowestLink = Math.min(node.lowestLink, otherNode.lowestLink);
                } else if (otherNode.onStack) {
                    node.lowestLink = Math.min(node.lowestLink, otherNode.index);
                }
            }
        }
        if (node.lowestLink == node.index) {
            TrajanNode other;
            do {
                other = nodeStack.pop();
                other.onStack = false;
                ret.add(mNodes.get(other.node));
            } while (other != node);
        }
        if (ret.size() > 1) mStronglyConnected.add(ret);
    }

    private void setupNode(TrajanNode node) {
        node.index = index;
        node.lowestLink = index;
        index += 1;
        node.onStack = true;
        nodeStack.push(node);
    }

    private boolean areNodesConnected(TrajanNode src, TrajanNode dest) {
        return mConnectionMatrix[src.node][dest.node];
    }

    private List<Set<T>> executeAlgorithm() {
        for (TrajanNode node : mTrajanNodes)
            if (!node.isObserved())
                findStronglyConnectedRec(node);
        return mStronglyConnected;
    }
}