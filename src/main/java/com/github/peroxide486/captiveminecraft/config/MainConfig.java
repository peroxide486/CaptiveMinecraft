package com.github.peroxide486.captiveminecraft.config;

import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.Property;

import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class MainConfig implements SettingsHolder {

    public static final Property<Integer> MAX_REGION_SIZE =
            newProperty("region-setting.max-region-size", 1000);

    public static final Property<Integer> MIN_REGION_SIZE =
            newProperty("region-setting.min-region-size", 2);
}
