package io.github.betterthanupdates.babricated;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import fr.catcore.modremapperapi.utils.Constants;
import modloader.BaseMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.impl.launch.FabricLauncherBase;
import net.fabricmc.mapping.tree.ClassDef;
import net.fabricmc.mapping.tree.FieldDef;
import net.legacyfabric.fabric.api.logger.v1.Logger;
import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.include.com.google.common.collect.ImmutableList;
import shockahpi.ShockAhPI;

import net.minecraft.world.biome.Biome;

@ApiStatus.Internal
public final class BabricatedForge {
	public static final String MOD_ID = "babricated-forge";
	public static long fabricModCount = 0, rmlModCount = 0;

	// Logging
	public static final Logger LOGGER;

	public static final File MOD_CACHE_FOLDER;

	public static final ModContainer MOD_CONTAINER;

	public static final List<Supplier<? extends BaseMod>> BUILTIN_RML_MODS = ImmutableList.of(
			ShockAhPI::new
	);

	static {
		LOGGER = Logger.get("Babricated Forge");

		MOD_CACHE_FOLDER = new File(Constants.VERSIONED_FOLDER, "mods");

		if (!(MOD_CACHE_FOLDER.exists() || MOD_CACHE_FOLDER.mkdirs())) {
			throw new RuntimeException("Could not get or create mod cache folder!");
		}

		MOD_CONTAINER = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(RuntimeException::new);
	}

	/**
	 * For use in screen mixins.
	 *
	 * @param original what the line originally said
	 * @return the string to replace it with
	 */
	public static String versionString(final String original) {
		return "Babricated Minecraft 1.7.3" + (original.endsWith(" (") ? " (" : "");
	}

	/**
	 * For use in screen mixins.
	 *
	 * @return a string telling how many mods are loaded from ModLoader
	 */
	public static String rmlModsLoaded() {
		return rmlModCount + " RML mods";
	}

	public static String fabricModsLoaded() {
		return fabricModCount + " Fabric mods";
	}

	public static String getRemappedFieldName(Class<?> instanceClass, String name) {
		for (ClassDef classDef : FabricLauncherBase.getLauncher().getMappingConfiguration().getMappings().getClasses()) {
			if (classDef.getName(FabricLoader.getInstance().getMappingResolver().getCurrentRuntimeNamespace())
					.replace(".", "/").equals(instanceClass.getName().replace(".", "/"))) {
				for (FieldDef fieldDef : classDef.getFields()) {
					if (Objects.equals(fieldDef.getName(FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT ? "client" : "server"), name)) {
						return fieldDef.getName(FabricLoader.getInstance().getMappingResolver().getCurrentRuntimeNamespace());
					}
				}
			}
		}

		return name;
	}

	public static Biome[] getStandardBiomes() {
		List<Biome> biomes = new ArrayList<>();

		// Vanilla Biomes
		biomes.add(Biome.RAINFOREST);
		biomes.add(Biome.SWAMPLAND);
		biomes.add(Biome.SEASONAL_FOREST);
		biomes.add(Biome.FOREST);
		biomes.add(Biome.SAVANNA);
		biomes.add(Biome.SHRUBLAND);
		biomes.add(Biome.TAIGA);
		biomes.add(Biome.DESERT);
		biomes.add(Biome.PLAINS);
		biomes.add(Biome.ICE_DESERT);
		biomes.add(Biome.TUNDRA);

		return biomes.toArray(new Biome[0]);
	}
}
