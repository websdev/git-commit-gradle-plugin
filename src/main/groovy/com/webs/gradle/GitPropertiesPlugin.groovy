package com.webs.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.BasePlugin
import org.gradle.language.base.plugins.LifecycleBasePlugin

/**
 * The class for the Git Properties Plugin
 */
class GitPropertiesPlugin implements Plugin<Project> {

	/**
	 * Create the "gitProperties" task from the "GitPropertiesTask" class, assign it to the "Build" group, and make the
	 * default "assemble" task depend on it
	 * @param project
	 */
	@Override
	void apply(Project project) {
		Task task = project.tasks.create('gitProperties', GitPropertiesTask)
		task.setGroup(BasePlugin.BUILD_GROUP)
		project.getTasks().getByName(LifecycleBasePlugin.ASSEMBLE_TASK_NAME).dependsOn(task)
	}
}