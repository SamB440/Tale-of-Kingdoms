package com.convallyria.taleofkingdoms.client.entity.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.QuadrupedEntityModel;
import net.minecraft.entity.Entity;

@Environment(EnvType.CLIENT)
public class BoarEntityModel<T extends Entity> extends QuadrupedEntityModel<T> {

    private final ModelPart base;

    public BoarEntityModel() {
        this(0.0F);
        this.textureHeight = 16;
        this.textureWidth = 16;
    }

    public BoarEntityModel(float scale) {
        super(6, scale, false, 4.0F, 4.0F, 2.0F, 2.0F, 24);
        this.textureWidth = 16;
        this.textureHeight = 16;
        this.head.setTextureOffset(16, 16).addCuboid(-2.0F, 0.0F, -9.0F, 4.0F, 3.0F, 1.0F, scale);
        this.base = new ModelPart(this, 0, 0);
    }
}