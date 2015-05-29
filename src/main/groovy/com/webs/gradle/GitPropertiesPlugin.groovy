package com.webs.gradle

import org.ajoberstar.grgit.Grgit
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.TaskAction
import org.joda.time.DateTimeZone
import org.joda.time.format.ISODateTimeFormat

class GitPropertiesPlugin implements Plugin<Project> {
	@Override
	void apply(Project project) {
		def task = project.tasks.create('gitProperties', GitPropertiesTask)
		task.setGroup(BasePlugin.BUILD_GROUP)
		ensureTaskRunsOnJavaClassesTask(project, task)
	}

	private void ensureTaskRunsOnJavaClassesTask(Project project, Task task) {
		project.getTasks().getByName(JavaPlugin.CLASSES_TASK_NAME).dependsOn(task)
	}

	static class GitPropertiesTask extends DefaultTask {
		@TaskAction
		void generate() {
			// note: deprecated, but the examples still use this syntax.  Not sure what to do here?
			def repo = Grgit.open(project.file('.'))
			def dir = new File(project.buildDir, "resources/main")
			if (!dir.exists()) dir.mkdirs()

			def dateTimeFormat = ISODateTimeFormat.basicDateTimeNoMillis().withZone DateTimeZone.UTC

			new File(dir, "git.properties").withWriter { w ->
				def props = new Properties()
				props.putAll([
						"git.branch"				: repo.branch.current.name,
						"git.commit.id"				: repo.head().id,
						"git.commit.id.abbrev"		: repo.head().abbreviatedId,
						"git.commit.user.name"		: repo.head().author.name,
						"git.commit.user.email"		: repo.head().author.email,
						"git.commit.message.short"	: repo.head().shortMessage,
						"git.commit.message.full"	: repo.head().fullMessage,
						"git.commit.time"			: dateTimeFormat.print(new Long(repo.head().time)*1000)
				])
				props.store w, 'Git properties (written at build time by Git-Properties-Plugin'
			}
		}
	}
}