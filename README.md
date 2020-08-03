# Mosaik FrameWork [![Build Status](https://maven-badges.herokuapp.com/maven-central/io.github.splotycode.mosaik/Mosaik-Framework/badge.svg?style=flat-square)](https://maven-badges.herokuapp.com/maven-central/io.github.splotycode.mosaik/Mosaik-Framework) [![License](https://img.shields.io/badge/License-EPL%202.0-blue.svg?style=flat-square)](https://github.com/SplotyCode/Mosaik-Framework/blob/master/LICENSE)
| Branch        | Status        | Version | Download |
| ------------- | ------------- | --------| ---------|
| master        | [![Build Status](https://img.shields.io/travis/SplotyCode/Mosaik-Framework/master.svg?&style=flat-square)](https://travis-ci.org/SplotyCode/Mosaik-Framework)|![Version](https://img.shields.io/nexus/r/https/oss.sonatype.org/io.github.splotycode.mosaik/Mosaik-Framework.svg?style=flat-square)|[Download releases](https://github.com/SplotyCode/Mosaik-Framework/releases)|
| develop       | [![Build Status](https://img.shields.io/travis/SplotyCode/Mosaik-Framework/develop.svg?style=flat-square)](https://travis-ci.org/SplotyCode/Mosaik-Framework)|![Version](https://img.shields.io/nexus/s/https/oss.sonatype.org/io.github.splotycode.mosaik/Mosaik-Framework.svg?style=flat-square)|[Download snapshots](https://oss.sonatype.org/content/repositories/snapshots/io.github.splotycode.mosaik/Mosaik-Framework/)|

# Description
Mosaik is a general Java Framework that makes it easy to build
applications. The APIs range from low level Resource apis to health
checks.

# Getting Started
[Maven central](https://search.maven.org/artifact/io.github.splotycode.mosaik/Mosaik-Framework) describes how to include mosaik with various build tools.
Take look at the [JavaDocs](https://splotycode.github.io/Mosaik-Framework/) or at the [Wiki](https://github.com/SplotyCode/Mosaik-Framework/wiki)<!-- @IGNORE PREVIOUS: link -->.<br>
Example Applications: [LedAnimation](https://github.com/SplotyCode/LedAnimation) and [Linky](https://github.com/SplotyCode/Linky)

# Modules
| Name              | Description                                                  | Wiki |
|:------------------|:-------------------------------------------------------------|:-----|
| AnnotationBase    | Annotation Data & Context APIs                               | -    |
| Annotations       | Common Annotations (mostly for AnnotationBase)               | -    |
| ArgParser         | Parses startup arguments into Objects                        | -    |
| Automatisation    | Crawler API and tools to generate UserAgent                  | -    |
| Console           | Input and visualization APIs for Consoles                    | -    |
| Database          | Database support based on Annotation Data API                | -    |
| DomParsing        | Document parsing based on Annotation Data API                | -    |
| Network           | CloudKit, Services, Monitoring and LoadBalancing             | -    |
| Runtime           | Core of Mosaik: Application API and StartUp                  | -    |
| Util              | Common classes & Utils for IO, caching and time              | -    |
| ValueTransformers | Transforms Objects using ConverterRoutes and ValueConverters | -    |
| WebApi            | WebHandlers using Annotation Context and Template API        | -    |

# Extensions
| Name                                                                                             | Description                                |
|:-------------------------------------------------------------------------------------------------|:-------------------------------------------|
| [GameEngine](https://github.com/SplotyCode/GameEngine)                                           | Framework to create 2d and 3d games        |
| [Mosaik-Spigot](https://github.com/SplotyCode/Mosaik-Spigot)                                     | Inventory, NMS and Command APIs for spigot |
For Library support take a look at [Mosaik-Extensions](https://github.com/SplotyCode/Mosaik-Extensions)
