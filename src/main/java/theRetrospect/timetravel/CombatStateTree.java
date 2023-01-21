package theRetrospect.timetravel;

import java.util.ArrayList;
import java.util.List;

public class CombatStateTree {

    /**
     * Root of the tree, storing the node of the first round.
     */
    public Node root = null;
    /**
     * Current node of the tree, storing the node of the current round in the current timeline.
     */
    private Node activeNode = null;

    public Node getActiveNode() {
        return activeNode;
    }

    public void setActiveNode(Node node) {
        activeNode = node;
    }

    public int getCurrentRound() {
        return activeNode.round;
    }

    public void addNode(int round, CombatState state) {
        Node node = new Node(round, state, activeNode);
        if (root == null) {
            root = node;
        }
        if (activeNode != null) {
            activeNode.children.add(node);
        }
        activeNode = node;
    }

    /**
     * Creates a linear path from the root to the provided origin node.
     * The path can be used to traverse the state tree in a 1-dimensional manner.
     *
     * @param node  Origin node of the path.
     * @param state State of the origin node. This state must be contained in the node.
     * @return A Linear Path with the origin at the provided node.
     */
    public LinearPath createLinearPath(Node node, CombatState state) {
        if (state != node.baseState && !node.midStates.contains(state)) {
            throw new IllegalArgumentException("Provided state is not contained in the provided node.");
        }
        return new LinearPath(this, node, state);
    }

    public static class Node {
        public final int round;
        public final CombatState baseState;
        public final List<CombatState> midStates = new ArrayList<>();
        public final Node parent;
        public final List<Node> children = new ArrayList<>();

        private Node(final int round, final CombatState baseState, final Node parent) {
            this.round = round;
            this.baseState = baseState;
            this.parent = parent;
        }
    }

    public static class LinearPath {
        public final CombatStateTree tree;
        public final Node originNode;
        public final CombatState originState;

        private Node cachedNode = null;

        private LinearPath(final CombatStateTree tree, final Node node, final CombatState state) {
            this.tree = tree;
            this.originNode = node;
            this.originState = state;
        }

        /**
         * Get the node at the specified round in this path.
         * The provided round number must be smaller than the round number of the origin node.
         *
         * @param round Round to get the node for.
         * @return Node at the specified round.
         */
        public Node getNodeForRound(final int round) {
            if (cachedNode != null && cachedNode.round == round) {
                return cachedNode;
            }

            if (round > originNode.round) {
                return null;
            }

            if (round == originNode.round) {
                cachedNode = originNode;
                return originNode;
            }

            Node node = originNode;
            while (node.round > round) {
                if (node.parent == null) {
                    return null;
                }
                node = node.parent;
            }

            cachedNode = node;
            return node;
        }
    }
}
