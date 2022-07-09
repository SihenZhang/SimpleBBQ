package com.sihenzhang.simplebbq.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

import javax.annotation.Nullable;

public class CampfireSmokeUnderGrillParticle extends TextureSheetParticle {
    public CampfireSmokeUnderGrillParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
        super(pLevel, pX, pY, pZ);
        this.scale(2.5F);
        this.setSize(0.25F, 0.25F);
        this.lifetime = this.random.nextInt(50) + 80;
        this.gravity = 3.0E-6F;
        this.xd = pXSpeed;
        this.yd = pYSpeed + (double) (this.random.nextFloat() / 500.0F);
        this.zd = pZSpeed;
    }

    @Override
    public void tick() {
        xo = x;
        yo = y;
        zo = z;
        if (age++ < lifetime && alpha > 0.0F) {
            xd += random.nextFloat() / 5000.0F * (random.nextBoolean() ? 1.0F : -1.0F);
            zd += random.nextFloat() / 5000.0F * (random.nextBoolean() ? 1.0F : -1.0F);
            yd -= gravity;
            this.move(xd, yd, zd);
            if (age >= lifetime - 60 && alpha > 0.01F) {
                alpha -= 0.02F;
            }
        } else {
            this.remove();
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet pSprites) {
            this.sprites = pSprites;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            var campfireSmokeParticle = new CampfireSmokeUnderGrillParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
            campfireSmokeParticle.setAlpha(0.6F);
            campfireSmokeParticle.pickSprite(sprites);
            return campfireSmokeParticle;
        }
    }
}
