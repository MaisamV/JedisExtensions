# JedisExtensions
This library provided functions to make working with Jedis easier

# Publish
In order to publish this library on mavenLocal follow instructions
1. ```git clone https://github.com/MaisamV/JedisExtensions.git```
2. ```cd ./JedisExtensions```
3. ```./gradlew clean build```
4. ```./gradlew install```

Now you can use this library in your projects like this:

```
repositories {
    mavenLocal()
    mavenCentral()
    // other repositories ...
}
```
```
dependencies {
    implementation "com.mvs:JedisExtensions:1.0.1"
    implementation "redis.clients:jedis:3.6.3"
    implementation "org.jetbrains.kotlin:kotlin-stdlib"
    // other libraries ...
}
```


In order to publish this library on private maven repository follow instructions
1. Set environmental variables ```artifactory_url```, ```artifactory_username``` and ```artifactory_password```
2. Set repository name in gradle.properties as name of your repository ```repositoryKey=YourRepoName```
3. Open a NEW terminal in project directory, so the terminal will pick updated environmental variables.
4. Update library version in build.gradle
5. ```./gradlew clean build```
6. ```./gradlew artifactoryPublish```

done.