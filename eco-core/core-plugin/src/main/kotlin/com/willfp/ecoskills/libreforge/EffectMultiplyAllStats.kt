package com.willfp.ecoskills.libreforge

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.ecoskills.api.addStatModifier
import com.willfp.ecoskills.api.modifiers.ModifierOperation
import com.willfp.ecoskills.api.modifiers.StatModifier
import com.willfp.ecoskills.api.removeStatModifier
import com.willfp.ecoskills.stats.Stats
import com.willfp.libreforge.NoCompileData
import com.willfp.libreforge.ProvidedHolder
import com.willfp.libreforge.arguments
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.effects.Identifiers
import org.bukkit.entity.Player

object EffectMultiplyAllStats : Effect<NoCompileData>("multiply_all_stats") {
    override val arguments = arguments {
        require("multiplier", "You must specify the multiplier!")
    }

    override fun onEnable(
        player: Player,
        config: Config,
        identifiers: Identifiers,
        holder: ProvidedHolder,
        compileData: NoCompileData
    ) {
        val factory = identifiers.makeFactory()

        for ((offset, stat) in Stats.values().withIndex()) {
            player.addStatModifier(
                StatModifier(
                    factory.makeIdentifiers(offset).uuid,
                    stat,
                    config.getDoubleFromExpression("multiplier", player),
                    ModifierOperation.MULTIPLY
                )
            )
        }
    }

    override fun onDisable(player: Player, identifiers: Identifiers, holder: ProvidedHolder) {
        val factory = identifiers.makeFactory()

        for (offset in Stats.values().indices) {
            player.removeStatModifier(factory.makeIdentifiers(offset).uuid)
        }
    }
}
