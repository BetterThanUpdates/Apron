package io.github.betterthanupdates.babricated.mixin;

import io.github.betterthanupdates.babricated.BabricatedForge;
import modloader.ModLoader;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.menu.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

import static io.github.betterthanupdates.babricated.BabricatedForge.fabricModsLoaded;
import static io.github.betterthanupdates.babricated.BabricatedForge.rmlModsLoaded;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
	@ModifyConstant(method = "render", constant = @Constant(stringValue = "Minecraft Beta 1.7.3"))
	private String babricated$renderVersion(final String constant) {
		return BabricatedForge.versionString(constant);
	}

	@Inject(method = "render(IIF)V", at = @At(value = "INVOKE",
			target = "Lorg/lwjgl/opengl/GL11;glPopMatrix()V", shift = At.Shift.AFTER, remap = false))
	private void babricated$renderMods(int a, int b, float c, CallbackInfo ci) {
		this.drawTextWithShadow(this.textRenderer,
				String.format("(%s, %s)", rmlModsLoaded(), fabricModsLoaded()), 2, 12, 0x50_50_50);
	}

	@Inject(method = "init", at = @At("HEAD"))
	private void babricated$setModCount(CallbackInfo ci) {
		BabricatedForge.fabricModCount = FabricLoader.getInstance().getAllMods().stream()
				.filter((modContainer -> !Objects.equals(modContainer.getMetadata().getId(), "minecraft"))).count();
		BabricatedForge.rmlModCount = ModLoader.getLoadedMods().size();
	}
}