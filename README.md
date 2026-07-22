# Gradle Plugins
## Plugins
### AWS CodeArtifacts
  * Get authorization token automatically
  * Support credentials provider.
#### Usage
```
plugins {
  id("kim.jade.gradle.plugin.aws-codeartifact") version "<version>"
}

repositories {
  codeArtifact("https://[code artifact url]", "profileName") //profileName is optional
  
  maven("https://[domain].d.codeartifact.[region].amazonaws.com/maven/[repository]/") {
    credentials.username = [profile name] //optional
  }
  
  maven("https://[custom code artifact url]#isCodeArtifact") {
    credentials.username = [profile name] //optional
  }
}
```

#### How to set the default profile as globally
```
//~/.zshrc
export CODEARTIFACT_PROFILE=[profile name]

// or

// ~/.gradle/gradle.properties
codeArtifact.profile=[profile name]
```

## License
This project is licensed under the [Apache License 2.0](LICENSE).