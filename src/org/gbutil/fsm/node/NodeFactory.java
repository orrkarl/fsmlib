package org.gbutil.fsm.node;

import org.gbutil.fsm.node.connected.ISMConnectedNode;
import org.gbutil.fsm.node.connected.active.SMActiveNodeImpl;
import org.gbutil.fsm.node.connected.bridge.SMBridgeNodeImpl;
import org.gbutil.fsm.node.end.SMEndCallbackNode;
import org.gbutil.fsm.node.end.SMEndInactiveNode;
import org.gbutil.fsm.node.end.SMEndNode;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Factory for node types, used to hide unnecessary implementation details.
 */
public class NodeFactory {
    /**
     * @param name     node name
     * @param parser   parsing function
     * @param callback callback too be called on every execution
     * @param <L>      Language set (type of possible connections)
     * @param <S>      State argument (type of the argument the fsm receives)
     * @return active and connected node with given functions
     * @see SMActiveNodeImpl
     */
    public static <L, S> ISMConnectedNode<L, S> mkConnectedNode(
            String name,
            Function<S, L> parser,
            Consumer<S> callback) {
        return new SMActiveNodeImpl<>(name, parser, callback);
    }

    /**
     * @param name   node name
     * @param parser parsing function
     * @param <L>    Language set (type of possible connections)
     * @param <S>    State argument (type of the argument the fsm receives)
     * @return inactive, connected nodes with given functions
     * @see SMBridgeNodeImpl
     */
    public static <L, S> ISMConnectedNode<L, S> mkConnectedNode(
            String name,
            Function<S, L> parser) {
        return new SMBridgeNodeImpl<>(name, parser);
    }

    /**
     * @param name     node name
     * @param value    boolean value of given node
     * @param callback callback too be called on every execution
     * @param <S>      State argument (type of the argument the fsm receives)
     * @return active node without connections
     * @see SMEndCallbackNode
     */
    public static <S> SMEndNode<S> mkEndNode(String name, boolean value, Consumer<S> callback) {
        return new SMEndCallbackNode<>(name, value, callback);
    }

    /**
     * @param name  node name
     * @param value boolean value of given node
     * @param <S>   State argument (type of the argument the fsm receives)
     * @return end node containing given name and value
     * @see SMEndNode
     */
    public static <S> SMEndNode<S> mkEndNode(String name, boolean value) {
        return new SMEndInactiveNode<>(name, value);
    }

    /**
     * @param value boolean value of given node
     * @param <S>   State argument (type of the argument the fsm receives)
     * @return end node containing given value
     * @see NodeFactory#mkEndNode
     */
    public static <S> SMEndNode<S> mkEndNode(boolean value) {
        return new SMEndInactiveNode<>(value);
    }
}
