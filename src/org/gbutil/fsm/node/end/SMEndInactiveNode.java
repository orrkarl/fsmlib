package org.gbutil.fsm.node.end;

import org.apache.logging.log4j.LogManager;

/**
 * The most simple end node possible. Returns a given boolean value, without executing anything else
 *
 * @param <S> State argument (type of the argument the fsm receives)
 * @see SMEndNode
 */
public final class SMEndInactiveNode<S> extends SMEndNode<S> {

    /**
     *
     * @param name name assigned to this node
     * @param value the value of this node
     */
    public SMEndInactiveNode(String name, boolean value) {
        super(name, value);
    }

    /**
     * Same as {@link SMEndInactiveNode#SMEndInactiveNode(String, boolean)}, only using {@code value} as the name
     * @param value name and value assigned to this node
     */
    public SMEndInactiveNode(boolean value) { this(Boolean.toString(value).toUpperCase(), value); }

    @Override
    public boolean execute(S state) {
        LogManager.getLogger("gbutil.fsm").trace("currently at node " + getNodeName());
        return asBoolean();
    }
}
