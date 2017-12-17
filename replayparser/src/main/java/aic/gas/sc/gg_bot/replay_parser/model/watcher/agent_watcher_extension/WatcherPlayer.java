package aic.gas.sc.gg_bot.replay_parser.model.watcher.agent_watcher_extension;

import static aic.gas.sc.gg_bot.abstract_bot.model.bot.AgentTypes.BASE_LOCATION;
import static aic.gas.sc.gg_bot.abstract_bot.model.bot.FactKeys.*;

import aic.gas.sc.gg_bot.abstract_bot.model.UnitTypeStatus;
import aic.gas.sc.gg_bot.abstract_bot.model.UpgradeTypeStatus;
import aic.gas.sc.gg_bot.abstract_bot.model.bot.AgentTypes;
import aic.gas.sc.gg_bot.abstract_bot.model.game.wrappers.APlayer;
import aic.gas.sc.gg_bot.abstract_bot.model.game.wrappers.ARace;
import aic.gas.sc.gg_bot.abstract_bot.model.game.wrappers.AUnit;
import aic.gas.sc.gg_bot.abstract_bot.model.game.wrappers.UnitWrapperFactory;
import aic.gas.sc.gg_bot.abstract_bot.model.game.wrappers.WrapperTypeFactory;
import aic.gas.sc.gg_bot.replay_parser.model.AgentMakingObservations;
import aic.gas.sc.gg_bot.replay_parser.model.watcher.AgentWatcher;
import aic.gas.sc.gg_bot.replay_parser.model.watcher.agent_watcher_type_extension.WatcherPlayerType;
import bwapi.Game;
import bwapi.Player;
import bwapi.Race;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of watcher for player Created by Jan on 18-Apr-17.
 */
public class WatcherPlayer extends AgentWatcher<WatcherPlayerType> implements
    AgentMakingObservations {

  private APlayer player;

  public WatcherPlayer(Player player, Game game) {
    super(WatcherPlayerType.builder()
        .factKeys(new HashSet<>(Arrays
            .asList(AVAILABLE_MINERALS, ENEMY_RACE, AVAILABLE_GAS, POPULATION_LIMIT, POPULATION,
                IS_PLAYER)))
        .factSetsKeys(
            new HashSet<>(Arrays.asList(UPGRADE_STATUS, TECH_TO_RESEARCH, OUR_BASE, ENEMY_BASE,
                OWN_AIR_FORCE_STATUS, OWN_BUILDING_STATUS, OWN_GROUND_FORCE_STATUS,
                ENEMY_AIR_FORCE_STATUS, ENEMY_BUILDING_STATUS,
                ENEMY_GROUND_FORCE_STATUS, LOCKED_UNITS, LOCKED_BUILDINGS,
                ENEMY_STATIC_AIR_FORCE_STATUS, ENEMY_STATIC_GROUND_FORCE_STATUS,
                OWN_STATIC_AIR_FORCE_STATUS, OWN_STATIC_GROUND_FORCE_STATUS)))
        .playerEnvironmentObservation((aPlayer, beliefs) -> {
          APlayer p = aPlayer.makeObservationOfEnvironment(game.getFrameCount());
          beliefs.updateFactSetByFacts(UPGRADE_STATUS, WrapperTypeFactory.upgrades().stream()
              .map(aUpgradeTypeWrapper -> new UpgradeTypeStatus(
                  p.getPlayer().getUpgradeLevel(aUpgradeTypeWrapper.getType()),
                  aUpgradeTypeWrapper))
              .collect(Collectors.toSet()));
          beliefs.updateFactSetByFacts(TECH_TO_RESEARCH, WrapperTypeFactory.techs().stream()
              .filter(aTechTypeWrapper -> !p.getPlayer().hasResearched(aTechTypeWrapper.getType()))
              .collect(Collectors.toSet()));
          beliefs.updateFactSetByFacts(LOCKED_UNITS, WrapperTypeFactory.units().stream()
              .filter(aUnitTypeWrapper -> !p.getPlayer()
                  .hasUnitTypeRequirement(aUnitTypeWrapper.getType()))
              .collect(Collectors.toSet()));
          beliefs.updateFactSetByFacts(LOCKED_BUILDINGS, WrapperTypeFactory.buildings().stream()
              .filter(aUnitTypeWrapper -> !p.getPlayer()
                  .hasUnitTypeRequirement(aUnitTypeWrapper.getType()))
              .collect(Collectors.toSet()));
          return p;
        })
        .agentTypeID(AgentTypes.PLAYER)
        .reasoning((bl, ms) -> {
          //read data from player
          APlayer aPlayer = bl.returnFactValueForGivenKey(IS_PLAYER).get();
          bl.updateFact(AVAILABLE_MINERALS, (double) aPlayer.getMinerals());
          bl.updateFact(AVAILABLE_GAS, (double) aPlayer.getGas());
          bl.updateFact(POPULATION_LIMIT, (double) aPlayer.getSupplyTotal());
          bl.updateFact(POPULATION, (double) aPlayer.getSupplyUsed());

          //estimate enemy force
          Set<UnitTypeStatus> enemyBuildingsTypes = UnitWrapperFactory
              .getStreamOfAllAliveEnemyUnits()
              .filter(enemy -> enemy.getType().isBuilding())
              .collect(Collectors.groupingBy(AUnit::getType)).entrySet().stream()
              .map(entry -> new UnitTypeStatus(entry.getKey(), entry.getValue().stream()))
              .collect(Collectors.toSet());
          bl.updateFactSetByFacts(ENEMY_BUILDING_STATUS, enemyBuildingsTypes);
          bl.updateFactSetByFacts(ENEMY_STATIC_AIR_FORCE_STATUS, enemyBuildingsTypes.stream()
              .filter(
                  unitTypeStatus -> unitTypeStatus.getUnitTypeWrapper().isMilitaryBuildingAntiAir())
              .collect(Collectors.toSet()));
          bl.updateFactSetByFacts(ENEMY_STATIC_GROUND_FORCE_STATUS, enemyBuildingsTypes.stream()
              .filter(unitTypeStatus -> unitTypeStatus.getUnitTypeWrapper()
                  .isMilitaryBuildingAntiGround())
              .collect(Collectors.toSet()));
          Set<UnitTypeStatus> enemyUnitsTypes = UnitWrapperFactory.getStreamOfAllAliveEnemyUnits()
              .filter(
                  enemy -> !enemy.getType().isNotActuallyUnit() && !enemy.getType().isBuilding())
              .collect(Collectors.groupingBy(AUnit::getType)).entrySet().stream()
              .map(entry -> new UnitTypeStatus(entry.getKey(), entry.getValue().stream()))
              .collect(Collectors.toSet());
          bl.updateFactSetByFacts(ENEMY_AIR_FORCE_STATUS, enemyUnitsTypes.stream()
              .filter(unitTypeStatus -> unitTypeStatus.getUnitTypeWrapper().canAttackAirUnits())
              .collect(Collectors.toSet()));
          bl.updateFactSetByFacts(ENEMY_GROUND_FORCE_STATUS, enemyUnitsTypes.stream()
              .filter(unitTypeStatus -> unitTypeStatus.getUnitTypeWrapper().canAttackGroundUnits())
              .collect(Collectors.toSet()));

          //estimate our force
          Set<UnitTypeStatus> ownBuildingsTypes = UnitWrapperFactory
              .getStreamOfAllAlivePlayersUnits()
              .filter(enemy -> enemy.getType().isBuilding())
              .collect(Collectors.groupingBy(AUnit::getType)).entrySet().stream()
              .map(entry -> new UnitTypeStatus(entry.getKey(), entry.getValue().stream()))
              .collect(Collectors.toSet());
          bl.updateFactSetByFacts(OWN_BUILDING_STATUS, ownBuildingsTypes);
          bl.updateFactSetByFacts(OWN_STATIC_AIR_FORCE_STATUS, ownBuildingsTypes.stream()
              .filter(
                  unitTypeStatus -> unitTypeStatus.getUnitTypeWrapper().isMilitaryBuildingAntiAir())
              .collect(Collectors.toSet()));
          bl.updateFactSetByFacts(OWN_STATIC_GROUND_FORCE_STATUS, ownBuildingsTypes.stream()
              .filter(unitTypeStatus -> unitTypeStatus.getUnitTypeWrapper()
                  .isMilitaryBuildingAntiGround())
              .collect(Collectors.toSet()));
          Set<UnitTypeStatus> ownUnitsTypes = UnitWrapperFactory.getStreamOfAllAlivePlayersUnits()
              .filter(
                  enemy -> !enemy.getType().isNotActuallyUnit() && !enemy.getType().isBuilding())
              .collect(Collectors.groupingBy(AUnit::getType)).entrySet().stream()
              .map(entry -> new UnitTypeStatus(entry.getKey(), entry.getValue().stream()))
              .collect(Collectors.toSet());
          bl.updateFactSetByFacts(OWN_AIR_FORCE_STATUS, ownUnitsTypes.stream()
              .filter(unitTypeStatus -> unitTypeStatus.getUnitTypeWrapper().canAttackAirUnits())
              .collect(Collectors.toSet()));
          bl.updateFactSetByFacts(OWN_GROUND_FORCE_STATUS, ownUnitsTypes.stream()
              .filter(unitTypeStatus -> unitTypeStatus.getUnitTypeWrapper().canAttackGroundUnits())
              .collect(Collectors.toSet()));

          //enemy race
          Optional<Race> enemyRace = UnitWrapperFactory.getStreamOfAllAliveEnemyUnits()
              .map(enemy -> enemy.getType().getRace()).findAny();
          enemyRace.ifPresent(race -> bl.updateFact(ENEMY_RACE, ARace.getRace(race)));

          //bases
          bl.updateFactSetByFacts(OUR_BASE, ms.getStreamOfWatchers()
              .filter(agentWatcher -> agentWatcher.getAgentWatcherType().getName()
                  .equals(BASE_LOCATION.getName()))
              .filter(agentWatcher -> agentWatcher.getBeliefs().returnFactValueForGivenKey(IS_BASE)
                  .orElse(false))
              .map(agentWatcher -> agentWatcher.getBeliefs()
                  .returnFactValueForGivenKey(IS_BASE_LOCATION).get())
              .collect(Collectors.toSet()));
          bl.updateFactSetByFacts(ENEMY_BASE, ms.getStreamOfWatchers()
              .filter(agentWatcher -> agentWatcher.getAgentWatcherType().getName()
                  .equals(BASE_LOCATION.getName()))
              .filter(agentWatcher -> agentWatcher.getBeliefs()
                  .returnFactValueForGivenKey(IS_ENEMY_BASE).orElse(false))
              .map(agentWatcher -> agentWatcher.getBeliefs()
                  .returnFactValueForGivenKey(IS_BASE_LOCATION).get())
              .collect(Collectors.toSet()));
        })
        .planWatchers(new ArrayList<>())
        .build()
    );
    this.player = APlayer.wrapPlayer(player, game.getFrameCount()).get();
    beliefs.updateFact(IS_PLAYER, this.player);
  }

  public void makeObservation() {
    beliefs.updateFact(IS_PLAYER,
        agentWatcherType.getPlayerEnvironmentObservation().updateBeliefs(player, beliefs));
  }

}
