package com.webs.gradle

import org.ajoberstar.grgit.Grgit
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.ISODateTimeFormat

/**
 * The class that encapsulates the behavior to build the "git.properties" file
 */
class GitPropertiesTask extends DefaultTask {

	@TaskAction
	void generate() {
		// note: deprecated, but the examples still use this syntax.  Not sure what to do here?
		Grgit repo = Grgit.open(project.file('.'))
		File dir = new File(project.buildDir, "resources/main")
		if (!dir.exists()) dir.mkdirs()

		DateTimeFormatter dateTimeFormat = ISODateTimeFormat.basicDateTimeNoMillis().withZone DateTimeZone.UTC

		// TODO figure out git describe
		new File(dir, "git.properties").withWriter { w ->
			def head = repo.head()
			def props = new Properties()
			props.putAll([
					"git.branch"                : repo.branch.current.name,
					"git.tags"                  : repo.tag.list()?.grep({ it.commit.id == head.id }).collect({ it.fullName }).join(', '),
					"git.commit.id"             : head.id,
					"git.commit.id.abbrev"      : head.abbreviatedId,
					"git.commit.id.describ"     : repo.describe() ?: '',
					"git.commit.user.name"      : head.author.name ?: '',
					"git.commit.user.email"     : head.author.email ?: '',
					"git.commit.message.short"  : head.shortMessage ?: '',
					"git.commit.message.full"   : head.fullMessage ?: '',
					"git.commit.time"           : dateTimeFormat.print(new Long(head.time)*1000)
			])

			props.store w, 'Git properties (written at build time by Git-Properties-Plugin'
		}
	}
}