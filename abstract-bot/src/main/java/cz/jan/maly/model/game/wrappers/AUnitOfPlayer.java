package cz.jan.maly.model.game.wrappers;

import bwapi.*;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Extension of unit wrapper with fields visible to player only
 */
public class AUnitOfPlayer extends AUnit.Players {

    @Getter
    private final boolean isLoaded;

    @Getter
    private final boolean hasNuke;

    @Getter
    private final boolean isBlind;

    private final List<UnitType> trainingQueue;

    public List<AUnitTypeWrapper> getUpgrades() {
        return trainingQueue.stream()
                .map(WrapperTypeFactory::createFrom)
                .collect(Collectors.toList());
    }

    @Getter
    private final int removeTimer;

    @Getter
    private final int spellCooldown;

    @Getter
    private final int energy;

    @Getter
    private final int scarabCount;

    @Getter
    private final int plagueTimer;

    @Getter
    private final int orderTimer;

    @Getter
    private final int stasisTimer;

    @Getter
    private final int ensnareTimer;

    @Getter
    private final int lockdownTimer;

    @Getter
    private final int stimTimer;

    final Optional<Unit> transport;

    private final Optional<Integer> transportId;

    public Optional<AUnitOfPlayer> getTransport() {
        return transportId.flatMap(UnitWrapperFactory::getWrappedPlayersUnit);
    }

    final List<Unit> loadedUnits;

    private final List<Integer> loadedUnitsIds;

    public List<AUnitOfPlayer> getLoadedUnits() {
        return loadedUnitsIds.stream()
                .map(UnitWrapperFactory::getWrappedPlayersUnit)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Getter
    private final boolean isUpgrading;

    @Getter
    private final boolean isTraining;

    @Getter
    private final boolean isResearching;

    @Getter
    private final boolean canAttackUnit;

    @Getter
    private final int remainingBuildTime;

    @Getter
    private final int remainingTrainTime;

    @Getter
    private final int remainingUpgradeTime;

    @Getter
    private final int airWeaponCooldown;

    @Getter
    private final int defenseMatrixTimer;

    @Getter
    private final int remainingResearchTime;

    @Getter
    private final int spaceRemaining;

    @Getter
    private final int defenseMatrixPoints;

    @Getter
    private final int irradiateTimer;

    @Getter
    private final int lastCommandFrame;

    @Getter
    private final int acidSporeCount;

    @Getter
    private final int spiderMineCount;

    @Getter
    private final int maelstromTimer;

    @Getter
    private final int groundWeaponCooldown;

    @Getter
    private final boolean isHoldingPosition;

    @Getter
    private final boolean isDefenseMatrixed;

    @Getter
    private final boolean isUnderDisruptionWeb;

    AUnitOfPlayer(Unit unit) {
        super(unit);

        this.isLoaded = unit.isLoaded();
        this.hasNuke = unit.hasNuke();
        this.isBlind = unit.isBlind();
        this.trainingQueue = new ArrayList<>(unit.getTrainingQueue());
        this.removeTimer = unit.getRemoveTimer();
        this.spellCooldown = unit.getSpellCooldown();
        this.energy = unit.getEnergy();
        this.scarabCount = unit.getScarabCount();
        this.plagueTimer = unit.getPlagueTimer();
        this.orderTimer = unit.getOrderTimer();
        this.stasisTimer = unit.getStasisTimer();
        this.ensnareTimer = unit.getEnsnareTimer();
        this.lockdownTimer = unit.getLockdownTimer();
        this.stimTimer = unit.getStimTimer();
        if (unit.getTransport() != null) {
            this.transportId = Optional.of(unit.getTransport().getID());
        } else {
            this.transportId = Optional.empty();
        }
        this.transport = Optional.ofNullable(unit.getTransport());
        this.loadedUnits = new ArrayList<>(unit.getLoadedUnits());
        this.loadedUnitsIds = this.loadedUnits.stream()
                .map(Unit::getID)
                .collect(Collectors.toList());
        this.isUpgrading = unit.isUpgrading();
        this.isTraining = unit.isTraining();
        this.isResearching = unit.isResearching();
        this.canAttackUnit = unit.canAttackUnit();
        this.remainingBuildTime = unit.getRemainingBuildTime();
        this.remainingTrainTime = unit.getRemainingTrainTime();
        this.remainingUpgradeTime = unit.getRemainingUpgradeTime();
        this.airWeaponCooldown = unit.getAirWeaponCooldown();
        this.defenseMatrixTimer = unit.getDefenseMatrixTimer();
        this.remainingResearchTime = unit.getRemainingResearchTime();
        this.spaceRemaining = unit.getSpaceRemaining();
        this.defenseMatrixPoints = unit.getDefenseMatrixPoints();
        this.irradiateTimer = unit.getIrradiateTimer();
        this.lastCommandFrame = unit.getLastCommandFrame();
        this.acidSporeCount = unit.getAcidSporeCount();
        this.spiderMineCount = unit.getSpiderMineCount();
        this.maelstromTimer = unit.getMaelstromTimer();
        this.groundWeaponCooldown = unit.getGroundWeaponCooldown();
        this.isHoldingPosition = unit.isHoldingPosition();
        this.isDefenseMatrixed = unit.isDefenseMatrixed();
        this.isUnderDisruptionWeb = unit.isUnderDisruptionWeb();
    }

    /**
     * Returns true if this unit is capable of attacking <b>otherUnit</b>. For example Zerglings can't attack
     * flying targets and Corsairs can't attack ground targets.
     */
    public boolean canAttackThisKindOfUnit(AUnitOfPlayer otherUnit) {

        // Enemy is GROUND unit
        if (!otherUnit.getType().isFlyer()) {
            return getType().canAttackGroundUnits() && getGroundWeaponCooldown() == 0;
        } // Enemy is AIR unit
        else {
            return getType().canAttackAirUnits() && getAirWeaponCooldown() == 0;
        }
    }

}