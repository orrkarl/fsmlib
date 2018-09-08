package org.gbutil.fsm.node.connected.active;

import java.util.function.Function;

/**
 * An active node whose parser is received as an arguments in the ctor
 *
 * @param <L> Language set (type of possible connections)
 * @param <S> State argument (type of the argument the fsm receives)
 */
public abstract class SMActiveNode<L, S> extends SMAbstractActiveNode<L, S> {
    private Function<S, L> mParser;

    public SMActiveNode(String name, Function<S, L> parser) {
        super(name);
        this.mParser = parser;
    }

    @Override
    public L parse(S state) {
        return mParser.apply(state);
    }

}
