package com.sammy.malum.common.item.curiosities.weapons.staff;

import com.sammy.malum.common.entity.bolt.AbstractBoltProjectileEntity;
import com.sammy.malum.common.entity.bolt.AuricFlameBoltEntity;
import com.sammy.malum.common.entity.nitrate.EthericNitrateEntity;
import com.sammy.malum.registry.client.ParticleRegistry;
import com.sammy.malum.registry.common.SoundRegistry;
import io.github.fabricators_of_create.porting_lib.entity.events.living.LivingHurtEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.helpers.RandomHelper;
import team.lodestar.lodestone.registry.common.LodestoneAttributeRegistry;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;
import team.lodestar.lodestone.systems.particle.render_types.LodestoneWorldParticleRenderType;
import team.lodestar.lodestone.systems.particle.world.behaviors.DirectionalParticleBehavior;
import team.lodestar.lodestone.systems.particle.world.behaviors.components.DirectionalBehaviorComponent;

public class AuricFlameStaffItem extends AbstractStaffItem {

    public static final ColorParticleData AURIC_COLOR_DATA = EthericNitrateEntity.AURIC_COLOR_DATA;

    public AuricFlameStaffItem(Tier tier, float magicDamage, Properties builderIn) {
        super(tier, -0.2f, 30, magicDamage, builderIn);
    }

    @Override
    public void hurtEvent(LivingHurtEvent event, LivingEntity attacker, LivingEntity target, ItemStack stack) {
        if (!(event.getSource().getDirectEntity() instanceof AbstractBoltProjectileEntity)) {
            target.setSecondsOnFire(4);
            attacker.level().playSound(null, target.getX(), target.getY(), target.getZ(), SoundRegistry.AURIC_FLAME_MOTIF.get(), attacker.getSoundSource(), 1, 1.25f);
        }
        super.hurtEvent(event, attacker, target, stack);
    }

    @Override
    public int getCooldownDuration(Level level, LivingEntity livingEntity) {
        return 160;
    }

    @Override
    public int getProjectileCount(Level level, LivingEntity livingEntity, float pct) {
        return pct == 1f ? 5 : 0;
    }

    @Override
    public void fireProjectile(LivingEntity player, ItemStack stack, Level level, InteractionHand hand, float chargePercentage, int count) {
        final float ceil = (float) Math.ceil(count / 2f);
        float spread = count > 0 ? ceil * 0.075f * (count % 2L == 0 ? 1 : -1) : 0f;
        float pitchOffset = 6f - (3f + ceil);
        int spawnDelay = count * 3;
        float velocity = 2f;
        float magicDamage = (float) player.getAttributes().getValue(LodestoneAttributeRegistry.MAGIC_DAMAGE.get()) - 2;
        Vec3 pos = getProjectileSpawnPos(player, hand, 0.5f, 0.5f);
        AuricFlameBoltEntity entity = new AuricFlameBoltEntity(level, pos.x, pos.y, pos.z);
        entity.setData(player, magicDamage, spawnDelay);
        entity.setItem(stack);

        entity.shootFromRotation(player, player.getXRot(), player.getYRot(), -pitchOffset, velocity, 0f);
        Vec3 projectileDirection = entity.getDeltaMovement();
        float yRot = ((float) (Mth.atan2(projectileDirection.x, projectileDirection.z) * (double) (180F / (float) Math.PI)));
        float yaw = (float) Math.toRadians(yRot);
        Vec3 left = new Vec3(-Math.cos(yaw), 0, Math.sin(yaw));
        entity.setDeltaMovement(entity.getDeltaMovement().add(left.scale(spread)));
        level.addFreshEntity(entity);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void spawnChargeParticles(Level pLevel, LivingEntity pLivingEntity, Vec3 pos, ItemStack pStack, float pct) {
        RandomSource random = pLevel.random;
        final SpinParticleData spinData = SpinParticleData.createRandomDirection(random, 0.25f, 0.5f).setSpinOffset(RandomHelper.randomBetween(random, 0f, 6.28f)).build();
        WorldParticleBuilder.create(ParticleRegistry.HEXAGON, new DirectionalBehaviorComponent(pLivingEntity.getLookAngle().normalize()))
                .setTransparencyData(GenericParticleData.create(0.5f * pct, 0f).setEasing(Easing.SINE_IN_OUT, Easing.SINE_IN).build())
                .setScaleData(GenericParticleData.create(0.35f * pct, 0).setEasing(Easing.SINE_IN_OUT).build())
                .setSpinData(spinData)
                .setColorData(AURIC_COLOR_DATA)
                .setLifetime(5)
                .setMotion(pLivingEntity.getLookAngle().normalize().scale(0.05f))
                .enableNoClip()
                .enableForcedSpawn()
                .setLifeDelay(2)
                .spawn(pLevel, pos.x, pos.y, pos.z)
                .setRenderType(LodestoneWorldParticleRenderType.LUMITRANSPARENT)
                .spawn(pLevel, pos.x, pos.y, pos.z);
    }
}
