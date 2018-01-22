package aic.gas.sc.gg_bot.bot.model.agent.types.implementation.location;

import static aic.gas.sc.gg_bot.abstract_bot.model.bot.AgentTypes.HATCHERY;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.AgentTypes.LAIR;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.AgentTypes.PLAYER;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactConverters.BASE_IS_COMPLETED;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactConverters.COUNT_OF_CREEP_COLONIES_AT_BASE;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactConverters.COUNT_OF_EXTRACTORS_ON_BASE;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactConverters.COUNT_OF_MINERALS_ON_BASE;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactConverters.COUNT_OF_SPORE_COLONIES_AT_BASE;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactConverters.COUNT_OF_SUNKEN_COLONIES_AT_BASE;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactKeys.BASE_TO_MOVE;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactKeys.CREEP_COLONY_COUNT;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactKeys.ENEMY_AIR;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactKeys.ENEMY_AIR_FORCE_STATUS;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactKeys.ENEMY_BUILDING;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactKeys.ENEMY_BUILDING_STATUS;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactKeys.ENEMY_GROUND;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactKeys.ENEMY_GROUND_FORCE_STATUS;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactKeys.ENEMY_STATIC_AIR_FORCE_STATUS;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactKeys.ENEMY_STATIC_GROUND_FORCE_STATUS;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactKeys.ENEMY_UNIT;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactKeys.HAS_BASE;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactKeys.HAS_EXTRACTOR;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactKeys.IS_BASE;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactKeys.IS_BASE_LOCATION;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactKeys.IS_ENEMY_BASE;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactKeys.IS_GATHERING_GAS;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactKeys.IS_GATHERING_MINERALS;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactKeys.LAST_TIME_SCOUTED;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactKeys.LOCATION;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactKeys.LOCKED_BUILDINGS;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactKeys.LOCKED_UNITS;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactKeys.MADE_OBSERVATION_IN_FRAME;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactKeys.OUR_UNIT;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactKeys.OWN_AIR;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactKeys.OWN_AIR_FORCE_STATUS;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactKeys.OWN_BUILDING;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactKeys.OWN_BUILDING_STATUS;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactKeys.OWN_GROUND;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactKeys.OWN_GROUND_FORCE_STATUS;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactKeys.OWN_STATIC_AIR_FORCE_STATUS;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactKeys.OWN_STATIC_GROUND_FORCE_STATUS;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactKeys.REPRESENTS_UNIT;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactKeys.SPORE_COLONY_COUNT;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactKeys.STATIC_DEFENSE;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactKeys.SUNKEN_COLONY_COUNT;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactKeys.TIME_OF_HOLD_COMMAND;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactKeys.WORKER_MINING_GAS;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactKeys.WORKER_MINING_MINERALS;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactKeys.WORKER_ON_BASE;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FeatureContainerHeaders.DEFENSE;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FeatureContainerHeaders.HOLDING;
import static aic.gas.sc.gg_bot.bot.model.agent.types.implementation.AgentTypeUtils.createConfigurationWithSharedDesireToBuildFromTemplate;

import aic.gas.sc.gg_bot.abstract_bot.model.UnitTypeStatus;
import aic.gas.sc.gg_bot.abstract_bot.model.bot.AgentTypes;
import aic.gas.sc.gg_bot.abstract_bot.model.bot.DesireKeys;
import aic.gas.sc.gg_bot.abstract_bot.model.bot.FactConverters;
import aic.gas.sc.gg_bot.abstract_bot.model.bot.FactKeys;
import aic.gas.sc.gg_bot.abstract_bot.model.features.FeatureContainerHeader;
import aic.gas.sc.gg_bot.abstract_bot.model.game.wrappers.ABaseLocationWrapper;
import aic.gas.sc.gg_bot.abstract_bot.model.game.wrappers.APosition;
import aic.gas.sc.gg_bot.abstract_bot.model.game.wrappers.AUnit;
import aic.gas.sc.gg_bot.abstract_bot.model.game.wrappers.AUnit.Enemy;
import aic.gas.sc.gg_bot.abstract_bot.model.game.wrappers.AUnitOfPlayer;
import aic.gas.sc.gg_bot.abstract_bot.model.game.wrappers.AUnitTypeWrapper;
import aic.gas.sc.gg_bot.abstract_bot.model.game.wrappers.UnitWrapperFactory;
import aic.gas.sc.gg_bot.bot.model.Decider;
import aic.gas.sc.gg_bot.bot.model.DesiresKeys;
import aic.gas.sc.gg_bot.bot.model.agent.types.AgentTypeBaseLocation;
import aic.gas.sc.gg_bot.bot.service.implementation.BotFacade;
import aic.gas.sc.gg_bot.bot.service.implementation.BuildLockerService;
import aic.gas.sc.gg_bot.mas.model.knowledge.ReadOnlyMemory;
import aic.gas.sc.gg_bot.mas.model.knowledge.WorkingMemory;
import aic.gas.sc.gg_bot.mas.model.metadata.DesireKey;
import aic.gas.sc.gg_bot.mas.model.metadata.FactKey;
import aic.gas.sc.gg_bot.mas.model.metadata.agents.configuration.ConfigurationWithAbstractPlan;
import aic.gas.sc.gg_bot.mas.model.metadata.agents.configuration.ConfigurationWithCommand;
import aic.gas.sc.gg_bot.mas.model.metadata.agents.configuration.ConfigurationWithCommand.WithReasoningCommandDesiredBySelf;
import aic.gas.sc.gg_bot.mas.model.metadata.agents.configuration.ConfigurationWithSharedDesire;
import aic.gas.sc.gg_bot.mas.model.metadata.containers.FactWithOptionalValueSet;
import aic.gas.sc.gg_bot.mas.model.planing.CommitmentDeciderInitializer;
import aic.gas.sc.gg_bot.mas.model.planing.command.ReasoningCommand;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//TODO refactor everything except buildings
public class BaseLocationAgentType {

  private interface SimpleDecisionStrategy {

    boolean isTrue(double value);
  }

  /**
   * Template to create desire initiated by learnt decision.
   * Initialize abstract build plan top - make reservation + check conditions (for building).
   * It is unlocked only when building is built
   */
  private static <T> ConfigurationWithAbstractPlan createOwnConfigurationWithAbstractPlanToBuildFromTemplate(
      FactWithOptionalValueSet<T> completedCount, FactKey<Integer> factCountToTrackPreviousCount,
      DesireKey desireKey, AUnitTypeWrapper unitTypeWrapper,
      FeatureContainerHeader featureContainerHeader,
      Stream<DesireKey> desireKeysWithSharedIntentionStream,
      Stream<DesireKey> desireKeysWithAbstractIntentionStream,
      SimpleDecisionStrategy forCommitment) {
    return ConfigurationWithAbstractPlan.builder()
        .reactionOnChangeStrategy((memory, desireParameters) -> {
              BotFacade.RESOURCE_MANAGER
                  .makeReservation(unitTypeWrapper, memory.getAgentId());
              memory.updateFact(factCountToTrackPreviousCount,
                  (int) memory.returnFactSetValueForGivenKey(STATIC_DEFENSE).orElse(Stream.empty())
                      .map(AUnit::getType)
                      .filter(typeWrapper -> typeWrapper.equals(unitTypeWrapper))
                      .count());
            }
        )
        .reactionOnChangeStrategyInIntention(
            (memory, desireParameters) -> {
              BotFacade.RESOURCE_MANAGER
                  .removeReservation(unitTypeWrapper, memory.getAgentId());
              memory.eraseFactValueForGivenKey(factCountToTrackPreviousCount);
            })
        .decisionInDesire(CommitmentDeciderInitializer.builder()
            .decisionStrategy(
                (dataForDecision, memory) ->
                    !BotFacade.RESOURCE_MANAGER
                        .hasMadeReservationOn(unitTypeWrapper, memory.getAgentId())
                        && !dataForDecision.madeDecisionToAny()
                        && !BuildLockerService.getInstance()
                        .isLocked(unitTypeWrapper)
                        //is base
                        && memory.returnFactValueForGivenKey(IS_BASE).get()
                        //we have base completed
                        && dataForDecision.getFeatureValueBeliefSets(BASE_IS_COMPLETED) == 1.0
                        //there is/will be no creep colony available
                        && forCommitment
                        .isTrue(dataForDecision.getFeatureValueBeliefSets(completedCount))
                        && Decider
                        .getDecision(AgentTypes.BASE_LOCATION, desireKey.getId(), dataForDecision,
                            featureContainerHeader, memory.getCurrentClock(), memory.getAgentId()))
            .globalBeliefTypes(featureContainerHeader.getConvertersForFactsForGlobalBeliefs())
            .globalBeliefSetTypes(featureContainerHeader.getConvertersForFactSetsForGlobalBeliefs())
            .globalBeliefTypesByAgentType(
                featureContainerHeader.getConvertersForFactsForGlobalBeliefsByAgentType())
            .globalBeliefSetTypesByAgentType(
                featureContainerHeader.getConvertersForFactSetsForGlobalBeliefsByAgentType())
            .beliefTypes(featureContainerHeader.getConvertersForFacts())
            .beliefSetTypes(
                Stream.concat(featureContainerHeader.getConvertersForFactSets().stream(),
                    Stream.of(BASE_IS_COMPLETED, completedCount))
                    .collect(Collectors.toSet()))
            .desiresToConsider(Collections.singleton(desireKey))
            .build())
        .decisionInIntention(CommitmentDeciderInitializer.builder()
            .decisionStrategy(
                (dataForDecision, memory) ->
                    !Decider
                        .getDecision(AgentTypes.BASE_LOCATION, desireKey.getId(), dataForDecision,
                            featureContainerHeader, memory.getCurrentClock(), memory.getAgentId())
                        || BuildLockerService.getInstance().isLocked(unitTypeWrapper)
                        || !memory.returnFactValueForGivenKey(IS_BASE).get()
                        || memory.returnFactValueForGivenKey(factCountToTrackPreviousCount)
                        .orElse(0) != dataForDecision.getFeatureValueBeliefSets(completedCount))
            .globalBeliefTypes(featureContainerHeader.getConvertersForFactsForGlobalBeliefs())
            .globalBeliefSetTypes(featureContainerHeader.getConvertersForFactSetsForGlobalBeliefs())
            .globalBeliefTypesByAgentType(
                featureContainerHeader.getConvertersForFactsForGlobalBeliefsByAgentType())
            .globalBeliefSetTypesByAgentType(
                featureContainerHeader.getConvertersForFactSetsForGlobalBeliefsByAgentType())
            .beliefTypes(featureContainerHeader.getConvertersForFacts())
            .beliefSetTypes(
                Stream.concat(featureContainerHeader.getConvertersForFactSets().stream(),
                    Stream.of(BASE_IS_COMPLETED, completedCount))
                    .collect(Collectors.toSet()))
            .build())
        .desiresWithAbstractIntention(
            desireKeysWithAbstractIntentionStream.collect(Collectors.toSet()))
        .desiresForOthers(desireKeysWithSharedIntentionStream.collect(Collectors.toSet()))
        .build();
  }

  public static final AgentTypeBaseLocation BASE_LOCATION = AgentTypeBaseLocation.builder()
      .initializationStrategy(type -> {

        //build creep colony
        ConfigurationWithAbstractPlan buildCreepColonyAbstractPlan = createOwnConfigurationWithAbstractPlanToBuildFromTemplate(
            COUNT_OF_CREEP_COLONIES_AT_BASE, FactKeys.CREEP_COLONY_COUNT,
            DesiresKeys.BUILD_CREEP_COLONY,
            AUnitTypeWrapper.CREEP_COLONY_TYPE, DEFENSE, Stream.of(DesiresKeys.BUILD_CREEP_COLONY),
            Stream.empty(), value -> value == 0);
        type.addConfiguration(DesiresKeys.BUILD_CREEP_COLONY, buildCreepColonyAbstractPlan, true);

        //common plan to build creep colony for sunken/spore colony
        ConfigurationWithAbstractPlan buildCreepColonyIfMissingAbstractPlan = ConfigurationWithAbstractPlan
            .builder()
            .reactionOnChangeStrategy((memory, desireParameters) -> BotFacade.RESOURCE_MANAGER
                .makeReservation(AUnitTypeWrapper.CREEP_COLONY_TYPE, memory.getAgentId()))
            .reactionOnChangeStrategyInIntention(
                (memory, desireParameters) -> BotFacade.RESOURCE_MANAGER
                    .removeReservation(AUnitTypeWrapper.CREEP_COLONY_TYPE, memory.getAgentId()))
            .decisionInDesire(CommitmentDeciderInitializer.builder()
                .decisionStrategy(
                    (dataForDecision, memory) ->
                        !dataForDecision.madeDecisionToAny()
                            && !BuildLockerService.getInstance()
                            .isLocked(AUnitTypeWrapper.CREEP_COLONY_TYPE)
                            //is base
                            && memory.returnFactValueForGivenKey(IS_BASE).get()
                            //we have base completed
                            && dataForDecision.getFeatureValueBeliefSets(BASE_IS_COMPLETED) == 1.0
                            //hack
                            && dataForDecision
                            .getFeatureValueBeliefSets(COUNT_OF_CREEP_COLONIES_AT_BASE) < 2)
                .beliefSetTypes(Stream.of(BASE_IS_COMPLETED, COUNT_OF_CREEP_COLONIES_AT_BASE)
                    .collect(Collectors.toSet()))
                .desiresToConsider(Collections.singleton(DesiresKeys.BUILD_CREEP_COLONY))
                .build())
            .decisionInIntention(CommitmentDeciderInitializer.builder()
                .decisionStrategy(
                    (dataForDecision, memory) ->
                        BuildLockerService.getInstance()
                            .isLocked(AUnitTypeWrapper.CREEP_COLONY_TYPE)
                            || !memory.returnFactValueForGivenKey(IS_BASE).get()
                            || dataForDecision
                            .getFeatureValueBeliefSets(COUNT_OF_CREEP_COLONIES_AT_BASE) > 0
                )
                .beliefSetTypes(Collections.singleton(COUNT_OF_CREEP_COLONIES_AT_BASE))
                .build())
            .desiresForOthers(Collections.singleton(DesiresKeys.MORPH_TO_CREEP_COLONY))
            .build();

        //shared desire to build creep colony
        ConfigurationWithSharedDesire buildCreepColonyShared = createConfigurationWithSharedDesireToBuildFromTemplate(
            DesiresKeys.MORPH_TO_CREEP_COLONY, AUnitTypeWrapper.CREEP_COLONY_TYPE,
            (memory, desireParameters) -> {
            }, (memory, desireParameters) -> {
            });
        type.addConfiguration(DesiresKeys.MORPH_TO_CREEP_COLONY, DesiresKeys.BUILD_CREEP_COLONY,
            buildCreepColonyShared);

        //build sunken as abstract plan
        ConfigurationWithAbstractPlan buildSunkenAbstract = createOwnConfigurationWithAbstractPlanToBuildFromTemplate(
            COUNT_OF_SUNKEN_COLONIES_AT_BASE, FactKeys.SUNKEN_COLONY_COUNT,
            DesiresKeys.BUILD_SUNKEN_COLONY,
            AUnitTypeWrapper.SUNKEN_COLONY_TYPE, DEFENSE,
            Stream.of(DesiresKeys.BUILD_SUNKEN_COLONY),
            Stream.of(DesiresKeys.BUILD_CREEP_COLONY), value -> value <= 3);
        type.addConfiguration(DesiresKeys.BUILD_SUNKEN_COLONY, buildSunkenAbstract, true);

        //to meet dependencies
        type.addConfiguration(DesiresKeys.BUILD_CREEP_COLONY, DesiresKeys.BUILD_SUNKEN_COLONY,
            buildCreepColonyIfMissingAbstractPlan);

        //shared desire to build creep colony
        ConfigurationWithSharedDesire buildSunkenShared = createConfigurationWithSharedDesireToBuildFromTemplate(
            DesiresKeys.MORPH_TO_SUNKEN_COLONY, AUnitTypeWrapper.SUNKEN_COLONY_TYPE,
            (memory, desireParameters) -> {
            }, (memory, desireParameters) -> {
            });
        type.addConfiguration(DesiresKeys.BUILD_SUNKEN_COLONY, DesiresKeys.BUILD_SUNKEN_COLONY,
            buildSunkenShared);

        //spore colony as abstract plan
        ConfigurationWithAbstractPlan buildSporeColonyAbstract = createOwnConfigurationWithAbstractPlanToBuildFromTemplate(
            COUNT_OF_SPORE_COLONIES_AT_BASE, FactKeys.SPORE_COLONY_COUNT,
            DesiresKeys.BUILD_SPORE_COLONY,
            AUnitTypeWrapper.SPORE_COLONY_TYPE, DEFENSE, Stream.of(DesiresKeys.BUILD_SPORE_COLONY),
            Stream.of(DesiresKeys.BUILD_CREEP_COLONY), value -> value <= 3);
        type.addConfiguration(DesiresKeys.BUILD_SPORE_COLONY, buildSporeColonyAbstract, true);

        //to meet dependencies
        type.addConfiguration(DesiresKeys.BUILD_CREEP_COLONY, DesiresKeys.BUILD_SPORE_COLONY,
            buildCreepColonyIfMissingAbstractPlan);

        //shared desire to build creep colony
        ConfigurationWithSharedDesire buildSporeColonyShared = createConfigurationWithSharedDesireToBuildFromTemplate(
            DesiresKeys.MORPH_TO_SPORE_COLONY, AUnitTypeWrapper.SPORE_COLONY_TYPE,
            (memory, desireParameters) -> {
            }, (memory, desireParameters) -> {
            });
        type.addConfiguration(DesiresKeys.BUILD_SPORE_COLONY, DesiresKeys.BUILD_SPORE_COLONY,
            buildSporeColonyShared);

        //TODO reason about enemy base, send overlord to our base when enemy is present

        //reason about last visit
        WithReasoningCommandDesiredBySelf reasonAboutVisit =
            WithReasoningCommandDesiredBySelf.builder()
                .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                  @Override
                  public boolean act(WorkingMemory memory) {
                    ABaseLocationWrapper base = memory.returnFactValueForGivenKey(IS_BASE_LOCATION)
                        .get();
                    OptionalInt frameWhenLastVisited = UnitWrapperFactory
                        .getStreamOfAllAlivePlayersUnits()
                        .filter(aUnitOfPlayer -> {
                          APosition aPosition = aUnitOfPlayer.getPosition();
                          return aPosition.distanceTo(base) < 5;
                        })
                        .mapToInt(AUnit::getFrameCount)
                        .max();
                    frameWhenLastVisited.ifPresent(
                        integer -> memory.updateFact(LAST_TIME_SCOUTED, integer));

                    //base status
                    boolean isBase = memory.returnFactSetValueForGivenKey(HAS_BASE).map(
                        Stream::count).orElse(0L) > 0;
                    memory.updateFact(IS_BASE, isBase);

                    //todo it is not our base and (it has enemy buildings on it || we haven't visited all base locations and this one is unvisited base location)
                    memory.updateFact(IS_ENEMY_BASE, !isBase &&
                        memory.returnFactSetValueForGivenKey(ENEMY_BUILDING).map(Stream::count)
                            .orElse(0L) > 0);
                    return true;
                  }
                })
                .decisionInDesire(CommitmentDeciderInitializer.builder()
                    .decisionStrategy((dataForDecision, memory) -> true)
                    .build())
                .decisionInIntention(CommitmentDeciderInitializer.builder()
                    .decisionStrategy((dataForDecision, memory) -> true)
                    .build())
                .build();
        type.addConfiguration(DesiresKeys.VISIT, reasonAboutVisit);

        //tell system to visit me
        ConfigurationWithSharedDesire visitMe = ConfigurationWithSharedDesire.builder()
            .sharedDesireKey(DesiresKeys.VISIT)
            .decisionInDesire(CommitmentDeciderInitializer.builder()
                .decisionStrategy((dataForDecision, memory) -> {

                  //do not visit our base location
                  if (dataForDecision.getFeatureValueBeliefs(FactConverters.IS_BASE) == 1.0) {
                    return false;
                  }

                  //if everything is visited desire visit
                  if (dataForDecision.getFeatureValueGlobalBeliefs(
                      FactConverters.COUNT_OF_VISITED_BASES)
                      == dataForDecision.getFeatureValueGlobalBeliefs(
                      FactConverters.AVAILABLE_BASES)) {
                    return true;
                  }

                  //visit bases first
                  long countOfUnvisitedStartingPositions = memory.getReadOnlyMemoriesForAgentType(
                      AgentTypes.BASE_LOCATION)
                      .filter(readOnlyMemory -> readOnlyMemory.isFactKeyForValueInMemory(IS_BASE))
                      .filter(readOnlyMemory -> !readOnlyMemory.returnFactValueForGivenKey(
                          IS_BASE).get())
                      .filter(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(
                          IS_BASE_LOCATION).get().isStartLocation())
                      .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(
                          LAST_TIME_SCOUTED))
                      .filter(integer -> !integer.isPresent())
                      .count();

                  //visit bases first
                  if (countOfUnvisitedStartingPositions > 0) {
                    return memory.returnFactValueForGivenKey(
                        IS_BASE_LOCATION).get().isStartLocation();
                  }

                  //all starting positions have been visit so desire to visit everything from now on
                  return true;
                })
                .globalBeliefTypesByAgentType(new HashSet<>(
                    Arrays.asList(FactConverters.COUNT_OF_VISITED_BASES,
                        FactConverters.AVAILABLE_BASES)))
                .beliefTypes(new HashSet<>(Collections.singleton(FactConverters.IS_BASE)))
                .useFactsInMemory(true)
                .build())
            .decisionInIntention(CommitmentDeciderInitializer.builder()
                .decisionStrategy((dataForDecision, memory) -> {

                  //do not visit our base location
                  if (dataForDecision.getFeatureValueBeliefs(FactConverters.IS_BASE) == 1.0) {
                    return true;
                  }

                  //stay if it is enemy base
                  if (dataForDecision.getFeatureValueBeliefs(FactConverters.IS_ENEMY_BASE) == 1.0) {
                    return false;
                  }

                  //not visit anything
                  if (dataForDecision.getFeatureValueDesireBeliefs(
                      FactConverters.LAST_TIME_SCOUTED) == -1
                      && dataForDecision.getFeatureValueBeliefs(
                      FactConverters.LAST_TIME_SCOUTED) == -1) {
                    return false;
                  }

                  ///we made first visit
                  if (dataForDecision.getFeatureValueDesireBeliefs(
                      FactConverters.LAST_TIME_SCOUTED) == -1
                      && dataForDecision.getFeatureValueBeliefs(
                      FactConverters.LAST_TIME_SCOUTED) > 0) {
                    return true;
                  }

                  //new visit
                  return (dataForDecision.getFeatureValueDesireBeliefs(
                      FactConverters.LAST_TIME_SCOUTED) < dataForDecision.getFeatureValueBeliefs(
                      FactConverters.LAST_TIME_SCOUTED));
                })
                .beliefTypes(new HashSet<>(
                    Arrays.asList(FactConverters.IS_BASE, FactConverters.LAST_TIME_SCOUTED,
                        FactConverters.IS_ENEMY_BASE)))
                .parameterValueTypes(
                    new HashSet<>(Collections.singleton(FactConverters.LAST_TIME_SCOUTED)))
                .build())
            .counts(1)
            .build();
        type.addConfiguration(DesiresKeys.VISIT, visitMe);

        //enemy's units
        WithReasoningCommandDesiredBySelf enemyUnits =
            WithReasoningCommandDesiredBySelf.builder()
                .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                  @Override
                  public boolean act(WorkingMemory memory) {
                    ABaseLocationWrapper base = memory.returnFactValueForGivenKey(IS_BASE_LOCATION)
                        .get();

                    Set<Enemy> enemies = UnitWrapperFactory.getStreamOfAllAliveEnemyUnits()
                        .filter(
                            enemy -> {
                              Optional<ABaseLocationWrapper> bL = enemy.getNearestBaseLocation();
                              return bL.isPresent() && bL.get().equals(base);
                            }).collect(Collectors.toSet());

                    memory.updateFactSetByFacts(ENEMY_UNIT, enemies);
                    memory.updateFactSetByFacts(ENEMY_BUILDING,
                        enemies.stream().filter(enemy -> enemy.getType().isBuilding()).collect(
                            Collectors.toSet()));
                    memory.updateFactSetByFacts(ENEMY_GROUND, enemies.stream().filter(
                        enemy -> !enemy.getType().isBuilding() && !enemy.getType().isFlyer())
                        .collect(
                            Collectors.toSet()));
                    memory.updateFactSetByFacts(ENEMY_AIR, enemies.stream().filter(
                        enemy -> !enemy.getType().isBuilding() && enemy.getType().isFlyer())
                        .collect(
                            Collectors.toSet()));

                    return true;
                  }
                })
                .decisionInDesire(CommitmentDeciderInitializer.builder()
                    .decisionStrategy((dataForDecision, memory) -> {
                      ABaseLocationWrapper base = memory.returnFactValueForGivenKey(
                          IS_BASE_LOCATION).get();
                      return UnitWrapperFactory.getStreamOfAllAliveEnemyUnits().filter(enemy -> {
                        Optional<ABaseLocationWrapper> bL = enemy.getNearestBaseLocation();
                        return bL.isPresent() && bL.get().equals(base);
                      }).count() > 0;
                    })
                    .build())
                .decisionInIntention(CommitmentDeciderInitializer.builder()
                    .decisionStrategy((dataForDecision, memory) -> true)
                    .build())
                .build();
        type.addConfiguration(DesiresKeys.ENEMIES_IN_LOCATION, enemyUnits);

        //player's units
        WithReasoningCommandDesiredBySelf ourUnits =
            WithReasoningCommandDesiredBySelf.builder()
                .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                  @Override
                  public boolean act(WorkingMemory memory) {
                    ABaseLocationWrapper base = memory.returnFactValueForGivenKey(IS_BASE_LOCATION)
                        .get();

                    Set<AUnitOfPlayer> playersUnits = UnitWrapperFactory
                        .getStreamOfAllAlivePlayersUnits().filter(
                            enemy -> {
                              Optional<ABaseLocationWrapper> bL = enemy.getNearestBaseLocation();
                              return bL.isPresent() && bL.get().equals(base);
                            }).collect(Collectors.toSet());

                    memory.updateFactSetByFacts(OUR_UNIT, playersUnits);
                    memory.updateFactSetByFacts(OWN_BUILDING,
                        playersUnits.stream().filter(own -> own.getType().isBuilding()).collect(
                            Collectors.toSet()));
                    memory.updateFactSetByFacts(OWN_GROUND, playersUnits.stream().filter(
                        own -> !own.getType().isBuilding() && !own.getType().isFlyer()).collect(
                        Collectors.toSet()));
                    memory.updateFactSetByFacts(OWN_AIR, playersUnits.stream().filter(
                        own -> !own.getType().isBuilding() && own.getType().isFlyer()).collect(
                        Collectors.toSet()));

                    //find new static defense buildings
                    if (memory.returnFactSetValueForGivenKey(OWN_BUILDING)
                        .orElse(Stream.empty())
                        .filter(aUnitOfPlayer -> aUnitOfPlayer.getType().isMilitaryBuilding())
                        .count() > 0) {
                      memory.updateFactSetByFacts(STATIC_DEFENSE,
                          memory.returnFactSetValueForGivenKey(OWN_BUILDING).orElse(Stream.empty())
                              .filter(aUnitOfPlayer -> aUnitOfPlayer.getType().isMilitaryBuilding())
                              .collect(Collectors.toSet()));
                    }

                    //eco buildings
                    memory.updateFactSetByFacts(HAS_BASE,
                        Stream.concat(memory.getReadOnlyMemoriesForAgentType(HATCHERY)
                                .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(
                                    REPRESENTS_UNIT).get())
                                .filter(
                                    aUnitOfPlayer -> aUnitOfPlayer.getNearestBaseLocation().isPresent())
                                .filter(aUnitOfPlayer -> base.equals(
                                    aUnitOfPlayer.getNearestBaseLocation().orElse(null))),
                            memory.getReadOnlyMemoriesForAgentType(LAIR)
                                .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(
                                    REPRESENTS_UNIT).get())
                                .filter(
                                    aUnitOfPlayer -> aUnitOfPlayer.getNearestBaseLocation()
                                        .isPresent())
                                .filter(aUnitOfPlayer -> base.equals(
                                    aUnitOfPlayer.getNearestBaseLocation().orElse(null)))
                        ).collect(Collectors.toSet()));
                    memory.updateFactSetByFacts(HAS_EXTRACTOR,
                        memory.returnFactSetValueForGivenKey(OWN_BUILDING).orElse(Stream.empty())
                            .filter(aUnitOfPlayer -> aUnitOfPlayer.getType().isGasBuilding())
                            .collect(Collectors.toSet()));

                    return true;
                  }
                })
                .decisionInDesire(CommitmentDeciderInitializer.builder()
                    .decisionStrategy((dataForDecision, memory) -> {
                      ABaseLocationWrapper base = memory.returnFactValueForGivenKey(
                          IS_BASE_LOCATION).get();
                      return UnitWrapperFactory.getStreamOfAllAlivePlayersUnits().filter(enemy -> {
                        Optional<ABaseLocationWrapper> bL = enemy.getNearestBaseLocation();
                        return bL.isPresent() && bL.get().equals(base);
                      }).count() > 0;
                    })
                    .build())
                .decisionInIntention(CommitmentDeciderInitializer.builder()
                    .decisionStrategy((dataForDecision, memory) -> true)
                    .build())
                .build();
        type.addConfiguration(DesiresKeys.FRIENDLIES_IN_LOCATION, ourUnits);

        //estimate enemy force
        type.addConfiguration(DesiresKeys.ESTIMATE_ENEMY_FORCE_IN_LOCATION,
            formForceEstimator(ENEMY_UNIT, ENEMY_BUILDING_STATUS,
                ENEMY_STATIC_AIR_FORCE_STATUS, ENEMY_STATIC_GROUND_FORCE_STATUS,
                ENEMY_AIR_FORCE_STATUS,
                ENEMY_GROUND_FORCE_STATUS));

        //estimate our force
        type.addConfiguration(
            DesiresKeys.ESTIMATE_OUR_FORCE_IN_LOCATION,
            formForceEstimator(OUR_UNIT, OWN_BUILDING_STATUS,
                OWN_STATIC_AIR_FORCE_STATUS, OWN_STATIC_GROUND_FORCE_STATUS, OWN_AIR_FORCE_STATUS,
                OWN_GROUND_FORCE_STATUS));

        //eco concerns
        WithReasoningCommandDesiredBySelf ecoConcerns =
            WithReasoningCommandDesiredBySelf.builder()
                .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                  @Override
                  public boolean act(WorkingMemory memory) {
                    ABaseLocationWrapper base = memory.returnFactValueForGivenKey(IS_BASE_LOCATION)
                        .get();

                    //workers
                    Set<ReadOnlyMemory> workersAroundBase = memory.getReadOnlyMemories()
                        .filter(
                            readOnlyMemory -> readOnlyMemory.isFactKeyForValueInMemory(LOCATION) &&
                                readOnlyMemory.returnFactValueForGivenKey(LOCATION).isPresent()
                                && readOnlyMemory.returnFactValueForGivenKey(LOCATION).get().equals(
                                base))
                        .filter(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(
                            REPRESENTS_UNIT).get().getType().isWorker()
                            || (!readOnlyMemory.returnFactValueForGivenKey(
                            REPRESENTS_UNIT).get().getTrainingQueue().isEmpty()
                            && readOnlyMemory.returnFactValueForGivenKey(
                            REPRESENTS_UNIT).get().getTrainingQueue().get(0).isWorker()))
                        .collect(Collectors.toSet());

                    memory.updateFactSetByFacts(WORKER_ON_BASE, workersAroundBase.stream()
                        .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(
                            REPRESENTS_UNIT).get())
                        .collect(Collectors.toSet()));
                    workersAroundBase = workersAroundBase.stream()
                        .filter(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(
                            REPRESENTS_UNIT).get().getType().isWorker())
                        .collect(Collectors.toSet());
                    memory.updateFactSetByFacts(WORKER_MINING_MINERALS, workersAroundBase.stream()
                        .filter(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(
                            IS_GATHERING_MINERALS).get())
                        .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(
                            REPRESENTS_UNIT).get())
                        .collect(Collectors.toSet()));
                    memory.updateFactSetByFacts(WORKER_MINING_GAS, workersAroundBase.stream()
                        .filter(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(
                            IS_GATHERING_GAS).get())
                        .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(
                            REPRESENTS_UNIT).get())
                        .collect(Collectors.toSet()));

                    return true;
                  }
                })
                .decisionInDesire(CommitmentDeciderInitializer.builder()
                    .decisionStrategy(
                        (dataForDecision, memory) -> dataForDecision.getFeatureValueBeliefs(
                            FactConverters.IS_BASE) == 1.0)
                    .beliefTypes(new HashSet<>(Collections.singleton(FactConverters.IS_BASE)))
                    .build())
                .decisionInIntention(CommitmentDeciderInitializer.builder()
                    .decisionStrategy((dataForDecision, memory) -> true)
                    .build())
                .build();
        type.addConfiguration(DesiresKeys.ECO_STATUS_IN_LOCATION, ecoConcerns);

        //Make request to start mining. Remove request when there are no more minerals to mine or there is no hatchery to bring mineral in
        ConfigurationWithSharedDesire mineMinerals = ConfigurationWithSharedDesire.builder()
            .sharedDesireKey(DesiresKeys.MINE_MINERALS_IN_BASE)
            .decisionInDesire(CommitmentDeciderInitializer.builder()
                .decisionStrategy(
                    (dataForDecision, memory) -> dataForDecision.getFeatureValueBeliefs(
                        FactConverters.IS_BASE) == 1
                        && dataForDecision.getFeatureValueDesireBeliefSets(
                        COUNT_OF_MINERALS_ON_BASE) > 0)
                .beliefTypes(new HashSet<>(Collections.singletonList(FactConverters.IS_BASE)))
                .parameterValueSetTypes(
                    new HashSet<>(Collections.singletonList(COUNT_OF_MINERALS_ON_BASE)))
                .build()
            )
            .decisionInIntention(CommitmentDeciderInitializer.builder()
                .decisionStrategy(
                    (dataForDecision, memory) -> dataForDecision.getFeatureValueBeliefs(
                        FactConverters.IS_BASE) == 0
                        || dataForDecision.getFeatureValueBeliefSets(COUNT_OF_MINERALS_ON_BASE) == 0
                        || dataForDecision.getFeatureValueBeliefSets(
                        COUNT_OF_MINERALS_ON_BASE) != dataForDecision
                        .getFeatureValueDesireBeliefSets(
                            COUNT_OF_MINERALS_ON_BASE)
                        || (dataForDecision.madeDecisionToAny()
                        && memory.returnFactSetValueForGivenKey(
                        WORKER_MINING_GAS).orElse(Stream.empty()).count() == 0)
                )
                .beliefTypes(new HashSet<>(Collections.singletonList(FactConverters.IS_BASE)))
                .beliefSetTypes(new HashSet<>(Collections.singletonList(COUNT_OF_MINERALS_ON_BASE)))
                .parameterValueSetTypes(
                    new HashSet<>(Collections.singletonList(COUNT_OF_MINERALS_ON_BASE)))
                .desiresToConsider(
                    new HashSet<>(Collections.singleton(DesiresKeys.MINE_GAS_IN_BASE)))
                .build()
            )
            .build();
        type.addConfiguration(DesiresKeys.MINE_MINERALS_IN_BASE, mineMinerals);

        //Make request to start mining gas. Remove request when there are no extractors or there is no hatchery to bring mineral in
        ConfigurationWithSharedDesire mineGas = ConfigurationWithSharedDesire.builder()
            .sharedDesireKey(DesiresKeys.MINE_GAS_IN_BASE)
            .counts(3)
            .decisionInDesire(CommitmentDeciderInitializer.builder()
                .decisionStrategy(
                    (dataForDecision, memory) ->
                        dataForDecision.getFeatureValueBeliefs(FactConverters.IS_BASE) == 1
                            && dataForDecision.getFeatureValueDesireBeliefSets(
                            COUNT_OF_EXTRACTORS_ON_BASE) > 0
                            && memory.returnFactSetValueForGivenKey(OWN_BUILDING).orElse(
                            Stream.empty())
                            .filter(aUnitOfPlayer -> aUnitOfPlayer.getType().isGasBuilding())
                            .anyMatch(
                                aUnitOfPlayer -> !aUnitOfPlayer.isBeingConstructed()
                                    && !aUnitOfPlayer
                                    .isMorphing()))
                .beliefTypes(new HashSet<>(Collections.singletonList(FactConverters.IS_BASE)))
                .parameterValueSetTypes(
                    new HashSet<>(Collections.singletonList(COUNT_OF_EXTRACTORS_ON_BASE)))
                .build())
            .decisionInIntention(CommitmentDeciderInitializer.builder()
                .decisionStrategy(
                    (dataForDecision, memory) -> dataForDecision.getFeatureValueBeliefs(
                        FactConverters.IS_BASE) == 0
                        || dataForDecision.getFeatureValueBeliefSets(COUNT_OF_EXTRACTORS_ON_BASE)
                        == 0
                )
                .beliefTypes(new HashSet<>(Collections.singletonList(FactConverters.IS_BASE)))
                .beliefSetTypes(
                    new HashSet<>(Collections.singletonList(COUNT_OF_EXTRACTORS_ON_BASE)))
                .build()
            )
            .build();
        type.addConfiguration(DesiresKeys.MINE_GAS_IN_BASE, mineGas);

        //hold ground
        ConfigurationWithSharedDesire holdGround = ConfigurationWithSharedDesire.builder()
            .sharedDesireKey(DesiresKeys.HOLD_GROUND)
            .reactionOnChangeStrategy(
                (memory, desireParameters) -> memory.updateFact(TIME_OF_HOLD_COMMAND,
                    memory.getReadOnlyMemoriesForAgentType(PLAYER)
                        .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(
                            MADE_OBSERVATION_IN_FRAME))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .findAny().orElse(null)))
            .decisionInDesire(CommitmentDeciderInitializer.builder()
                .decisionStrategy((dataForDecision, memory) ->
                    memory.returnFactValueForGivenKey(IS_ENEMY_BASE).get()
                        && Decider.getDecision(AgentTypes.BASE_LOCATION, DesireKeys.HOLD_GROUND,
                        dataForDecision, HOLDING, memory.getCurrentClock(), memory.getAgentId()))
                .globalBeliefTypes(HOLDING.getConvertersForFactsForGlobalBeliefs())
                .globalBeliefSetTypes(HOLDING.getConvertersForFactSetsForGlobalBeliefs())
                .globalBeliefTypesByAgentType(
                    HOLDING.getConvertersForFactsForGlobalBeliefsByAgentType())
                .globalBeliefSetTypesByAgentType(
                    HOLDING.getConvertersForFactSetsForGlobalBeliefsByAgentType())
                .beliefTypes(HOLDING.getConvertersForFacts())
                .beliefSetTypes(HOLDING.getConvertersForFactSets())
                .build())
            .decisionInIntention(CommitmentDeciderInitializer.builder()
                .decisionStrategy((dataForDecision, memory) -> !memory.returnFactValueForGivenKey(
                    IS_ENEMY_BASE).get()
                    || !Decider.getDecision(AgentTypes.BASE_LOCATION, DesireKeys.HOLD_GROUND,
                    dataForDecision, HOLDING, memory.getCurrentClock(), memory.getAgentId()))
                .globalBeliefTypes(HOLDING.getConvertersForFactsForGlobalBeliefs())
                .globalBeliefSetTypes(HOLDING.getConvertersForFactSetsForGlobalBeliefs())
                .globalBeliefTypesByAgentType(
                    HOLDING.getConvertersForFactsForGlobalBeliefsByAgentType())
                .globalBeliefSetTypesByAgentType(
                    HOLDING.getConvertersForFactSetsForGlobalBeliefsByAgentType())
                .beliefTypes(HOLDING.getConvertersForFacts())
                .beliefSetTypes(HOLDING.getConvertersForFactSets())
                .build())
            .build();
        type.addConfiguration(DesiresKeys.HOLD_GROUND, holdGround);

        //hold air
        ConfigurationWithSharedDesire holdAir = ConfigurationWithSharedDesire.builder()
            .sharedDesireKey(DesiresKeys.HOLD_AIR)
            .reactionOnChangeStrategy(
                (memory, desireParameters) -> memory.updateFact(TIME_OF_HOLD_COMMAND,
                    memory.getReadOnlyMemoriesForAgentType(PLAYER)
                        .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(
                            MADE_OBSERVATION_IN_FRAME))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .findAny().orElse(null)))
            .decisionInDesire(CommitmentDeciderInitializer.builder()
                .decisionStrategy((dataForDecision, memory) -> memory.returnFactValueForGivenKey(
                    IS_ENEMY_BASE).get()
                    && Decider.getDecision(AgentTypes.BASE_LOCATION, DesireKeys.HOLD_AIR,
                    dataForDecision, HOLDING, memory.getCurrentClock(), memory.getAgentId()))
                .globalBeliefTypes(HOLDING.getConvertersForFactsForGlobalBeliefs())
                .globalBeliefSetTypes(HOLDING.getConvertersForFactSetsForGlobalBeliefs())
                .globalBeliefTypesByAgentType(
                    HOLDING.getConvertersForFactsForGlobalBeliefsByAgentType())
                .globalBeliefSetTypesByAgentType(
                    HOLDING.getConvertersForFactSetsForGlobalBeliefsByAgentType())
                .beliefTypes(HOLDING.getConvertersForFacts())
                .beliefSetTypes(HOLDING.getConvertersForFactSets())
                .build())
            .decisionInIntention(CommitmentDeciderInitializer.builder()
                .decisionStrategy((dataForDecision, memory) -> !memory.returnFactValueForGivenKey(
                    IS_ENEMY_BASE).get()
                    || !Decider.getDecision(AgentTypes.BASE_LOCATION, DesireKeys.HOLD_AIR,
                    dataForDecision, HOLDING, memory.getCurrentClock(), memory.getAgentId()))
                .globalBeliefTypes(HOLDING.getConvertersForFactsForGlobalBeliefs())
                .globalBeliefSetTypes(HOLDING.getConvertersForFactSetsForGlobalBeliefs())
                .globalBeliefTypesByAgentType(
                    HOLDING.getConvertersForFactsForGlobalBeliefsByAgentType())
                .globalBeliefSetTypesByAgentType(
                    HOLDING.getConvertersForFactSetsForGlobalBeliefsByAgentType())
                .beliefTypes(HOLDING.getConvertersForFacts())
                .beliefSetTypes(HOLDING.getConvertersForFactSets())
                .build())
            .build();
        type.addConfiguration(DesiresKeys.HOLD_AIR, holdAir);

        //defend base
        ConfigurationWithSharedDesire defend = ConfigurationWithSharedDesire.builder()
            .sharedDesireKey(DesiresKeys.DEFEND)
            .reactionOnChangeStrategy(
                (memory, desireParameters) -> memory.updateFact(TIME_OF_HOLD_COMMAND,
                    memory.getReadOnlyMemoriesForAgentType(PLAYER)
                        .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(
                            MADE_OBSERVATION_IN_FRAME))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .findAny().orElse(null)))
            .decisionInDesire(CommitmentDeciderInitializer.builder()
                .decisionStrategy(
                    (dataForDecision, memory) -> memory.returnFactValueForGivenKey(IS_BASE).get()
                        && memory.returnFactSetValueForGivenKey(ENEMY_UNIT).orElse(
                        Stream.empty()).count() > 0)
                .build())
            .decisionInIntention(CommitmentDeciderInitializer.builder()
                .decisionStrategy(
                    (dataForDecision, memory) -> !memory.returnFactValueForGivenKey(IS_BASE).get()
                        || memory.returnFactSetValueForGivenKey(ENEMY_UNIT).orElse(
                        Stream.empty()).count() == 0)
                .build())
            .build();
        type.addConfiguration(DesiresKeys.DEFEND, defend);
      })
      .usingTypesForFacts(Stream
          .of(IS_BASE, IS_ENEMY_BASE, BASE_TO_MOVE, TIME_OF_HOLD_COMMAND, SUNKEN_COLONY_COUNT,
              SPORE_COLONY_COUNT, CREEP_COLONY_COUNT)
          .collect(Collectors.toSet()))
      .usingTypesForFactSets(Stream.of(WORKER_ON_BASE, ENEMY_BUILDING, ENEMY_AIR,
          ENEMY_GROUND, HAS_BASE, HAS_EXTRACTOR, OWN_BUILDING, OWN_AIR, OWN_GROUND,
          WORKER_MINING_MINERALS, WORKER_MINING_GAS, OWN_AIR_FORCE_STATUS, OWN_BUILDING_STATUS,
          OWN_GROUND_FORCE_STATUS, ENEMY_AIR_FORCE_STATUS, ENEMY_BUILDING_STATUS,
          ENEMY_GROUND_FORCE_STATUS, LOCKED_UNITS, LOCKED_BUILDINGS, ENEMY_STATIC_AIR_FORCE_STATUS,
          ENEMY_STATIC_GROUND_FORCE_STATUS, OWN_STATIC_AIR_FORCE_STATUS,
          OWN_STATIC_GROUND_FORCE_STATUS, STATIC_DEFENSE, ENEMY_UNIT, OUR_UNIT)
          .collect(Collectors.toSet()))
      .desiresWithIntentionToReason(Stream
          .of(DesiresKeys.ECO_STATUS_IN_LOCATION, DesiresKeys.FRIENDLIES_IN_LOCATION,
              DesiresKeys.ENEMIES_IN_LOCATION, DesiresKeys.ESTIMATE_ENEMY_FORCE_IN_LOCATION,
              DesiresKeys.ESTIMATE_OUR_FORCE_IN_LOCATION, DesiresKeys.VISIT)
          .collect(Collectors.toSet())
      )
      .desiresForOthers(Stream
          .of(DesiresKeys.VISIT, DesiresKeys.MINE_GAS_IN_BASE, DesiresKeys.MINE_MINERALS_IN_BASE,
              DesiresKeys.HOLD_GROUND, DesiresKeys.HOLD_AIR, DesiresKeys.DEFEND)
          .collect(Collectors.toSet()))
      .desiresWithAbstractIntention(
          Stream.of(DesiresKeys.BUILD_SUNKEN_COLONY, DesiresKeys.BUILD_SPORE_COLONY,
              DesiresKeys.BUILD_CREEP_COLONY)
              .collect(Collectors.toSet()))
      .build();

  /**
   * Template to create configuration of force estimation reasoning
   */
  private static ConfigurationWithCommand.WithReasoningCommandDesiredBySelf formForceEstimator(
      FactKey<? extends AUnit> factToSelectUnits,
      FactKey<UnitTypeStatus> buildings,
      FactKey<UnitTypeStatus> staticAir,
      FactKey<UnitTypeStatus> staticGround,
      FactKey<UnitTypeStatus> air,
      FactKey<UnitTypeStatus> ground) {
    return ConfigurationWithCommand.
        WithReasoningCommandDesiredBySelf.builder()
        .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
          @Override
          public boolean act(WorkingMemory memory) {
            Set<UnitTypeStatus> unitTypes = memory.returnFactSetValueForGivenKey(factToSelectUnits)
                .orElse(Stream.empty())
                .filter(enemy -> enemy.getType().isBuilding())
                .collect(Collectors.groupingBy(AUnit::getType)).entrySet().stream()
                .map(entry -> new UnitTypeStatus(entry.getKey(), entry.getValue().stream()))
                .collect(Collectors.toSet());

            memory.updateFactSetByFacts(buildings, unitTypes);
            memory.updateFactSetByFacts(staticAir, unitTypes.stream()
                .filter(unitTypeStatus -> unitTypeStatus.getUnitTypeWrapper()
                    .isMilitaryBuildingAntiAir())
                .collect(Collectors.toSet()));
            memory.updateFactSetByFacts(staticGround, unitTypes.stream()
                .filter(unitTypeStatus -> unitTypeStatus.getUnitTypeWrapper()
                    .isMilitaryBuildingAntiGround())
                .collect(Collectors.toSet()));

            Set<UnitTypeStatus> ownUnitsTypes = memory
                .returnFactSetValueForGivenKey(factToSelectUnits)
                .orElse(Stream.empty())
                .filter(
                    enemy -> !enemy.getType().isNotActuallyUnit() && !enemy.getType().isBuilding())
                .collect(Collectors.groupingBy(AUnit::getType)).entrySet().stream()
                .map(entry -> new UnitTypeStatus(entry.getKey(), entry.getValue().stream()))
                .collect(Collectors.toSet());
            memory.updateFactSetByFacts(air, ownUnitsTypes.stream()
                .filter(unitTypeStatus -> unitTypeStatus.getUnitTypeWrapper().canAttackAirUnits())
                .collect(Collectors.toSet()));
            memory.updateFactSetByFacts(ground, ownUnitsTypes.stream()
                .filter(
                    unitTypeStatus -> unitTypeStatus.getUnitTypeWrapper().canAttackGroundUnits())
                .collect(Collectors.toSet()));

            return true;
          }
        })
        .decisionInDesire(CommitmentDeciderInitializer.builder()
            .decisionStrategy((dataForDecision, memory) -> true)
            .build())
        .decisionInIntention(CommitmentDeciderInitializer.builder()
            .decisionStrategy((dataForDecision, memory) -> true)
            .build())
        .build();
  }

}
