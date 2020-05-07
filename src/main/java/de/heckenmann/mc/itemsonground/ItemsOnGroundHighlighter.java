package de.heckenmann.mc.itemsonground;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.stream.IntStream;
import java.util.stream.Stream;

/** @author heckenmann */
public class ItemsOnGroundForgeListener {

  private long lastTriggerTimestamp = 0;
  private ParticleFlame.Factory particleFlameFactory;
  private ParticleCloud.Factory particleCloudFactory;
  private final Configuration configuration;

  public ItemsOnGroundForgeListener(Configuration configuration) {
    this.configuration = configuration;
    this.createParticleFactory();
  }

  @SubscribeEvent
  public void onTick(FOVUpdateEvent event) {
    World world = Minecraft.getMinecraft().world;
    long currentTriggerTimestamp = System.currentTimeMillis();
    long timestampDelta = currentTriggerTimestamp - this.lastTriggerTimestamp;
    if (!this.configuration.isEnabled()
        || world == null
        || timestampDelta < this.configuration.getRefreshThreshold()) return;
    this.lastTriggerTimestamp = currentTriggerTimestamp;
    // Highlight Items on Ground
    world
        .getLoadedEntityList()
        .parallelStream()
        .filter(entity -> EntityItem.class.equals(entity.getClass()))
        .filter(entity -> entity.onGround || entity.isAirBorne)
        .map(entity -> createParticles(world, entity))
        .flatMap(particleStream -> particleStream)
        .sequential()
        .forEach(particle -> Minecraft.getMinecraft().effectRenderer.addEffect(particle));
  }

  /**
   * Creates the particles.
   *
   * @param world
   * @param entity
   * @return
   */
  private Stream<Particle> createParticles(World world, Entity entity) {
    return Stream.of(
        this.particleCloudFactory.createParticle(
            0, world, entity.posX, entity.posY + 5.0, entity.posZ, 0.0, 1.0, 0.0),
        this.particleFlameFactory.createParticle(
            0, world, entity.posX, entity.posY + 0.5, entity.posZ, 0.0, 0.2, 0.0));
  }

  /** Creates all needed factories. */
  private void createParticleFactory() {
    this.particleCloudFactory = new ParticleCloud.Factory();
    this.particleFlameFactory = new ParticleFlame.Factory();
  }
}
