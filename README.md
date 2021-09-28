# JedisExtensions
This library provided functions to make working with Jedis easier

# Build
in order to use this library in other projects
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