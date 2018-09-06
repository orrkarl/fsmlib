package org.gbutil.graph;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


class TrajanAlgorithmTest {
    static class TestNode implements Comparable {
        String name;
        List<TestNode> next = new LinkedList<>();

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TestNode)) return false;

            TestNode testNode = (TestNode) o;

            return name != null ? name.equals(testNode.name) : testNode.name == null;
        }

        @Override
        public int hashCode() {
            return name != null ? name.hashCode() : 0;
        }

        @Override
        public int compareTo(Object o) {
            if (this == o) return 0;
            if (!(o instanceof TestNode)) return -1;

            TestNode testNode = (TestNode) o;
            return testNode.name.compareTo(name);
        }

        @Override
        public String toString() {
            return "TestNode{" + "name='" + name + '\'' +
                    '}';
        }

        TestNode(String name, TestNode... nodes) {
            this.name = name;
            next.addAll(Arrays.asList(nodes));
        }

        boolean isConnectedTo(TestNode other) {
            return next.contains(other);
        }
    }

    @Test
    @DisplayName("Complete 4-graph test. Sanity test #1")
    void completeGraph() {
        boolean[][] connectionMatrix = {
                {true, true, true, true},
                {true, true, true, true},
                {true, true, true, true},
                {true, true, true, true}
        };
        TestNode[] graph = {
                new TestNode("node1"),
                new TestNode("node2"),
                new TestNode("node3"),
                new TestNode("node4")
        };

        List<Set<TestNode>> res = TrajanAlgorithm.getStronglyConnectedComponents(graph, connectionMatrix);
        List<Set<TestNode>> expected = new LinkedList<>();
        expected.add(new TreeSet<>(Arrays.asList(graph)));
        assertEquals(expected, res);
    }

    @Test
    @DisplayName("Empty 4-graph test. Sanity test #2")
    void emptyGraph() {
        boolean[][] connectionMatrix = {
                {false, false, false, false},
                {false, false, false, false},
                {false, false, false, false},
                {false, false, false, false}
        };
        TestNode[] graph = {
                new TestNode("node1"),
                new TestNode("node2"),
                new TestNode("node3"),
                new TestNode("node4")
        };

        List<Set<TestNode>> res = TrajanAlgorithm.getStronglyConnectedComponents(graph, connectionMatrix);
        List<Set<TestNode>> expected = new LinkedList<>();
        assertEquals(expected, res);
    }

    @Test
    @DisplayName("really large graph")
    void longChain() {
        int length = 2000;
        TestNode current = new TestNode("node0");
        TestNode[] arr = new TestNode[length];
        for (int i = 1; i <= length; i++) {
            arr[i - 1] = current;
            current.next.add(new TestNode("node" + i));
            current = current.next.get(0);
        }
        arr[arr.length - 1].next.add(arr[0]);
        assertEquals(TrajanAlgorithm.getStronglyConnectedComponents(arr, buildConnectionMatrix(arr)).get(0).size(), length);
    }

    @Test
    @DisplayName("Standard case")
    void standard() {
        TestNode[] graph = new TestNode[8];
        graph[0] = new TestNode("node 0");
        graph[1] = new TestNode("node 1");
        graph[2] = new TestNode("node 2");
        graph[3] = new TestNode("node 3");
        graph[4] = new TestNode("node 4");
        graph[5] = new TestNode("node 5");
        graph[6] = new TestNode("node 6");
        graph[7] = new TestNode("node 7");

        // node 0
        graph[0].next.add(graph[1]);
        graph[0].next.add(graph[3]);
        // node 1
        graph[1].next.add(graph[2]);
        graph[1].next.add(graph[3]);
        // node 2
        graph[2].next.add(graph[5]);
        // node 3
        graph[3].next.add(graph[4]);
        // node 4
        graph[4].next.add(graph[1]);
        // node 5
        graph[5].next.add(graph[6]);
        // node 6
        graph[6].next.add(graph[7]);
        // node 7
        graph[7].next.add(graph[5]);

        Set<TestNode> component1 = new TreeSet<>();
        component1.add(graph[1]);
        component1.add(graph[3]);
        component1.add(graph[4]);

        Set<TestNode> component2 = new TreeSet<>();
        component2.add(graph[5]);
        component2.add(graph[6]);
        component2.add(graph[7]);

        List<Set<TestNode>> expected = new LinkedList<>();
        expected.add(component2);
        expected.add(component1);

        assertEquals(expected, TrajanAlgorithm.getStronglyConnectedComponents(graph, buildConnectionMatrix(graph)));
    }

    private boolean[][] buildConnectionMatrix(TestNode[] nodes) {
        boolean[][] ret = new boolean[nodes.length][nodes.length];
        for (int i = 0; i < nodes.length; i++)
            for (int j = 0; j < nodes.length; j++)
                ret[i][j] = nodes[i].isConnectedTo(nodes[j]);
        return ret;
    }
}