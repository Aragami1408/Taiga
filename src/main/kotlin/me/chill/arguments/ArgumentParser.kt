package me.chill.arguments

import me.chill.framework.Command
import net.dv8tion.jda.core.entities.Guild

fun parseArguments(command: Command, guild: Guild, args: Array<String>): ParseMap {
	val expectedArgs = command.argumentTypes
	val parseMap = ParseMap()

	val zipped = expectedArgs.zip(args)
	for (pair in zipped) {
		val result = pair.first.check(guild, pair.second)
		val status = result.status
		val parsedValue = result.parsedValue

		parseMap.parsedValues.add(parsedValue)

		if (!status) {
			parseMap.status = status
			parseMap.errMsg = result.errMsg
			return parseMap
		}
	}

	return parseMap
}
