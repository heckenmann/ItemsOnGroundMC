package de.heckenmann.mc.itemsonground;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleTotem;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.stream.IntStream;
import java.util.stream.Stream;

/** Dieser Listener highlightet Spawner über die gesamte Y-Achse. */
public class SpawnerHighlighter {

  private long lastTriggerTimestamp = 0;
  private final long thresholdMinMs = 500;
  private final Configuration configuration;
  private final IParticleFactory particleFactory;

  public SpawnerHighlighter(Configuration configuration) {
    this.configuration = configuration;
    this.particleFactory = new ParticleTotem.Factory();
  }

  @SubscribeEvent
  public void onTick(FOVUpdateEvent event) {
    World world = Minecraft.getMinecraft().world;
    long currentTriggerTimestamp = System.currentTimeMillis();
    long timestampDelta = currentTriggerTimestamp - this.lastTriggerTimestamp;
    if (world == null
        || !this.configuration.isEnabled()
        || !this.configuration.isSpawnerHighlightingEnabled()
        || timestampDelta < this.configuration.getRefreshThreshold() + this.thresholdMinMs) return;
    this.lastTriggerTimestamp = currentTriggerTimestamp;

    world
        .loadedTileEntityList
        .parallelStream()
        .filter(entity -> TileEntityMobSpawner.class.equals(entity.getClass()))
        .map(entity -> createParticlesForSpawner(world, entity.getPos()))
        .flatMap(particleStream -> particleStream)
        .sequential()
        .forEach(particle -> Minecraft.getMinecraft().effectRenderer.addEffect(particle));
  }

  private Stream<Particle> createParticlesForSpawner(World world, BlockPos position) {
    return IntStream.range(1, world.getActualHeight())
        .asDoubleStream()
        .mapToObj(
            d ->
                this.particleFactory.createParticle(
                    0, world, position.getX() + 0.5d, d, position.getZ() + 0.5d, 0.0, 1.0, 0.0));
  }
}
