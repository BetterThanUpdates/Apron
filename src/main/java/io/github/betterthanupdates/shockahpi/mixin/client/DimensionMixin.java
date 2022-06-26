package io.github.betterthanupdates.shockahpi.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import shockahpi.DimensionBase;

import net.minecraft.world.dimension.Dimension;

@Mixin(Dimension.class)
public class DimensionMixin {
	/**
	 * @author SAPI
	 * @reason
	 */
	@Overwrite
	public static Dimension getByID(int paramInt) {
		DimensionBase localDimensionBase = DimensionBase.getDimByNumber(paramInt);
		return localDimensionBase != null ? localDimensionBase.getWorldProvider() : null;
	}
}