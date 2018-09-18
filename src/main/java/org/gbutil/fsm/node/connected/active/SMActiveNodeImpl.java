package org.gbutil.fsm.node.connected.active;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Active node implementation
 *
 * @param <L> Language set (type of possible connections)
 * @param <S> State argument (type of the argument the fsm receives)
 */
public final class SMActiveNodeImpl<L, S> extends SMActiveNode<L, S> {
    private Consumer<S> mCallback;

    /**
     *
     * @param name name assigned to this node
     * @param parser function used as this node's {@link org.gbutil.fsm.node.connected.ISMConnectedNode#parse(Object) parser}
     * @param callback callback which will be executed
     */
    public SMActiveNodeImpl(String name, Function<S, L> parser, Consumer<S> callback) {
        super(name, parser);
        mCallback = callback;
    }

    @Override
    public void executeNode(S state) {
        mCallback.accept(state);
    }
}
