package net.islandearth.taleofkingdoms.common.entity.render;

import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TOKBipedRender<T extends MobEntity, M extends BipedModel<T>> extends BipedRenderer<MobEntity, PlayerModel<MobEntity>> {
	
	private final ResourceLocation DEFAULT_RES_LOC;

	public TOKBipedRender(EntityRendererManager renderManagerIn, PlayerModel<MobEntity> modelBipedIn,
			float shadowSize, ResourceLocation skinLocation) {
		super(renderManagerIn, modelBipedIn, shadowSize);
		this.DEFAULT_RES_LOC = skinLocation;
	}
	
	@Override
	public ResourceLocation getEntityTexture(MobEntity entity) {
		return DEFAULT_RES_LOC;
	}
}
