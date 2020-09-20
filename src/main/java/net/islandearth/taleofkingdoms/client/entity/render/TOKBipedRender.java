package net.islandearth.taleofkingdoms.client.entity.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class TOKBipedRender<T extends MobEntity, M extends BipedEntityModel<T>> extends BipedEntityRenderer<MobEntity, PlayerEntityModel<MobEntity>> {
	
	private final Identifier DEFAULT_RES_LOC;

	public TOKBipedRender(EntityRenderDispatcher renderManagerIn, PlayerEntityModel<MobEntity> modelBipedIn,
						  float shadowSize, Identifier skinLocation) {
		super(renderManagerIn, modelBipedIn, shadowSize);
		this.DEFAULT_RES_LOC = skinLocation;
	}
	
	@Override
	public Identifier getTexture(MobEntity entity) {
		return DEFAULT_RES_LOC;
	}
}
