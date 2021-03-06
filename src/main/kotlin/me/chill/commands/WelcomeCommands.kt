package me.chill.commands

import me.chill.arguments.types.Sentence
import me.chill.database.operations.disableWelcome
import me.chill.database.operations.editWelcomeMessage
import me.chill.database.operations.enableWelcome
import me.chill.database.operations.getWelcomeDisabled
import me.chill.database.states.WelcomeState
import me.chill.embed.types.newMemberJoinEmbed
import me.chill.framework.CommandCategory
import me.chill.framework.commands
import me.chill.settings.clap
import me.chill.utility.jda.failureEmbed
import me.chill.utility.jda.send
import me.chill.utility.jda.successEmbed
import me.chill.utility.str
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.MessageChannel

@CommandCategory
fun welcomeCommands() = commands("Welcome") {
	command("disablewelcome") {
		execute {
			alterWelcomeState(guild, channel, WelcomeState.Disabled)
		}
	}

	command("enablewelcome") {
		execute {
			alterWelcomeState(guild, channel, WelcomeState.Enabled)
		}
	}

	command("getwelcomeenabled") {
		execute {
			val isWelcomeDisabled = getWelcomeDisabled(guild.id)
			val welcomeState = when (isWelcomeDisabled) {
				false -> WelcomeState.Enabled
				true -> WelcomeState.Disabled
			}
			respond(
				successEmbed(
					"Welcomes",
					"Welcomes are ${welcomeState.name.toLowerCase()} in **${guild.name}**",
					clap
				)
			)
		}
	}

	command("getwelcomemessage") {
		execute {
			respond(newMemberJoinEmbed(guild, invoker))
		}
	}

	command("setwelcomemessage") {
		expects(Sentence())
		execute {
			editWelcomeMessage(guild.id, arguments[0]!!.str())
			respond(newMemberJoinEmbed(guild, invoker))
		}
	}
}

private fun alterWelcomeState(guild: Guild, channel: MessageChannel, welcomeState: WelcomeState) {
	val guildId = guild.id
	val isWelcomeDisabled = getWelcomeDisabled(guildId)
	val welcomeStateName = welcomeState.name

	if (isWelcomeDisabled != welcomeState.b) {
		when (welcomeState) {
			WelcomeState.Disabled -> disableWelcome(guildId)
			WelcomeState.Enabled -> enableWelcome(guildId)
		}
		channel.send(
			successEmbed(
				"${welcomeStateName.substring(0, welcomeStateName.length)} Welcome",
				"Welcomes have been ${welcomeStateName.toLowerCase()} for **${guild.name}**",
				clap
			)
		)
	} else {
		channel.send(
			failureEmbed(
				"${welcomeStateName.substring(0, welcomeStateName.length)} Welcome",
				"Welcomes are already ${welcomeStateName.toLowerCase()} for **${guild.name}**"
			)
		)
	}
}