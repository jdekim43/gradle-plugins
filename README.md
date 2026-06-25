# Gradle Plugins
## Plugins
### AWS CodeArtifacts
  * Get authorization token automatically
  * Support credentials provider.
#### Usage
```
plugins {
  id("kim.jade.gradle.plugin.aws-codeartifact") version "0.1.0"
}

repositories {
  codeArtifact("https://[code artifact url]", "profileName")
}
```

## License
This project is licensed under the [Apache License 2.0](LICENSE).