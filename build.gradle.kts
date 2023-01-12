plugins {
    id("java")
    id("maven-publish")
    id("java-gradle-plugin")
    id("com.gradle.plugin-publish") version "0.18.0"
}

group = "org.glavo"
version = "0.2.0"

tasks.compileJava {
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
}

tasks.javadoc {
    options.encoding = "UTF-8"
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(gradleApi())
}

configure<PublishingExtension> {
    publications {
        create<MavenPublication>("maven") {
            // project.shadow.component(this)

            groupId = project.group.toString()
            version = project.version.toString()
            artifactId = project.name

            from(components["java"])

            pom {
                name.set(project.name)
                description.set(project.description)
                url.set("https://github.com/Glavo/load-maven-publish-properties-plugin")

                licenses {
                    license {
                        name.set("Apache-2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }

                developers {
                    developer {
                        id.set("glavo")
                        name.set("Glavo")
                        email.set("zjx001202@gmail.com")
                    }
                }

                scm {
                    url.set("https://github.com/Glavo/load-maven-publish-properties-plugin")
                }
            }
        }
    }
}

description = "Load Maven Publish Properties"

pluginBundle {
    website = "https://github.com/Glavo/load-maven-publish-properties-plugin"
    vcsUrl = "https://github.com/Glavo/load-maven-publish-properties-plugin.git"
    tags = listOf("java")
}

gradlePlugin {
    plugins {
        create("loadMavenPublishPropertiesPlugin") {
            id = "org.glavo.load-maven-publish-properties"
            displayName = "Load Maven Publish Properties Plugin"
            description = rootProject.description
            implementationClass = "org.glavo.gradle.LoadMavenPublishPropertiesPlugin"
        }
    }
}