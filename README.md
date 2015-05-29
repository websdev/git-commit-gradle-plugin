# Git Commit Gradle Plugin

A simple Gradle plugin to generate a file named "git.properties" containing the
git commit & branch information used for the build.  This file can be read by
service  containers & providers (i.e. spring-boot) to generate runtime information
describing what code source generated this specific build

Heavily inspired by: http://java.dzone.com/articles/spring-boots-info-endpoint-git

## Usage

Include in your buildscript:

```groovy
buildscript {
	dependencies {
		classpath 'com.webs.gradle:git-commit-gradle-plugin:1.0-SNAPSHOT'
	}
}
```

Then apply the plugin:

```groovy
apply plugin: 'com.webs.generate-git-info'
```

(Note: this artifact is published to the Webs Nexus instance, so you will need
to include that repository in your ```repositories``` section)

## Updates

So you want to publish?  Make your edits, submit your PR, then publish as
appropriate:

For snapshot releases:

```
gradle publishMavenJavaPublicationToWebsNexus-SnapshotsRepository
```

Once everything is kosher, publish a new release:

```
gradle publishMavenJavaPublicationToWebsNexus-ReleasesRepository
```
