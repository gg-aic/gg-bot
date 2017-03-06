package cz.jan.maly.model.planing.tree;

import cz.jan.maly.model.knowledge.DataForDecision;
import cz.jan.maly.model.metadata.DecisionContainerParameters;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.planing.*;

import java.util.Optional;

/**
 * Template for desire in top level
 * Created by Jan on 28-Feb-17.
 */
public abstract class DesireNodeAtTopLevel<T extends InternalDesire<? extends Intention>> extends Node.TopLevel implements DesireNodeInterface<IntentionNodeAtTopLevel<?, ?>> {
    final T desire;

    private DesireNodeAtTopLevel(Tree tree, T desire) {
        super(tree, desire.getDesireParameters());
        this.desire = desire;
    }

    @Override
    public DesireKey getAssociatedDesireKey() {
        return getDesireKey();
    }

    abstract IntentionNodeAtTopLevel<?, ?> formIntentionNode();

    public Optional<IntentionNodeAtTopLevel<?, ?>> makeCommitment(DataForDecision dataForDecision) {
        if (desire.shouldCommit(dataForDecision)) {
            IntentionNodeAtTopLevel<?, ?> node = formIntentionNode();
            parent.replaceDesireByIntention(this, node);
            return Optional.of(node);
        }
        return Optional.empty();
    }

    @Override
    public DecisionContainerParameters getParametersToLoad() {
        return desire.getParametersToLoad();
    }

    /**
     * Implementation of top node with desire for other agents
     */
    static class ForOthers extends DesireNodeAtTopLevel<DesireForOthers> {
        ForOthers(Tree tree, DesireForOthers desire) {
            super(tree, desire);
        }

        @Override
        protected IntentionNodeAtTopLevel<?, ?> formIntentionNode() {
            return new IntentionNodeAtTopLevel.WithDesireForOthers(parent, desire);
        }
    }

    /**
     * Template for nodes with desires from another agent
     *
     * @param <V>
     */
    public abstract static class FromAnotherAgent<V extends DesireFromAnotherAgent<? extends Intention>> extends DesireNodeAtTopLevel<V> {
        FromAnotherAgent(Tree tree, V desire) {
            super(tree, desire);
        }

        /**
         * Concrete implementation, desire forms intention with abstract plan
         */
        static class WithAbstractIntention extends FromAnotherAgent<DesireFromAnotherAgent.WithAbstractIntention> {
            WithAbstractIntention(Tree tree, DesireFromAnotherAgent.WithAbstractIntention desire) {
                super(tree, desire);
            }

            @Override
            IntentionNodeAtTopLevel<?, ?> formIntentionNode() {
                return new IntentionNodeAtTopLevel.WithAbstractPlan.FromAnotherAgent(parent, desire);
            }
        }

        /**
         * Concrete implementation, desire forms intention with concrete plan (command)
         */
        static class WithIntentionWithPlan extends FromAnotherAgent<DesireFromAnotherAgent.WithIntentionWithPlan> {
            WithIntentionWithPlan(Tree tree, DesireFromAnotherAgent.WithIntentionWithPlan desire) {
                super(tree, desire);
            }

            @Override
            IntentionNodeAtTopLevel<?, ?> formIntentionNode() {
                return new IntentionNodeAtTopLevel.WithPlan.FromAnotherAgent(parent, desire);
            }
        }

    }

    /**
     * Template for nodes with own desires
     *
     * @param <V>
     */
    abstract static class Own<V extends OwnDesire<? extends Intention>> extends DesireNodeAtTopLevel<V> {
        Own(Tree tree, V desire) {
            super(tree, desire);
        }

        /**
         * Concrete implementation, desire forms intention with abstract plan
         */
        static class WithAbstractIntention extends Own<OwnDesire.WithAbstractIntention> {
            WithAbstractIntention(Tree tree, OwnDesire.WithAbstractIntention desire) {
                super(tree, desire);
            }

            @Override
            IntentionNodeAtTopLevel<?, ?> formIntentionNode() {
                return new IntentionNodeAtTopLevel.WithAbstractPlan.Own(parent, desire);
            }
        }

        /**
         * Concrete implementation, desire forms intention with concrete plan (command)
         */
        static class WithIntentionWithPlan extends Own<OwnDesire.WithIntentionWithPlan> {
            WithIntentionWithPlan(Tree tree, OwnDesire.WithIntentionWithPlan desire) {
                super(tree, desire);
            }

            @Override
            IntentionNodeAtTopLevel<?, ?> formIntentionNode() {
                return new IntentionNodeAtTopLevel.WithPlan.Own(parent, desire);
            }
        }

    }

}