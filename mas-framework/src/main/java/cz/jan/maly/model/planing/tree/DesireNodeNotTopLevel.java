package cz.jan.maly.model.planing.tree;

import cz.jan.maly.model.knowledge.DataForDecision;
import cz.jan.maly.model.metadata.DecisionContainerParameters;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.planing.DesireForOthers;
import cz.jan.maly.model.planing.Intention;
import cz.jan.maly.model.planing.InternalDesire;
import cz.jan.maly.model.planing.OwnDesire;

import java.util.Optional;

/**
 * Template for desire not in top level
 * Created by Jan on 28-Feb-17.
 */
public abstract class DesireNodeNotTopLevel<T extends InternalDesire<? extends Intention>, K extends Node & IntentionNodeWithChildes & Parent> extends Node.NotTopLevel<K> implements DesireNodeInterface<IntentionNodeNotTopLevel<?, ?, ?>> {
    final T desire;

    private DesireNodeNotTopLevel(K parent, T desire) {
        super(parent, desire.getDesireParameters());
        this.desire = desire;
    }

    abstract IntentionNodeNotTopLevel<?, ?, ?> formIntentionNode();

    public Optional<IntentionNodeNotTopLevel<?, ?, ?>> makeCommitment(DataForDecision dataForDecision) {
        if (desire.shouldCommit(dataForDecision)) {
            IntentionNodeNotTopLevel<?, ?, ?> node = formIntentionNode();
            parent.replaceDesireByIntention(this, node);
            return Optional.of(node);
        }
        return Optional.empty();
    }

    @Override
    public DesireKey getAssociatedDesireKey() {
        return getDesireKey();
    }

    @Override
    public DecisionContainerParameters getParametersToLoad() {
        return desire.getParametersToLoad();
    }

    /**
     * Implementation of top node with desire for other agents
     */
    static abstract class ForOthers<K extends Node & IntentionNodeWithChildes & Parent> extends DesireNodeNotTopLevel<DesireForOthers, K> {
        private ForOthers(K parent, DesireForOthers desire) {
            super(parent, desire);
        }

        /**
         * Parent is in top level
         */
        static class TopLevelParent extends ForOthers<IntentionNodeAtTopLevel.WithAbstractPlan> {
            TopLevelParent(IntentionNodeAtTopLevel.WithAbstractPlan parent, DesireForOthers desire) {
                super(parent, desire);
            }

            @Override
            IntentionNodeNotTopLevel<?, ?, ?> formIntentionNode() {
                return new IntentionNodeNotTopLevel.WithDesireForOthers.TopLevelParent(parent, desire);
            }
        }

        /**
         * Parent is not in top level
         */
        static class NotTopLevelParent extends ForOthers<IntentionNodeNotTopLevel.WithAbstractPlan> {
            NotTopLevelParent(IntentionNodeNotTopLevel.WithAbstractPlan parent, DesireForOthers desire) {
                super(parent, desire);
            }

            @Override
            IntentionNodeNotTopLevel<?, ?, ?> formIntentionNode() {
                return new IntentionNodeNotTopLevel.WithDesireForOthers.NotTopLevelParent(parent, desire);
            }
        }

    }

    /**
     * Implementation of top node with desire for other agents
     */
    static abstract class WithAbstractPlan<T extends InternalDesire<? extends Intention>, K extends Node & IntentionNodeWithChildes & Parent> extends DesireNodeNotTopLevel<T, K> {
        private WithAbstractPlan(K parent, T desire) {
            super(parent, desire);
        }

        /**
         * Parent is in top level
         */
        static class TopLevelParent extends WithAbstractPlan<OwnDesire.WithAbstractIntention, IntentionNodeAtTopLevel.WithAbstractPlan> {
            TopLevelParent(IntentionNodeAtTopLevel.WithAbstractPlan parent, OwnDesire.WithAbstractIntention desire) {
                super(parent, desire);
            }

            @Override
            IntentionNodeNotTopLevel<?, ?, ?> formIntentionNode() {
                return new IntentionNodeNotTopLevel.WithAbstractPlan.TopLevelParent(parent, desire);
            }
        }

        /**
         * Parent is in top level
         */
        static class NotTopLevelParent extends WithAbstractPlan<OwnDesire.WithAbstractIntention, IntentionNodeNotTopLevel.WithAbstractPlan> {
            NotTopLevelParent(IntentionNodeNotTopLevel.WithAbstractPlan parent, OwnDesire.WithAbstractIntention desire) {
                super(parent, desire);
            }

            @Override
            IntentionNodeNotTopLevel<?, ?, ?> formIntentionNode() {
                return new IntentionNodeNotTopLevel.WithAbstractPlan.NotTopLevelParent(parent, desire);
            }
        }

    }

    /**
     * Class to extend template - to define desire node without child
     */
    abstract static class WithPlan<K extends Node & IntentionNodeWithChildes & Parent> extends WithAbstractPlan<OwnDesire.WithIntentionWithPlan, K> {
        private WithPlan(K parent, OwnDesire.WithIntentionWithPlan desire) {
            super(parent, desire);
        }

        /**
         * Concrete implementation, intention's desire is formed anew
         */
        static class AtTopLevelParent extends WithPlan<IntentionNodeAtTopLevel.WithAbstractPlan> {
            AtTopLevelParent(IntentionNodeAtTopLevel.WithAbstractPlan parent, OwnDesire.WithIntentionWithPlan desire) {
                super(parent, desire);
            }

            @Override
            IntentionNodeNotTopLevel<?, ?, ?> formIntentionNode() {
                return new IntentionNodeNotTopLevel.WithPlan.AtTopLevelParent(parent, desire);
            }
        }

        /**
         * Concrete implementation, intention's desire is formed anew
         */
        static class NotTopLevelParent extends WithPlan<IntentionNodeNotTopLevel.WithAbstractPlan> {
            NotTopLevelParent(IntentionNodeNotTopLevel.WithAbstractPlan parent, OwnDesire.WithIntentionWithPlan desire) {
                super(parent, desire);
            }

            @Override
            IntentionNodeNotTopLevel<?, ?, ?> formIntentionNode() {
                return new IntentionNodeNotTopLevel.WithPlan.NotTopLevelParent(parent, desire);
            }
        }

    }

}