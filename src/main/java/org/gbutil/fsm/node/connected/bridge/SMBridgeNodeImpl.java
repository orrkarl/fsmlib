package org.gbutil.fsm.node.connected.bridge;

import org.apache.logging.log4j.LogManager;

import java.util.function.Function;

/**
 * @param <L> Language set (type of possible connections)
 * @param <S> State argument (type of the argument the fsm receives)
 * @see SMBridgeNode
 * <p>
 * Bridge node implementation
 */
public final class SMBridgeNodeImpl<L, S> extends SMBridgeNode<L, S> {
    private Function<S, L> mParser;

    /**
     *
     * @param name name assigned to this node
     * @param parser function used as this node's {@link org.gbutil.fsm.node.connected.ISMConnectedNode#parse(Object) parser}
     */
    public SMBridgeNodeImpl(String name, Function<S, L> parser) {
        super(name);
        mParser = parser;
    }

    @Override
    public L parse(S state) {
        LogManager.getLogger("gbutil.fsm").trace("paring: " + state + " -> " + mParser.apply(state));
        return mParser.apply(state);
    }
}
