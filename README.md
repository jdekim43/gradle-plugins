# Gradle Plugins
## Plugins
### AWS CodeArtifacts
  * Get authorization token automatically
  * Support credentials provider.
#### Usage
```
plugins {
  id("kim.jade.gradle.plugin.aws-codeartifact") version "0.1.2"
}

repositories {
  codeArtifact("https://[code artifact url]", "profileName")
  
  // or
  
  maven("https://[code artifact url]")
}
```

## License
This project is licensed under the [Apache License 2.0](LICENSE).