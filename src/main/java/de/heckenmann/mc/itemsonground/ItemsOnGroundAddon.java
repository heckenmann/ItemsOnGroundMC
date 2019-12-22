package de.heckenmann.mc.itemsonground;

import net.labymod.api.LabyModAddon;
import net.labymod.settings.elements.SettingsElement;

import java.util.List;

/** @author heckenmann */
public class ItemsOnGroundAddon extends LabyModAddon {

  private ItemsOnGroundForgeListener listener;

  @Override
  public void onEnable() {
    this.listener = new ItemsOnGroundForgeListener();
    getApi().registerForgeListener(this.listener);
  }

  @Override
  public void loadConfig() {}

  @Override
  protected void fillSettings(List<SettingsElement> list) {}
}
