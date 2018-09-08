package org.gbutil.graph;

import org.gbutil.Tuple.Tuple3;
import org.gbutil.fsm.FiniteStateMachine;
import org.gbutil.fsm.IState;
import org.gbutil.fsm.node.NodeFactory;
import org.gbutil.fsm.node.connected.ISMConnectedNode;
import org.gbutil.fsm.node.end.SMEndNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class FSMTest {

    static Boolean[] values = {Boolean.FALSE, Boolean.TRUE};

    static class ComplexState implements IState {
        boolean[] values;
        String name;

        public ComplexState(String name, boolean... values) {
            this.values = values;
            this.name = name;
        }

        public static Function<ComplexState, Boolean> getGetter(int index) {
            return (state) -> state.values[index];
        }

        @Override
        public String toString() {
            return "ComplexState{" + "name='" + name + '\'' +
                    '}';
        }
    }

    static class IntegerState implements IState {
        public int value;

        public IntegerState(int value) {
            this.value = value;
        }

        public int getValue() {
            value = (value + 1) % 2;
            return value;
        }
    }

    @Test
    @DisplayName("simple chain - sanity test")
    void simpleChain() {
        SMEndNode<IntegerState> FALSE = NodeFactory.mkEndNode(false);
        SMEndNode<IntegerState> TRUE = NodeFactory.mkEndNode(true);

        List<ISMConnectedNode<Boolean, IntegerState>> arr = new LinkedList<>();
        arr.add(NodeFactory.mkConnectedNode("initial", s -> s.value > 5));
        arr.add(NodeFactory.mkConnectedNode("n1", s -> s.value > 5));
        arr.add(NodeFactory.mkConnectedNode("n2", s -> s.value > 5));
        arr.add(NodeFactory.mkConnectedNode("n3", s -> s.value > 5));
        arr.add(NodeFactory.mkConnectedNode("n4", s -> s.value > 5));
        arr.add(NodeFactory.mkConnectedNode("n5", s -> s.value > 5));
        arr.add(NodeFactory.mkConnectedNode("n6", s -> s.value > 5));
        arr.add(NodeFactory.mkConnectedNode("n7", s -> s.value > 5));

        for (int i = 1; i < arr.size(); i++) {
            arr.get(i - 1).setConnectedNode(Boolean.TRUE, arr.get(i));
        }

        FiniteStateMachine<Boolean, IntegerState> fsm = new FiniteStateMachine<>("testing", arr.get(0));
        fsm.attachOpenedTo(FALSE, values);
        arr.get(arr.size() - 1).setConnectedNode(Boolean.TRUE, TRUE);

        assertFalse(fsm.apply(new IntegerState(4)));
        assertTrue(fsm.apply(new IntegerState(8)));
    }

    @Test
    @DisplayName("Randomized")
    void random() {
        for (int i = 0; i < 50; i++) {
            Tuple3<FiniteStateMachine<Boolean, ComplexState>, ComplexState, ComplexState> tested =
                    generateComplexTest();

            assertTrue(tested.first.apply(tested.second));
            assertFalse(tested.first.apply(tested.third));
            assertFalse(tested.first.checkForCycles());
        }
    }


    static Tuple3<FiniteStateMachine<Boolean, ComplexState>, ComplexState, ComplexState> generateComplexTest() {
        int size = 10;
        boolean[] trueState = generateRandom(size);
        boolean[] falseState = generateRandom(size);
        if (Arrays.equals(trueState, falseState)) falseState[0] = !falseState[0];

        List<ISMConnectedNode<Boolean, ComplexState>> nodes = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            nodes.add(NodeFactory.mkConnectedNode("node" + i, ComplexState.getGetter(i)));
        }
        for (int i = 0; i < size - 1; i++) {
            nodes.get(i).setConnectedNode(trueState[i], nodes.get(i + 1));
        }
        nodes.get(size - 1).setConnectedNode(trueState[size - 1], NodeFactory.mkEndNode(true));
        FiniteStateMachine<Boolean, ComplexState> fsm = new FiniteStateMachine<>("test", nodes.get(0));
        fsm.attachOpenedTo(values);

        return new Tuple3<>(fsm, new ComplexState("true", trueState), new ComplexState("false", falseState));
    }

    static boolean[] generateRandom(int size) {
        boolean[] ret = new boolean[size];
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            ret[i] = rand.nextBoolean();
        }
        return ret;
    }
}