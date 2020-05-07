package de.heckenmann.mc.itemsonground;

public class Configuration {

  /*
   * Addon enabled?
   */
  private boolean enabled;

  private boolean spawnerHighlightingEnabled;

  /*
   * Refresh-Threshold in ms.
   */
  private int refreshThreshold;

  public void setRefreshThreshold(int refreshThreshold) {
    this.refreshThreshold = refreshThreshold;
  }

  public int getRefreshThreshold() {
    return refreshThreshold;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public boolean isSpawnerHighlightingEnabled() {
    return spawnerHighlightingEnabled;
  }

  public void setSpawnerHighlightingEnabled(boolean spawnerHighlightingEnabled) {
    this.spawnerHighlightingEnabled = spawnerHighlightingEnabled;
  }
}
