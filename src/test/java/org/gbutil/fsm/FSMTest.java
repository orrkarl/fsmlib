package org.gbutil.fsm;

import org.gbutil.Tuple.Tuple3;
import org.gbutil.fsm.node.NodeFactory;
import org.gbutil.fsm.node.connected.ISMConnectedNode;
import org.gbutil.fsm.node.end.SMEndNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class FSMTest {

    private static Boolean[] values = {Boolean.FALSE, Boolean.TRUE};

    static class ComplexState {
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

    static class IntegerState {
        public int value;

        public IntegerState(int value) {
            this.value = value;
        }

        public int getValue() {
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
    @DisplayName("callback")
    void callback() {
        List<Integer> res = new LinkedList<>();
        Function<IntegerState, Boolean> parser = n -> n.getValue() < 10;
        Consumer<IntegerState> callback = n -> res.add(n.getValue());
        ISMConnectedNode<Boolean, IntegerState> initial =
                NodeFactory.mkConnectedNode("initial", parser, callback);
        ISMConnectedNode<Boolean, IntegerState> node1 =
                NodeFactory.mkConnectedNode("node1", parser, callback);
        SMEndNode<IntegerState> TRUE =
                NodeFactory.mkEndNode("TRUE", true, callback);
        SMEndNode<IntegerState> FALSE =
                NodeFactory.mkEndNode("FALSE", false, callback);

        initial.setConnectedNode(true, node1);
        node1.setConnectedNode(true, TRUE);
        FiniteStateMachine<Boolean, IntegerState> fsm = new FiniteStateMachine<>("test", initial);
        fsm.attachOpenedTo(FALSE, values);

        List<Integer> success = new LinkedList<>();
        List<Integer> fail = new LinkedList<>();
        success.add(5);
        success.add(5);
        success.add(5);
        fail.add(15);
        fail.add(15);

        fsm.apply(new IntegerState(5));
        assertTrue(res.equals(success));

        res.clear();
        fsm.apply(new IntegerState(15));
        assertTrue(res.equals(fail));
    }

    @Test
    @DisplayName("Randomized")
    void random() {
        for (int i = 2; i < 50; i++) {
            Tuple3<FiniteStateMachine<Boolean, ComplexState>, ComplexState, ComplexState> tested =
                    generateComplexTest(10);

            assertTrue(tested.first.apply(tested.second));
            assertFalse(tested.first.apply(tested.third));
            assertFalse(tested.first.checkForCycles());
        }
    }

    @Test
    @DisplayName("detached")
    void detached() {
        ISMConnectedNode<Boolean, ComplexState> node = NodeFactory.mkConnectedNode("node", ComplexState.getGetter(0));
        FiniteStateMachine<Boolean, ComplexState> fsm = new FiniteStateMachine<>("failing", node);

        assertFalse(fsm.apply(new ComplexState("fail1", false)));
    }


    private static Tuple3<FiniteStateMachine<Boolean, ComplexState>, ComplexState, ComplexState> generateComplexTest(int size) {
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

    static List<ISMConnectedNode<Boolean, ComplexState>> generateRandomMachine(int size) {
        boolean[] trueState = generateRandom(size);
        List<ISMConnectedNode<Boolean, ComplexState>> nodes = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            nodes.add(NodeFactory.mkConnectedNode("node" + i, ComplexState.getGetter(i)));
        }
        for (int i = 0; i < size - 1; i++) {
            nodes.get(i).setConnectedNode(trueState[i], nodes.get(i + 1));
        }
        nodes.get(size - 1).setConnectedNode(trueState[size - 1], NodeFactory.mkEndNode(true));
        return nodes;
    }

    private static boolean[] generateRandom(int size) {
        boolean[] ret = new boolean[size];
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            ret[i] = rand.nextBoolean();
        }
        return ret;
    }
}