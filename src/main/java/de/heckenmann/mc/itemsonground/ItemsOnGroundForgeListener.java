package de.heckenmann.mc.itemsonground;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleCloud;
import net.minecraft.client.particle.ParticleFlame;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.world.World;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.stream.Stream;

/** @author heckenmann */
public class ItemsOnGroundForgeListener {

  private ParticleFlame.Factory particleFlameFactory;
  private IParticleFactory particleCloudFactory;

  @SubscribeEvent
  public void onTick(FOVUpdateEvent event) {
    createParticleFactory();
    World world = Minecraft.getMinecraft().world;
    if (world == null) return;
    world.getLoadedEntityList().stream()
        .filter(entity -> EntityItem.class.equals(entity.getClass()))
        .filter(entity -> entity.onGround || entity.isAirBorne)
        //        .forEach(e -> System.out.println(e.getClass().getName() + " " + e.ticksExisted));
        .map(entity -> createParticles(world, entity))
        .flatMap(particleStream -> particleStream)
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
    Particle[] particle = {
      this.particleCloudFactory.createParticle(
          0, world, entity.posX, entity.posY + 5.0, entity.posZ, 0.0, 1.0, 0.0),
      this.particleFlameFactory.createParticle(
          0, world, entity.posX, entity.posY, entity.posZ, 0.0, 0.2, 0.0)
    };
    return Stream.of(particle);
  }

  /** Creates all needed factories. */
  private void createParticleFactory() {
    this.particleCloudFactory = new ParticleCloud.Factory();
    this.particleFlameFactory = new ParticleFlame.Factory();
  }
}
