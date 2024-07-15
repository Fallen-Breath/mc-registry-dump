/*
 * This file is part of the mc registry dump project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  Fallen_Breath and contributors
 *
 * mc registry dump is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mc registry dump is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with mc registry dump.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.fallenbreath.mcregistrydump;

import com.google.common.collect.Maps;
import com.google.gson.GsonBuilder;
import net.minecraft.SharedConstants;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class RegistryDumper
{
	private static final Logger LOGGER = McRegistryDumpMod.LOGGER;

	public static void dump()
	{
		Map<String, Object> json = Maps.newLinkedHashMap();

		json.put("block", dumpRegistry(Registries.BLOCK));
		json.put("block_entity_type", dumpRegistry(Registries.BLOCK_ENTITY_TYPE));
		json.put("entity_type", dumpRegistry(Registries.ENTITY_TYPE));
		json.put("item", dumpRegistry(Registries.ITEM));
		json.put("particle_type", dumpRegistry(Registries.PARTICLE_TYPE));
		json.put("screen_handler", dumpRegistry(Registries.SCREEN_HANDLER));
		json.put("status_effect", dumpRegistry(Registries.STATUS_EFFECT));

		var gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

		var outputDir = Optional.ofNullable(System.getenv("MC_REGISTRY_DUMP_OUTPUT_DIR")).orElse("output");
		var path = Path.of(outputDir, "%s.json".formatted(SharedConstants.getGameVersion().getName()));
		try
		{
			boolean ignored = path.getParent().toFile().mkdirs();
			Files.writeString(path, gson.toJson(json), StandardCharsets.UTF_8);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		LOGGER.info("Registry dump is written to {}", path);
	}

	private static <T> Map<String, Integer> dumpRegistry(Registry<T> registry)
	{
		Map<String, Integer> result = Maps.newLinkedHashMap();
		for (T item : registry)
		{
			result.put(Objects.requireNonNull(registry.getId(item)).toString(), registry.getRawId(item));
		}
		return result;
	}
}
