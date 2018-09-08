package org.gbutil.fsm.node.end;

import java.util.function.Consumer;

/**
 * End node which also executes a given callback
 *
 * @param <S> State argument (type of the argument the fsm receives)
 * @see SMEndNode
 */
public class SMEndCallbackNode<S> extends SMEndNode<S> {
    private Consumer<S> mCallback;

    /**
     *
     * @param name name assigned to this node
     * @param value the value of this node
     * @param callback given callback to be executed
     */
    public SMEndCallbackNode(String name, boolean value, Consumer<S> callback) {
        super(name, value);
        mCallback = callback;
    }

    @Override
    public boolean execute(S state) {
        mCallback.accept(state);
        return asBoolean();
    }
}
