package aic.gas.sc.gg_bot.abstract_bot.model.decision;

import static aic.gas.sc.gg_bot.abstract_bot.model.decision.NextActionEnumerations.NO;
import static aic.gas.sc.gg_bot.abstract_bot.model.decision.NextActionEnumerations.YES;

import aic.gas.sc.gg_bot.abstract_bot.model.features.FeatureNormalizer;
import aic.gas.sc.gg_bot.abstract_bot.utils.Configuration;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import jsat.linear.DenseVector;
import jsat.linear.Vec;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * DecisionPoint decide next action based on current state. It is initialized from
 * DecisionPointDataStructure
 */
@Getter
@Slf4j
public class DecisionPoint {

  private final List<StateWithTransition> states;
  private final List<FeatureNormalizer> normalizers;
  private final Map<Integer, DecisionInState> cache = new HashMap<>();

  public DecisionPoint(DecisionPointDataStructure dataStructure) {
    this.states = dataStructure.states.stream()
        .map(StateWithTransition::new)
        .collect(Collectors.toList());
    this.normalizers = dataStructure.normalizers;
  }

  /**
   * For given state (represented by feature vector) return optimal action based on policy
   */
  public boolean nextAction(double[] featureVector, int frame, int agentId,
      int forHowLongToCacheDecision) {
    DecisionInState decisionInState = cache.get(agentId);
    if (decisionInState == null || decisionInState
        .canChangeDecision(featureVector, frame, forHowLongToCacheDecision)) {
      decisionInState = new DecisionInState(featureVector, frame, getState(featureVector).commit());
      cache.put(agentId, decisionInState);
    }
    return decisionInState.isCommit();
  }

  private StateWithTransition getState(double[] featureVector) {
    Vec anotherInstance = new DenseVector((Configuration
        .normalizeFeatureVector(featureVector, normalizers)));
    return states.stream().min(Comparator.comparingDouble(o -> o.distance(anotherInstance))).get();
  }

  @AllArgsConstructor
  private static class DecisionInState {

    private final double[] featureVector;
    private final int madeInFrame;
    @Getter
    private final boolean commit;

    public boolean canChangeDecision(double[] currentFeatureVector, int currentFrame,
        int forHowLongToCacheDecision) {
      return currentFrame - madeInFrame >= forHowLongToCacheDecision && !Arrays
          .equals(featureVector, currentFeatureVector);
    }

  }

  /**
   * StateWithTransition to compute distance between instances and return next action (commitment)
   * based on policy
   */
  @Getter
  @EqualsAndHashCode(of = "center")
  public static class StateWithTransition {

    private final double probOfYes;
    private final double probOfNo;
    private final Vec center;
    private transient static final Random RANDOM = new Random();

    private StateWithTransition(
        DecisionPointDataStructure.StateWithTransition stateWithTransition) {
      this.center = stateWithTransition.getFeatureVector();
      this.probOfYes = stateWithTransition.getNextActions().getOrDefault(YES, 0.0);
      this.probOfNo = stateWithTransition.getNextActions().getOrDefault(NO, 0.0);
    }

    boolean commit() {
      if (RANDOM.nextBoolean()) {
        if (RANDOM.nextDouble() <= probOfYes) {
          return YES.commit();
        }
        return NO.commit();
      } else {
        if (RANDOM.nextDouble() <= probOfNo) {
          return NO.commit();
        }
        return YES.commit();
      }
    }

    /**
     * Returns distance between center and passed instance
     */
    double distance(Vec anotherPoint) {
      return Configuration.DISTANCE_FUNCTION.dist(center, anotherPoint);
    }

  }

}
