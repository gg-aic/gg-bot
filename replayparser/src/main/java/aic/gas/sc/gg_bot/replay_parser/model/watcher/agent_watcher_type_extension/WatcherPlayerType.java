package aic.gas.sc.gg_bot.replay_parser.model.watcher.agent_watcher_type_extension;

import aic.gas.sc.gg_bot.abstract_bot.model.bot.AgentTypes;
import aic.gas.sc.gg_bot.mas.model.metadata.FactKey;
import aic.gas.sc.gg_bot.replay_parser.model.watcher.AgentWatcherType;
import aic.gas.sc.gg_bot.replay_parser.model.watcher.updating_strategies.IPlayerEnvironmentObservation;
import aic.gas.sc.gg_bot.replay_parser.model.watcher.updating_strategies.IReasoning;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;

/**
 * Extension of AgentWatcherType to WatcherPlayerType
 */
@Getter
public class WatcherPlayerType extends AgentWatcherType {

  private final IPlayerEnvironmentObservation playerEnvironmentObservation;

  @Builder
  private WatcherPlayerType(
      AgentTypes agentType,
      Set<FactKey<?>> factSetsKeys,
      List<IPlanWatcherInitializationStrategy> planWatchers, IReasoning reasoning,
      IPlayerEnvironmentObservation playerEnvironmentObservation) {
    super(agentType, factSetsKeys, planWatchers, reasoning);
    this.playerEnvironmentObservation = playerEnvironmentObservation;
  }

  /**
   * Builder with default values
   */
  public static class WatcherPlayerTypeBuilder extends AgentWatcherTypeBuilder {
    private Set<FactKey<?>> factSetsKeys = new HashSet<>();
  }
}
