package me.chill.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

fun setupDatabase(databaseUrl: String) {
	Database.connect(databaseUrl, "org.postgresql.Driver")

	transaction {
		SchemaUtils.create(
			ChannelAssignment,
			Permission,
			OnJoinRole,
			Preference
		)
	}
}

object ChannelAssignment : Table() {
	val serverId = varchar("server_id", 20).primaryKey()
	val loggingChannelId = varchar("logging", 20)
	val suggestionChannelId = varchar("suggestion", 20)
	val joinChannelId = varchar("join", 20)
}

object Permission : Table() {
	val commandName = varchar("command_name", 50).primaryKey()
	val serverId = varchar("server_id", 20).primaryKey()
	val permission = varchar("permission", 20).primaryKey()
}

object OnJoinRole : Table() {
	val serverId = varchar("server_id", 20).primaryKey()
	val roleId = varchar("role_id", 20)
}

object Preference : Table() {
	val serverId = varchar("server_id", 20).primaryKey()
	val prefix = varchar("prefix", 3)
	val joinChannel = varchar("join", 20)
	val loggingChannel = varchar("logging", 20)
	val suggestionChannel = varchar("suggestion", 20)
	val raidMessageLimit = integer("raid_message_limit")
	val raidMessageDuration = integer("raid_message_duration")
	val disableWelcome = bool("disable_welcome")
	val welcomeMessage = varchar("welcome_message", 100)
	val timeMultiplier = varchar("time_multiplier", 1)
	val onJoinRole = varchar("on_join_role", 20).nullable()
}