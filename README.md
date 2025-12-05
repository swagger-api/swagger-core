# Swagger Core <img src="https://raw.githubusercontent.com/swagger-api/swagger.io/wordpress/images/assets/SW-logo-clr.png" height="50" align="right">

**NOTE:** If you're looking for Swagger Core 1.5.X and OpenAPI 2.0, please refer to [1.5 branch](https://github.com/swagger-api/swagger-core/tree/1.5).

**NOTE:** Since version 2.1.7, Swagger Core also supports the Jakarta namespace. There are a parallel set of artifacts with the `-jakarta` suffix, providing the same functionality as the unsuffixed (i.e.: `javax`) artifacts.
Please see the [Wiki](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Getting-started) for more details.

**NOTE:** Since version 2.2.0 Swagger Core supports OpenAPI 3.1; see [this page](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---OpenAPI-3.1) for details

![Build Test Deploy](https://github.com/swagger-api/swagger-core/workflows/Build%20Test%20Deploy%20master/badge.svg?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.swagger.core.v3/swagger-project/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/io.swagger.core.v3/swagger-project)

Swagger Core is a Java implementation of the OpenAPI Specification. Current version supports *JAX-RS2* (`javax` and `jakarta` namespaces).

## Get started with Swagger Core!
See the guide on [getting started with Swagger Core](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Getting-started) to get started with adding Swagger to your API.

## See the Wiki!
The [github wiki](https://github.com/swagger-api/swagger-core/wiki) contains documentation, samples, contributions, etc. Start there.

## Compatibility
The OpenAPI Specification has undergone several revisions since initial creation in 2010.  The Swagger Core project has the following compatibilities with the OpenAPI Specification:

Swagger core Version      | Release Date | OpenAPI Spec compatibility | Notes | Status
------------------------- | ------------ | -------------------------- | ----- | ----
2.2.41 (**current stable**)| 2025-11-24   | 3.x           | [tag v2.2.41](https://github.com/swagger-api/swagger-core/tree/v2.2.41) | Supported
2.2.40                     | 2025-10-28   | 3.x           | [tag v2.2.40](https://github.com/swagger-api/swagger-core/tree/v2.2.40) | Supported
2.2.39                     | 2025-10-13   | 3.x           | [tag v2.2.39](https://github.com/swagger-api/swagger-core/tree/v2.2.39) | Supported
2.2.38                     | 2025-09-29   | 3.x           | [tag v2.2.38](https://github.com/swagger-api/swagger-core/tree/v2.2.38) | Supported
2.2.37                     | 2025-09-16   | 3.x           | [tag v2.2.37](https://github.com/swagger-api/swagger-core/tree/v2.2.37) | Supported
2.2.36                     | 2025-08-18   | 3.x           | [tag v2.2.36](https://github.com/swagger-api/swagger-core/tree/v2.2.36) | Supported
2.2.35                     | 2025-07-31   | 3.x           | [tag v2.2.35](https://github.com/swagger-api/swagger-core/tree/v2.2.35) | Supported
2.2.34                     | 2025-06-20   | 3.x           | [tag v2.2.34](https://github.com/swagger-api/swagger-core/tree/v2.2.34) | Supported
2.2.33                     | 2025-06-12   | 3.x           | [tag v2.2.33](https://github.com/swagger-api/swagger-core/tree/v2.2.33) | Supported
2.2.32                     | 2025-05-14   | 3.x           | [tag v2.2.32](https://github.com/swagger-api/swagger-core/tree/v2.2.32) | Supported
2.2.31                     | 2025-05-13   | 3.x           | [tag v2.2.31](https://github.com/swagger-api/swagger-core/tree/v2.2.31) | Supported
2.2.30                     | 2025-04-07   | 3.x           | [tag v2.2.30](https://github.com/swagger-api/swagger-core/tree/v2.2.30) | Supported
2.2.29                     | 2025-03-10   | 3.x           | [tag v2.2.29](https://github.com/swagger-api/swagger-core/tree/v2.2.29) | Supported
2.2.28                     | 2025-01-16   | 3.x           | [tag v2.2.28](https://github.com/swagger-api/swagger-core/tree/v2.2.28) | Supported
2.2.27                     | 2024-12-11   | 3.x           | [tag v2.2.27](https://github.com/swagger-api/swagger-core/tree/v2.2.27) | Supported
2.2.26                     | 2024-11-18   | 3.x           | [tag v2.2.26](https://github.com/swagger-api/swagger-core/tree/v2.2.26) | Supported
2.2.25                     | 2024-10-02   | 3.x           | [tag v2.2.25](https://github.com/swagger-api/swagger-core/tree/v2.2.25) | Supported
2.2.24                     | 2024-09-23   | 3.x           | [tag v2.2.24](https://github.com/swagger-api/swagger-core/tree/v2.2.24) | Supported
2.2.23                     | 2024-08-28   | 3.x           | [tag v2.2.23](https://github.com/swagger-api/swagger-core/tree/v2.2.23) | Supported
2.2.22                     | 2024-05-15   | 3.x           | [tag v2.2.22](https://github.com/swagger-api/swagger-core/tree/v2.2.22) | Supported
2.2.21                     | 2024-03-20   | 3.x           | [tag v2.2.21](https://github.com/swagger-api/swagger-core/tree/v2.2.21) | Supported
2.2.20                     | 2023-12-19   | 3.x           | [tag v2.2.20](https://github.com/swagger-api/swagger-core/tree/v2.2.20) | Supported
2.2.19                     | 2023-11-10   | 3.x           | [tag v2.2.19](https://github.com/swagger-api/swagger-core/tree/v2.2.19) | Supported
2.2.18                     | 2023-10-25   | 3.x           | [tag v2.2.18](https://github.com/swagger-api/swagger-core/tree/v2.2.18) | Supported
2.2.17                     | 2023-10-12   | 3.x           | [tag v2.2.17](https://github.com/swagger-api/swagger-core/tree/v2.2.17) | Supported
2.2.16                     | 2023-09-18   | 3.x           | [tag v2.2.16](https://github.com/swagger-api/swagger-core/tree/v2.2.16) | Supported
2.2.15                     | 2023-07-08   | 3.x           | [tag v2.2.15](https://github.com/swagger-api/swagger-core/tree/v2.2.15) | Supported
2.2.14                     | 2023-06-26   | 3.x           | [tag v2.2.14](https://github.com/swagger-api/swagger-core/tree/v2.2.14) | Supported
2.2.13                     | 2023-06-24   | 3.x           | [tag v2.2.13](https://github.com/swagger-api/swagger-core/tree/v2.2.13) | Supported
2.2.12                     | 2023-06-13   | 3.x           | [tag v2.2.12](https://github.com/swagger-api/swagger-core/tree/v2.2.12) | Supported
2.2.11                     | 2023-06-01   | 3.x           | [tag v2.2.11](https://github.com/swagger-api/swagger-core/tree/v2.2.11) | Supported
2.2.10                     | 2023-05-15   | 3.x           | [tag v2.2.10](https://github.com/swagger-api/swagger-core/tree/v2.2.10) | Supported
2.2.9                     | 2023-03-20  | 3.x           | [tag v2.2.9](https://github.com/swagger-api/swagger-core/tree/v2.2.9)                                             | Supported
2.2.8                     | 2023-01-06  | 3.x           | [tag v2.2.8](https://github.com/swagger-api/swagger-core/tree/v2.2.8)                                             | Supported
2.2.7                     | 2022-11-15  | 3.0           | [tag v2.2.7](https://github.com/swagger-api/swagger-core/tree/v2.2.7)                                             | Supported
2.2.6                     | 2022-11-02  | 3.0           | [tag v2.2.6](https://github.com/swagger-api/swagger-core/tree/v2.2.6)                                             | Supported
2.2.5                     | 2022-11-02  | 3.0           | [tag v2.2.5](https://github.com/swagger-api/swagger-core/tree/v2.2.5)                                             | Supported
2.2.4                     | 2022-10-16  | 3.0           | [tag v2.2.4](https://github.com/swagger-api/swagger-core/tree/v2.2.4)                                             | Supported
2.2.3                     | 2022-09-27  | 3.0           | [tag v2.2.3](https://github.com/swagger-api/swagger-core/tree/v2.2.3)                                             | Supported
2.2.2                     | 2022-07-20  | 3.0           | [tag v2.2.2](https://github.com/swagger-api/swagger-core/tree/v2.2.2)                                             | Supported
2.2.1                     | 2022-06-15  | 3.0           | [tag v2.2.1](https://github.com/swagger-api/swagger-core/tree/v2.2.1)                                             | Supported
2.2.0                     | 2022-04-04  | 3.0           | [tag v2.2.0](https://github.com/swagger-api/swagger-core/tree/v2.2.0)                                             | Supported
2.1.13                     | 2022-02-07  | 3.0           | [tag v2.1.13](https://github.com/swagger-api/swagger-core/tree/v2.1.13)                                           | Supported
2.1.12                     | 2021-12-23  | 3.0           | [tag v2.1.12](https://github.com/swagger-api/swagger-core/tree/v2.1.12)                                           | Supported
2.1.11                     | 2021-09-29  | 3.0           | [tag v2.1.11](https://github.com/swagger-api/swagger-core/tree/v2.1.11)                                           | Supported
2.1.10                     | 2021-06-28  | 3.0           | [tag v2.1.10](https://github.com/swagger-api/swagger-core/tree/v2.1.10)                                           | Supported
2.1.9                     | 2021-04-20  | 3.0           | [tag v2.1.9](https://github.com/swagger-api/swagger-core/tree/v2.1.9)                                             | Supported
2.1.8                     | 2021-04-18  | 3.0           | [tag v2.1.8](https://github.com/swagger-api/swagger-core/tree/v2.1.8)                                             | Supported
2.1.7                     | 2021-02-18  | 3.0           | [tag v2.1.7](https://github.com/swagger-api/swagger-core/tree/v2.1.7)                                             | Supported
2.1.6                     | 2020-12-04  | 3.0           | [tag v2.1.6](https://github.com/swagger-api/swagger-core/tree/v2.1.6)                                             | Supported
2.1.5                     | 2020-10-01  | 3.0           | [tag v2.1.5](https://github.com/swagger-api/swagger-core/tree/v2.1.5)                                             | Supported
2.1.4                     | 2020-07-24  | 3.0           | [tag v2.1.4](https://github.com/swagger-api/swagger-core/tree/v2.1.4)                                             | Supported
2.1.3                     | 2020-06-27  | 3.0           | [tag v2.1.3](https://github.com/swagger-api/swagger-core/tree/v2.1.3)                                             | Supported
2.1.2                     | 2020-04-01  | 3.0           | [tag v2.1.2](https://github.com/swagger-api/swagger-core/tree/v2.1.2)                                             | Supported
2.1.1                     | 2020-01-02  | 3.0           | [tag v2.1.1](https://github.com/swagger-api/swagger-core/tree/v2.1.1)                                             | Supported
2.1.0                     | 2019-11-16  | 3.0           | [tag v2.1.0](https://github.com/swagger-api/swagger-core/tree/v2.1.0)                                             | Supported
2.0.10                    | 2019-10-11  | 3.0           | [tag v2.0.10](https://github.com/swagger-api/swagger-core/tree/v2.0.10)                                           | Supported
2.0.9                     | 2019-08-22  | 3.0           | [tag v2.0.9](https://github.com/swagger-api/swagger-core/tree/v2.0.9)                                             | Supported
2.0.8                     | 2019-04-24  | 3.0           | [tag v2.0.8](https://github.com/swagger-api/swagger-core/tree/v2.0.8)                                             | Supported
2.0.7                     | 2019-02-18  | 3.0           | [tag v2.0.7](https://github.com/swagger-api/swagger-core/tree/v2.0.7)                                             | Supported
2.0.6                     | 2018-11-27  | 3.0           | [tag v2.0.6](https://github.com/swagger-api/swagger-core/tree/v2.0.6)                                             | Supported
2.0.5                     | 2018-09-19  | 3.0           | [tag v2.0.5](https://github.com/swagger-api/swagger-core/tree/v2.0.5)                                             | Supported
2.0.4                     | 2018-09-05  | 3.0           | [tag v2.0.4](https://github.com/swagger-api/swagger-core/tree/v2.0.4)                                             | Supported
2.0.3                     | 2018-08-09  | 3.0           | [tag v2.0.3](https://github.com/swagger-api/swagger-core/tree/v2.0.3)                                             | Supported
1.6.14 (**current stable**)| 2024-03-19   | 2.0           | [tag v1.6.14](https://github.com/swagger-api/swagger-core/tree/v1.6.14)                                           | Supported
1.6.13                    | 2024-01-26   | 2.0           | [tag v1.6.13](https://github.com/swagger-api/swagger-core/tree/v1.6.13)                                           | Supported
1.6.12                    | 2023-10-14   | 2.0           | [tag v1.6.12](https://github.com/swagger-api/swagger-core/tree/v1.6.12)                                           | Supported
1.6.11                    | 2023-05-15  | 2.0           | [tag v1.6.11](https://github.com/swagger-api/swagger-core/tree/v1.6.11)                                           | Supported
1.6.10                    | 2023-03-21  | 2.0           | [tag v1.6.10](https://github.com/swagger-api/swagger-core/tree/v1.6.10)                                           | Supported
1.6.9                     | 2022-11-15  | 2.0           | [tag v1.6.9](https://github.com/swagger-api/swagger-core/tree/v1.6.9)                                             | Supported
1.6.8                     | 2022-10-16  | 2.0           | [tag v1.6.8](https://github.com/swagger-api/swagger-core/tree/v1.6.8)                                             | Supported
1.6.7                     | 2022-09-27  | 2.0           | [tag v1.6.7](https://github.com/swagger-api/swagger-core/tree/v1.6.7)                                             | Supported
1.6.6                     | 2022-04-04  | 2.0           | [tag v1.6.6](https://github.com/swagger-api/swagger-core/tree/v1.6.6)                                             | Supported
1.6.5                     | 2022-02-07  | 2.0           | [tag v1.6.5](https://github.com/swagger-api/swagger-core/tree/v1.6.5)                                             | Supported
1.6.4                     | 2021-12-23  | 2.0           | [tag v1.6.4](https://github.com/swagger-api/swagger-core/tree/v1.6.4)                                             | Supported
1.6.3                     | 2021-09-29  | 2.0           | [tag v1.6.3](https://github.com/swagger-api/swagger-core/tree/v1.6.3)                                             | Supported
1.6.2                     | 2020-07-01  | 2.0           | [tag v1.6.2](https://github.com/swagger-api/swagger-core/tree/v1.6.2)                                             | Supported
1.6.1                     | 2020-04-01  | 2.0           | [tag v1.6.1](https://github.com/swagger-api/swagger-core/tree/v1.6.1)                                             | Supported
1.6.0                     | 2019-11-16  | 2.0           | [tag v1.6.0](https://github.com/swagger-api/swagger-core/tree/v1.6.0)                                             | Supported
1.5.24                    | 2019-10-11  | 2.0           | [tag v1.5.24](https://github.com/swagger-api/swagger-core/tree/v1.5.24)                                           | Supported
1.5.23                    | 2019-08-22  | 2.0           | [tag v1.5.23](https://github.com/swagger-api/swagger-core/tree/v1.5.23)                                           | Supported
1.5.22                    | 2019-02-18  | 2.0           | [tag v1.5.22](https://github.com/swagger-api/swagger-core/tree/v1.5.22)                                           | Supported
1.5.21                    | 2018-08-09  | 2.0           | [tag v1.5.21](https://github.com/swagger-api/swagger-core/tree/v1.5.21)                                           | Supported
1.5.20                    | 2018-05-23  | 2.0           | [tag v1.5.20](https://github.com/swagger-api/swagger-core/tree/v1.5.20)                                           | Supported
2.0.2                     | 2018-05-23  | 3.0           | [tag v2.0.2](https://github.com/swagger-api/swagger-core/tree/v2.0.2)                                             | Supported
2.0.1                     | 2018-04-16  | 3.0           | [tag v2.0.1](https://github.com/swagger-api/swagger-core/tree/v2.0.1)                                             | Supported
1.5.19                    | 2018-04-16  | 2.0           | [tag v1.5.19](https://github.com/swagger-api/swagger-core/tree/v1.5.19)                                           | Supported
2.0.0                     | 2018-03-20  | 3.0           | [tag v2.0.0](https://github.com/swagger-api/swagger-core/tree/v2.0.0)                                             | Supported
2.0.0-rc4                 | 2018-01-22  | 3.0           | [tag v2.0.0-rc4](https://github.com/swagger-api/swagger-core/tree/v2.0.0-rc4)                                     | Supported
2.0.0-rc3                 | 2017-11-21  | 3.0           | [tag v2.0.0-rc3](https://github.com/swagger-api/swagger-core/tree/v2.0.0-rc3)                                     | Supported
2.0.0-rc2                 | 2017-09-29  | 3.0           | [tag v2.0.0-rc2](https://github.com/swagger-api/swagger-core/tree/v2.0.0-rc2)                                     | Supported
2.0.0-rc1                 | 2017-08-17  | 3.0           | [tag v2.0.0-rc1](https://github.com/swagger-api/swagger-core/tree/v2.0.0-rc1)                                     | Supported
1.5.18                    | 2018-01-22  | 2.0           | [tag v1.5.18](https://github.com/swagger-api/swagger-core/tree/v1.5.18)                                           | Supported
1.5.17                    | 2017-11-21  | 2.0           | [tag v1.5.17](https://github.com/swagger-api/swagger-core/tree/v1.5.17)                                           | Supported
1.5.16                    | 2017-07-15  | 2.0           | [tag v1.5.16](https://github.com/swagger-api/swagger-core/tree/v1.5.16)                                           | Supported
1.3.12                    | 2014-12-23  | 1.2           | [tag v1.3.12](https://github.com/swagger-api/swagger-core/tree/v1.3.12)                                           | Supported
1.2.4                     | 2013-06-19  | 1.1           | [tag swagger-project_2.10.0-1.2.4](https://github.com/swagger-api/swagger-core/tree/swagger-project_2.10.0-1.2.4) | Deprecated
1.0.0                     | 2011-10-16  | 1.0           | [tag v1.0](https://github.com/swagger-api/swagger-core/tree/v1.0)                                                 | Deprecated


### Change History
If you're interested in the change history of swagger and the Swagger Core framework, see [here](https://github.com/swagger-api/swagger-core/releases).

### Prerequisites
You need the following installed and available in your $PATH:

* Java 11
* Apache maven 3.0.4 or greater
* Jackson 2.4.5 or greater


### To build from source (currently 2.2.42-SNAPSHOT)
```
# first time building locally
mvn -N
```

Subsequent builds:
```
mvn install
```

This will build the modules.

Of course if you don't want to build locally you can grab artifacts from maven central:

`https://repo1.maven.org/maven2/io/swagger/core/`

## Sample Apps
The samples have moved to [a new repository](https://github.com/swagger-api/swagger-samples/tree/2.0) and contain various integrations and configurations.

## Security contact

Please disclose any security-related issues or vulnerabilities by emailing [security@swagger.io](mailto:security@swagger.io), instead of using the public issue tracker.
