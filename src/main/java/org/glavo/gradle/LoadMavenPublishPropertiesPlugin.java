package org.glavo.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.ExtraPropertiesExtension;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Properties;

/**
 * Simplify loading Maven Publish properties.
 */
public class LoadMavenPublishPropertiesPlugin implements Plugin<Project> {

    private static final String PROPERTIES_FILE_NAME = "maven-central-publish.properties";

    @Override
    public void apply(Project project) {
        Project rootProject = project.getRootProject();
        ExtraPropertiesExtension ext = (ExtraPropertiesExtension) rootProject.getExtensions().getByName("ext");

        Path secretPropsFile = rootProject.file("gradle/" + PROPERTIES_FILE_NAME).toPath();
        if (!Files.isRegularFile(secretPropsFile)) {
            secretPropsFile = Paths.get(System.getProperty("user.home"), ".gradle", PROPERTIES_FILE_NAME);
        }

        if (Files.isRegularFile(secretPropsFile)) {
            // Read local.properties file first if it exists
            Properties p = new Properties();

            try (BufferedReader reader = Files.newBufferedReader(secretPropsFile)) {
                p.load(reader);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }

            p.forEach((name, value) -> ext.set(name.toString(), value));
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("sonatypeUsername", "SONATYPE_USERNAME");
        map.put("sonatypePassword", "SONATYPE_PASSWORD");
        map.put("sonatypeStagingProfileId", "SONATYPE_STAGING_PROFILE_ID");
        map.put("signing.keyId", "SIGNING_KEY_ID");
        map.put("signing.password", "SIGNING_PASSWORD");
        map.put("signing.key", "SIGNING_KEY");


        map.forEach((p, e) -> {
            if (!ext.has(p)) {
                ext.set(p, System.getenv(e));
            }
        });

    }
}
