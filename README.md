# Gradle Plugins
## Plugins
### AWS CodeArtifacts
  * Get authorization token automatically
  * Support credentials provider.
#### Usage
```
plugins {
  id("kim.jade.gradle.plugin.aws-codeartifact") version "0.1.10"
}

repositories {
  codeArtifact("https://[code artifact url]", "profileName") //profileName is optional
  
  maven("https://[domain].d.codeartifact.[region].amazonaws.com/maven/[repository]/")
  
  maven("https://[custom code artifact url]") {
    credentials.username = "##code_artifact##"
    credentials.password = [profile name] //optional
    
    // or
    
    useCodeArtifactCredentials()
}
```

## License
This project is licensed under the [Apache License 2.0](LICENSE).