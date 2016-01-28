package graphql_codegen;

import graphql_codegen.cli.Generate;
import graphql_codegen.cli.Langs;
import io.airlift.airline.Cli;
import io.airlift.airline.Help;

public class Main {

    public static void main(String[] args) throws Exception {
        @SuppressWarnings("unchecked")
        Cli.CliBuilder<Runnable> builder = Cli.<Runnable>builder("graphql-codegen")
                .withDescription("GraphQL code generator CLI.")
                .withDefaultCommand(Langs.class)
                .withCommands(
                        Generate.class,
                        Help.class
                );

        builder.build().parse(args).run();
    }
}
