package com.karasu256.locatorbarplus.config;

import com.karasu256.locatorbarplus.client.*;
import com.karasu256.locatorbarplus.impl.IActivationCondition;
import com.karasu256.locatorbarplus.impl.IActivationLogic;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import java.util.function.Supplier;

import static com.karasu256.locatorbarplus.Constants.MOD_ID;

@Config(name = MOD_ID)
public class ModConfig implements ConfigData {
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public General general = new General();

    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public LocatorBar locatorBar = new LocatorBar();

    public static class General {
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public ConditionMode conditionMode = ConditionMode.SNEAK;
        public boolean alwaysHideLocatorBar = false;
        public float sneakThresholdSeconds = 5.0f;
    }

    public enum ConditionMode {
        SNEAK(SneakCondition::new, ThresholdTimerLogic::new),
        HOLD(KeyHoldCondition::new, InstantLogic::new),
        TOGGLE(KeyToggleCondition::new, InstantLogic::new);

        private final Supplier<IActivationCondition> conditionFactory;
        private final Supplier<IActivationLogic> logicFactory;

        ConditionMode(Supplier<IActivationCondition> conditionFactory, Supplier<IActivationLogic> logicFactory) {
            this.conditionFactory = conditionFactory;
            this.logicFactory = logicFactory;
        }

        public IActivationCondition createCondition() {
            return conditionFactory.get();
        }

        public IActivationLogic createLogic() {
            return logicFactory.get();
        }
    }

    public static class LocatorBar {
        @ConfigEntry.Gui.TransitiveObject
        public float experienceBarTransparency = 0.5f;
    }
}
