package org.gbutil.fsm.node.end;

import org.apache.logging.log4j.LogManager;
import org.gbutil.fsm.IState;

public class SMEndInactiveNode<S extends IState> extends SMEndNode<S> {

    public SMEndInactiveNode(String name, boolean value) {
        super(name, value);
    }

    public SMEndInactiveNode(boolean value) { this(Boolean.toString(value).toUpperCase(), value); }

    @Override
    public boolean execute(S state) {
        LogManager.getRootLogger().trace("currently at node " + getNodeName());
        return asBoolean();
    }
}
