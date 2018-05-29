task<Jar>("WinPatch") {
    classifier = "win-patch"

    doFirst {
        manifest {
            val patchedClassPath = with(configurations) {
                listOf(
                        this["runtime"],
                        this["testRuntime"],
                        this["junitPlatform"],
                        this["compile"]
                )
                        .flatMap { it.files }
                        .map { project.uri(it) }
                        .joinToString(separator = " ") { it.toString() }
            }

            attributes["Class-Path"] = patchedClassPath
        }
    }

    dependsOn(configurations["runtime"])
    dependsOn(configurations["testRuntime"])
    dependsOn(configurations["junitPlatform"])
    dependsOn(configurations["compile"])
}

(tasks.find { it.name == "junitPlatformTest" } as JavaExec).apply {

    dependsOn.add(tasks.findByName("WinPatch"))

    this.doFirst {
        classpath = files(
                project.buildDir.toString() + "/classes/java/main",
                project.buildDir.toString() + "/classes/java/main-instrumented",
                project.buildDir.toString() + "/classes/java/test",
                project.buildDir.toString() + "/classes/java/test-instrumented",
                project.buildDir.toString() + "/classes/kotlin/main",
                project.buildDir.toString() + "/classes/kotlin/test",
                project.buildDir.toString() + "/resources/main",
                project.buildDir.toString() + "/resources/test",
                run {
                    val winPatch = tasks.withType<Jar>().toList().find { it.name == "WinPatch" }
                    winPatch?.archivePath
                }
        )
    }

}
