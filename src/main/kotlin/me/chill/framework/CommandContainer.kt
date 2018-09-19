package me.chill.framework

import org.reflections.Reflections
import org.reflections.scanners.MethodAnnotationsScanner

class CommandContainer private constructor() {
	init {
		val reflections = Reflections("me.chill.commands", MethodAnnotationsScanner())
		reflections
				.getMethodsAnnotatedWith(CommandCategory::class.java)
				.forEach { it.invoke(null) }
	}

	companion object {
		val commandSets = mutableListOf<CommandSet>()

		fun loadContainer() {
			CommandContainer()
		}

		fun hasCommand(command: String) = commandSets.stream().anyMatch { it.hasCommand(command) }

		fun hasCategory(category: String) = commandSets.stream().anyMatch { it.categoryName == category }

		fun getSet(category: String) = commandSets.stream().filter { it.categoryName == category }.toArray()[0]!! as CommandSet

		fun getCommand(command: String) = createCommandList().filter { it.name == command }.toTypedArray()

		fun getCommandNames() = createCommandList().map { it.name }.toTypedArray()

		private fun createCommandList() = commandSets.map { it.commands }.flatten()
	}
}

inline fun commands(categoryName: String, create: CommandSet.() -> Unit) {
	val set = CommandSet(categoryName)
	set.create()
	CommandContainer.commandSets.add(set)
}