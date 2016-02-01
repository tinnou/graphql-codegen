graphql-codegen
===============

graphql-codegen generates code given a GraphQL Schema.
(Only Java is supported for now)

Install:
--------

Clone the repo and build the jar: `./gradlew shadowJar` or download the latest jar under the [Releases](https://github.com/tinnou/graphql-codegen/releases) tab.

Usage:
------

To generate the code for the [Star Wars](http://graphql-swapi.parseapp.com/) sample:

```
java -jar graphql-codegen.jar generate java -u http://graphql-swapi.parseapp.com/graphiql --package com.disney.starwars
```


You can specify a number of options, all described with the `help generate java` command:

```
java -jar graphql-codegen.jar help generate java 
```

outputs: 

```
NAME
        graphql-codegen generate java - Generate code for java

SYNOPSIS
        graphql-codegen generate java
                [(-o <output directory> | --output <output directory>)]
                [--override <imports override>...] [--package <java package>]
                [--password <Basic Auth password>]
                (-u <GraphQL server url> | --url <GraphQL server url>)
                [--username <Basic Auth username>]

OPTIONS
        -o <output directory>, --output <output directory>
            Where to write the generated files (default: .)

        --override <imports override>
            For custom GraphQL scalars of for external libraries such as joda
            you can specify your own import for a given type. The format must be
            type::package Package wildcard is allowed. e.g:
            DateTime::org.joda.time.DateTime or
            MyCustomList::com.my.MyCustomList or e.g: DateTime::org.joda.time.*

        --package <java package>
            Classes output package (default: my.custom.package)

        -u <GraphQL server url>, --url <GraphQL server url>
            Fully qualified GraphQL server url

        --username <Basic Auth username>
            Basic Auth username
            
        --password <Basic Auth password>
            Basic Auth password
```

### License

graphql-codegen is licensed under the MIT License. See [LICENSE](LICENSE.md) for details.

Copyright (c) 2016, Antoine Boyer and [Contributors](https://github.com/tinnou/graphql-codegen/graphs/contributors)

[graphql-js License](https://github.com/graphql/graphql-js/blob/master/LICENSE)
