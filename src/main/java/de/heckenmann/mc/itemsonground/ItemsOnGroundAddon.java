package de.heckenmann.mc.itemsonground;

import net.labymod.api.LabyModAddon;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.ControlElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.settings.elements.SliderElement;
import net.labymod.utils.Material;

import java.util.List;
import java.util.UUID;

/** @author heckenmann */
public class ItemsOnGroundAddon extends LabyModAddon {

  private final Configuration configuration;

  private final String FIELD_NAME_ENABLED = "enabled";
  private final String FIELD_NAME_SPAWNER_HIGHTLIGHTING_ENABLED = "spawnerHighlightingEnabled";
  private final String FIELD_NAME_REFRESH_THRESHOLD = "refreshThreshold";

  public ItemsOnGroundAddon() {
    this.configuration = new Configuration();
  }

  @Override
  public void onEnable() {}

  @Override
  public void init(String addonName, UUID uuid) {
    super.init(addonName, uuid);

    getApi().registerForgeListener(new ItemsOnGroundHighlighter(this.configuration));
    getApi().registerForgeListener(new SpawnerHighlighter(this.configuration));
  }

  @Override
  public void loadConfig() {
    this.configuration.setEnabled(
        getConfig().has(FIELD_NAME_ENABLED)
            ? getConfig().get(FIELD_NAME_ENABLED).getAsBoolean()
            : true);
    this.configuration.setSpawnerHighlightingEnabled(
        getConfig().has(FIELD_NAME_SPAWNER_HIGHTLIGHTING_ENABLED)
            ? getConfig().get(FIELD_NAME_SPAWNER_HIGHTLIGHTING_ENABLED).getAsBoolean()
            : false);
    this.configuration.setRefreshThreshold(
        getConfig().has(FIELD_NAME_REFRESH_THRESHOLD)
            ? getConfig().get(FIELD_NAME_REFRESH_THRESHOLD).getAsInt()
            : 50);
  }

  @Override
  protected void fillSettings(List<SettingsElement> subSettings) {
    System.out.println("fillSettings()");
    subSettings.add(
        new BooleanElement(
            "Enabled",
            this,
            new ControlElement.IconData(Material.REDSTONE_LAMP_ON),
            FIELD_NAME_ENABLED,
            this.configuration.isEnabled()));
    subSettings.add(
        new SliderElement(
                "refresh-threshold (ms)",
                this,
                new ControlElement.IconData(Material.ITEM_FRAME),
                FIELD_NAME_REFRESH_THRESHOLD,
                this.configuration.getRefreshThreshold())
            .setRange(0, 2000));
    subSettings.add(
        new BooleanElement(
            "Spawner highlighting",
            this,
            new ControlElement.IconData(Material.MOB_SPAWNER),
            FIELD_NAME_SPAWNER_HIGHTLIGHTING_ENABLED,
            this.configuration.isSpawnerHighlightingEnabled()));
  }
}
