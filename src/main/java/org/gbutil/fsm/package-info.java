/**
 * Finite State machine lib (fsmlib) classes.
 * Use {@link org.gbutil.fsm.FiniteStateMachine} to create fsm's, which contain {@link org.gbutil.fsm.node.ISMNode}
 * which call each other (as the name 'finite state machine' suggests).
 * <p>
 * <p>
 * In future versions, and fsm builder may be added. until than, you build your own fsm's using the nodes themselves.
 * Some build assisting tools (such as cycle detection) are given in {@link org.gbutil.fsm.FiniteStateMachine}
 * </p><p>
 * For most purposes fsmlib will be used, an implementation of needed nodes is already contained in it.
 * If you want a node which connects to other node, inherit {@link org.gbutil.fsm.node.connected.ISMConnectedNode}.
 * If you want a node which has no further connections, inherit {@link org.gbutil.fsm.node.end.SMEndNode}
 * notice that for most cases, some inheriting classes which are supplied in this package will be enough.
 * </p>
 *
 * @author karlo
 * @version 1.0
 * @see <a href="https://en.wikipedia.org/wiki/Finite-state_machine">finite state machine</a>
 * @since 2018-2019
 */
package org.gbutil.fsm;