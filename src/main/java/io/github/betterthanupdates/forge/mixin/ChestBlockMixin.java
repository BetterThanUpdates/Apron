package io.github.betterthanupdates.forge.mixin;

import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.block.ChestBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.DoubleChestInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ChestBlock.class)
public abstract class ChestBlockMixin extends BlockWithEntity {

	protected ChestBlockMixin(int i, Material arg) {
		super(i, arg);
	}

	/**
	 * @author Forge
	 */
	@Overwrite
	public boolean canUse(World world, int i, int j, int k, PlayerEntity playerEntity) {
		Object obj = world.getBlockEntity(i, j, k);
		if (world.isBlockSolidOnSide(i, j + 1, k, 0)) {
			return true;
		} else if (world.getBlockId(i - 1, j, k) == this.id && world.isBlockSolidOnSide(i - 1, j + 1, k, 0)) {
			return true;
		} else if (world.getBlockId(i + 1, j, k) == this.id && world.isBlockSolidOnSide(i + 1, j + 1, k, 0)) {
			return true;
		} else if (world.getBlockId(i, j, k - 1) == this.id && world.isBlockSolidOnSide(i, j + 1, k - 1, 0)) {
			return true;
		} else if (world.getBlockId(i, j, k + 1) == this.id && world.isBlockSolidOnSide(i, j + 1, k + 1, 0)) {
			return true;
		} else {
			if (world.getBlockId(i - 1, j, k) == this.id) {
				obj = new DoubleChestInventory("Large chest", (ChestBlockEntity) world.getBlockEntity(i - 1, j, k), (Inventory) obj);
			}

			if (world.getBlockId(i + 1, j, k) == this.id) {
				obj = new DoubleChestInventory("Large chest", (Inventory) obj, (ChestBlockEntity) world.getBlockEntity(i + 1, j, k));
			}

			if (world.getBlockId(i, j, k - 1) == this.id) {
				obj = new DoubleChestInventory("Large chest", (ChestBlockEntity) world.getBlockEntity(i, j, k - 1), (Inventory) obj);
			}

			if (world.getBlockId(i, j, k + 1) == this.id) {
				obj = new DoubleChestInventory("Large chest", (Inventory) obj, (ChestBlockEntity) world.getBlockEntity(i, j, k + 1));
			}

			if (!world.isClient) {
				playerEntity.openChestScreen((Inventory) obj);
			}
			return true;
		}
	}
}