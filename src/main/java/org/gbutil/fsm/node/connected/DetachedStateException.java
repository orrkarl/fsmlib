package org.gbutil.fsm.node.connected;

/**
 * Indicates that a node has an unattached option, i.e under certain input
 * it will have no next node to turn to (which is illegal for {@link ISMConnectedNode connected nodes})
 *
 * @see ISMConnectedNode
 */
public class DetachedStateException extends IllegalStateException {
    /**
     * Name of the node who threw this exception
     */
    public final String nodeName;

    /**
     * The unattached option
     */
    public final String option;

    /**
     *
     * @param nodeName of the node who threw this exception
     * @param option The unattached option
     */
    public DetachedStateException(String nodeName, String option) {
        this.nodeName = nodeName;
        this.option = option;
    }

    public DetachedStateException(String s, String nodeName, String option) {
        super(s);
        this.nodeName = nodeName;
        this.option = option;
    }

    public DetachedStateException(String message, Throwable cause, String nodeName, String option) {
        super(message, cause);
        this.nodeName = nodeName;
        this.option = option;
    }

    public DetachedStateException(Throwable cause, String nodeName, String option) {
        super(cause);
        this.nodeName = nodeName;
        this.option = option;
    }
}
