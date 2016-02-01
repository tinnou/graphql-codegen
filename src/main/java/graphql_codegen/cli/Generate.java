package graphql_codegen.cli;

import io.airlift.airline.Option;

public abstract class Generate implements Runnable {

    @Option(name = {"-o", "--output"}, title = "output directory",
            description = "Where to write the generated files (default: .)")
    protected String output = ".";

    @Option(name = {"--username"}, title = "Basic Auth username",
            description = "Basic Auth username")
    protected String authUsername = null;

    @Option(name = {"--password"}, title = "Basic Auth password",
            description = "Basic Auth password")
    protected String authPassword = null;

    @Option(name = {"-u", "--url"}, title = "GraphQL server url", required = true, description = "Fully qualified GraphQL server url ")
    protected String serverUrl;

}
