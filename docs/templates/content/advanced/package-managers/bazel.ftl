# Bazel support

As of ${solution_name} versions 5.2.0 and higher, ${solution_name} provides limited support for Bazel projects.

As of ${solution_name} version 5.2.0, ${solution_name} supports dependencies specified in *maven_jar* workspace rules.
As of ${solution_name} version 6.0.0, ${solution_name} also supports dependencies specified in *maven_install* workspace rules.

The Bazel tool attempts to run on your project if you provide a Bazel build target using the Bazel target property.

The Bazel tool also requires a bazel executable on $PATH.

${solution_name} attempts to determine the workspace dependency rule (*maven_jar* or *maven_install*) from the WORKSPACE file.
In case it cannot, you can specify which rule you use with the Bazel dependency type property.
Refer to [Properties](../../../properties/detectors/bazel/) for details.

## Processing for the *maven_install* workspace rule

The Bazel tool runs a bazel cquery on the given target to produce output from which it can parse artifact details such as group, artifact, and version for dependencies.

The following is an example using commands similar to those that ${solution_name} runs, but from the command line of how ${solution_name}'s Bazel detector currently identifies components.
```
$ bazel cquery --noimplicit_deps 'kind(j.*import, deps(//tests/integration:ArtifactExclusionsTest))' --output build 2>&1 | grep maven_coordinates
tags = ["maven_coordinates=com.google.guava:guava:27.0-jre"],
tags = ["maven_coordinates=org.hamcrest:hamcrest:2.1"],
tags = ["maven_coordinates=org.hamcrest:hamcrest-core:2.1"],
tags = ["maven_coordinates=com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava"],
tags = ["maven_coordinates=org.checkerframework:checker-qual:2.5.2"],
tags = ["maven_coordinates=com.google.guava:failureaccess:1.0"],
tags = ["maven_coordinates=com.google.errorprone:error_prone_annotations:2.2.0"],
tags = ["maven_coordinates=com.google.code.findbugs:jsr305:3.0.2"],
```

Then, it parses the group/artifact/version details from the values of the maven_coordinates tags.

## Processing for the *maven_jar* workspace rule

The Bazel tool runs a bazel query on the given target to get a list of jar dependencies. On each jar dependency, the Bazel tool runs another bazel query to get its artifact details: group, artifact, and version.

The following is an example using the equivalent commands that ${solution_name} runs, but from the command line of how ${solution_name}'s Bazel detector currently identifies components.
First, it gets a list of dependencies:
```
$ bazel query 'filter("@.*:jar", deps(//:ProjectRunner))'
INFO: Invocation ID: dfe8718d-b4db-4bd9-b9b9-57842cca3fb4
@org_apache_commons_commons_io//jar:jar
@com_google_guava_guava//jar:jar
Loading: 0 packages loaded
```
Then, it gets details for each dependency. It prepends //external: to the dependency name for this command.
```
$ bazel query 'kind(maven_jar, //external:org_apache_commons_commons_io)' --output xml
INFO: Invocation ID: 0a320967-b2a8-4b36-ab47-e183bc4d4781
<?xml version="1.1" encoding="UTF-8" standalone="no"?>
<query version="2">
    <rule class="maven_jar" location="/root/home/steve/examples/java-tutorial/WORKSPACE:6:1" name="//external:org_apache_commons_commons_io">
        <string name="name" value="org_apache_commons_commons_io"/>
        <string name="artifact" value="org.apache.commons:commons-io:1.3.2"/>
    </rule>
</query>
Loading: 0 packages loaded
```
Finally, it parses the group/artifact/version details from the value of the string element using the name of artifact.
