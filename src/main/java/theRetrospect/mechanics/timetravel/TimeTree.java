package theRetrospect.mechanics.timetravel;

import com.badlogic.gdx.utils.Disposable;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.ArrayList;
import java.util.List;

public class TimeTree implements Disposable {

    /**
     * Roots of the tree, storing nodes of the first round in each timeline.
     * The round number may not be exactly 1 if the player rewinds past the first round.
     */
    public final List<Node> roots = new ArrayList<>();
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

    public int getActiveRound() {
        return activeNode.round;
    }

    public Node addNode(int round, CombatState state) {
        Node node = new Node(round, state, activeNode);
        if (activeNode != null) {
            activeNode.children.add(node);
        } else {
            roots.add(node);
        }
        activeNode = node;
        return node;
    }

    public Node addRoot(int round, CombatState state) {
        Node node = new Node(round, state, null);
        roots.add(node);
        return node;
    }

    /**
     * Creates a linear path from the root to the provided target node.
     * The path can be used to traverse the state tree in a 1-dimensional manner.
     *
     * @param targetNode  Target node of the path.
     * @param targetState State of the target node. This state must be contained in the node.
     * @param originNode  Origin node of the path.
     * @return A Linear Path with the target at the provided node.
     */
    public LinearPath createLinearPath(Node targetNode, CombatState targetState, Node originNode) {
        if (targetState != targetNode.baseState && !targetNode.midStates.contains(targetState)) {
            throw new IllegalArgumentException("Provided state is not contained in the provided node.");
        }
        return new LinearPath(this, targetNode, targetState, originNode);
    }

    /**
     * Creates a linear path from the root to the provided target node.
     * The path can be used to traverse the state tree in a 1-dimensional manner.
     *
     * @param targetNode   Target node of the path.
     * @param targetState  State of the target node. This state can be a base state or a mid-state, but it must be contained in the node.
     * @param originOffset Number of rounds to go back from the target node.
     * @return A Linear Path with the target at the provided node.
     */
    public LinearPath createLinearPath(Node targetNode, CombatState targetState, int originOffset) {
        Node originNode = getNodeForRound(targetNode, targetNode.round - originOffset);
        if (originNode == null) {
            originNode = getRoot(targetNode);
        }
        return createLinearPath(targetNode, targetState, originNode);
    }

    /**
     * Get the node at the specified round in the path leading up to the target node.
     * The provided round number must be smaller than the round number of the target node.
     *
     * @param targetNode Node at which the path ends.
     * @param round      Round to get the node for.
     * @return Node at the specified round.
     */
    public Node getNodeForRound(Node targetNode, int round) {
        if (round > targetNode.round) {
            return null;
        }

        if (round == targetNode.round) {
            return targetNode;
        }

        Node node = targetNode;
        while (node.round > round) {
            if (node.parent == null) {
                return null;
            }
            node = node.parent;
        }

        return node;
    }

    /**
     * Get the corresponding root node of the provided target node.
     *
     * @param targetNode Target node.
     * @return Root node of the provided target node.
     */
    public Node getRoot(Node targetNode) {
        Node node = targetNode;
        while (node.parent != null) {
            node = node.parent;
        }
        return node;
    }

    @Override
    public void dispose() {
        for (Node node : roots) {
            node.dispose();
        }
        roots.clear();
        activeNode = null;
    }

    public static class Node implements Disposable {
        public final int round;
        /**
         * The state of this round, captured at the start of the round.
         */
        public CombatState baseState;
        /**
         * The states of this round, captured in the middle of the round just before time traveling.
         */
        public final List<CombatState> midStates = new ArrayList<>();
        /**
         * The cards played manually in this round, captured at the end of the round,
         * or in the middle of the round just before time traveling.
         */
        public final List<AbstractCard> cardsPlayedManually = new ArrayList<>();
        public final Node parent;
        public final List<Node> children = new ArrayList<>();

        private Node(final int round, CombatState baseState, final Node parent) {
            this.round = round;
            this.baseState = baseState;
            this.parent = parent;
        }

        public boolean isRecursiveParentOf(Node node) {
            Node parent = node.parent;
            while (parent != null) {
                if (parent == this) {
                    return true;
                }
                parent = parent.parent;
            }
            return false;
        }

        @Override
        public void dispose() {
            baseState.dispose();
            for (CombatState state : midStates) {
                state.dispose();
            }
            midStates.clear();
            cardsPlayedManually.clear();
            for (Node child : children) {
                child.dispose();
            }
            children.clear();
        }
    }

    public static class LinearPath {
        public final TimeTree tree;
        /**
         * The node from which the path originates. The path is invalid for nodes before this node.
         */
        public final Node originNode;
        /**
         * The node at which the path ends. The path is invalid for nodes after this node.
         */
        public final Node targetNode;
        /**
         * The state at which the path ends. This is usually a mid-state of the target node.
         */
        public final CombatState targetState;

        private Node cachedNode = null;

        private LinearPath(final TimeTree tree, final Node targetNode, final CombatState targetState, final Node originNode) {
            this.tree = tree;
            this.targetNode = targetNode;
            this.targetState = targetState;
            this.originNode = originNode;
        }

        /**
         * Get the node at the specified round in this path.
         * The provided round number must be smaller than the round number of the target node.
         *
         * @param round Round to get the node for.
         * @return Node at the specified round.
         */
        public Node getNodeForRound(final int round) {
            if (cachedNode != null && cachedNode.round == round) {
                return cachedNode;
            }

            cachedNode = tree.getNodeForRound(targetNode, round);
            return cachedNode;
        }
    }
}
