package org.gbutil.fsm.node.connected;

public class DetachedStateException extends IllegalStateException {
    public final String nodeName;
    public final String option;

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
