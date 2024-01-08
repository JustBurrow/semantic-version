# Version

[Semantic Versioning 2.0.0](https://semver.org)을 지원하는 KMP 라이브러리.

## Dev Env.

- [Get started with Kotlin Multiplatform — tutorial](https://www.jetbrains.com/help/kotlin-multiplatform-dev/multiplatform-getting-started.html)
  을 따라 환경 설정.
- JDK 17을 사용.
- IDE는 Android Studio를 사용.
- 로컬 레포지토리 경로는 `~/Repositories/github.com/JustBurrow/version`으로 가정함.

### macOS

[macOS 개발 환경 설정](https://gist.github.com/JustBurrow/970c06306f790fb9bf195e3e37d5799d) 참조.

- [Homebrew](https://brew.sh) 설치.
- JDK는 `jenv`로 관라한다.

```shell
# 필요한 명령만 실행.
brew update
brew install jenv openjdk@17
jenv add /opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home/

mkdir -p ~/Repositories/github.com/JustBurrow
cd ~/Repositories/github.com/JustBurrow
git clone git@github.com:JustBurrow/version.git

jenv local 17.0
```

### Windows

TBD

### Android Studio

- Plugin 설치.
    - [.ignore](https://plugins.jetbrains.com/plugin/7495--ignore)
    - [GitToolBox](https://plugins.jetbrains.com/plugin/7499-gittoolbox)
    - [Kotlin Multiplatform Mobile](https://plugins.jetbrains.com/plugin/14936-kotlin-multiplatform-mobile)
- Gradle 설정.
  ![Android Studio Gradle](doc/file/android%20studio%20gradle.png)
- Git 설정.
  ![Android Studio Git](doc/file/android%20studio%20git.png)
- GitToolBox 설정
  ![Android Studio GitToolBox](doc/file/android%20studio%20gittoolbox.png)
