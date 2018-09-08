package org.gbutil.fsm.node;

import org.gbutil.fsm.IState;
import org.gbutil.fsm.node.connected.ISMConnectedNode;
import org.gbutil.fsm.node.connected.active.SMActiveNodeImpl;
import org.gbutil.fsm.node.connected.bridge.SMBridgeNodeImpl;
import org.gbutil.fsm.node.end.SMEndCallbackNode;
import org.gbutil.fsm.node.end.SMEndInactiveNode;
import org.gbutil.fsm.node.end.SMEndNode;

import java.util.function.Consumer;
import java.util.function.Function;

public class NodeFactory {
    public static <L, S extends IState> ISMConnectedNode<L, S> mkConnectedNode(
            String name,
            Function<S, L> parser,
            Consumer<S> callback) {
        return new SMActiveNodeImpl<>(name, parser, callback);
    }

    public static <L, S extends IState> ISMConnectedNode<L, S> mkConnectedNode(
            String name,
            Function<S, L> parser) {
        return new SMBridgeNodeImpl<>(name, parser);
    }

    public static <S extends IState> SMEndNode<S> mkEndNode(String name, boolean value, Consumer<S> callback) {
        return new SMEndCallbackNode<>(name, value, callback);
    }

    public static <S extends IState> SMEndNode<S> mkEndNode(String name, boolean value) {
        return new SMEndInactiveNode<>(name, value);
    }

    public static <S extends IState> SMEndNode<S> mkEndNode(boolean value) {
        return new SMEndInactiveNode<>(value);
    }
}
