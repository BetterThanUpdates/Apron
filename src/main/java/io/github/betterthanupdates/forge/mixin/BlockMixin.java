package io.github.betterthanupdates.forge.mixin;

import forge.ForgeHooks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import io.github.betterthanupdates.forge.block.ForgeBlock;

/**
 * Default implementation of the new methods provided by Minecraft Forge.
 */
@Mixin(Block.class)
public abstract class BlockMixin implements ForgeBlock {
	@Shadow
	protected float hardness;

	@Shadow
	@Final
	public int id;

	@Shadow
	@Final
	public Material material;

	@Shadow
	public abstract boolean isFullCube();

	@Shadow
	@Final
	public static int[] EMITTANCE;

	/**
	 * @author Eloraam
	 * @reason Minecraft Forge extension of this method
	 */
	@Inject(method = "getHardness(Lnet/minecraft/entity/player/PlayerEntity;)F", at = @At("RETURN"), cancellable = true)
	public void getHardness(PlayerEntity player, CallbackInfoReturnable<Float> cir) {
		cir.setReturnValue(this.blockStrength(player, 0));
	}

	@Override
	public boolean isLadder() {
		return false;
	}

	@Override
	public boolean isBlockNormalCube(World world, int x, int y, int z) {
		return this.material.hasNoSuffocation() && this.isFullCube();
	}

	@Override
	public boolean isBlockSolidOnSide(World world, int x, int y, int z, int side) {
		return this.isBlockNormalCube(world, x, y, z);
	}

	@Override
	public boolean isBlockReplaceable(World world, int x, int y, int z) {
		return false;
	}

	@Override
	public boolean isBlockBurning(World world, int x, int y, int z) {
		return false;
	}

	@Override
	public boolean isAirBlock(World world, int x, int y, int z) {
		return false;
	}

	/**
	 * Gets hardness for a block, taking into account its hardness.<br>
	 * By default, this has the same behavior as {@link Block#getHardness()}
	 *
	 * @param meta the meta value of the block state
	 * @return the hardness (time to break) of the block
	 */
	@Override
	public float getHardness(int meta) {
		return this.hardness;
	}

	@Override
	public float blockStrength(World world, PlayerEntity player, int x, int y, int z) {
		int meta = world.getBlockMeta(x, y, z);
		return this.blockStrength(player, meta);
	}

	@Override
	public float blockStrength(PlayerEntity player, int meta) {
		return ForgeHooks.blockStrength((Block) (Object) this, player, meta);
	}

	@Override
	public boolean canHarvestBlock(PlayerEntity player, int meta) {
		return ForgeHooks.canHarvestBlock((Block) (Object) this, player, meta);
	}

	@Override
	public int getLightValue(BlockView blockView, int x, int y, int z) {
		return EMITTANCE[this.id];
	}
}
