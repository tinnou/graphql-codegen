package graphql_codegen.cli;

import graphql_codegen.Util;
import graphql_codegen.builder.java.JavaGenerator;
import graphql_codegen.type.GraphQLTypeDescription;
import io.airlift.airline.Command;
import io.airlift.airline.Option;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

@Command(name = "generate", description = "Generate code with chosen lang")
public class Generate implements Runnable {

    @Option(name = {"-l", "--lang"}, title = "language", required = true,
            description = "client language to generate")
    private String lang;

    @Option(name = {"-o", "--output"}, title = "output directory",
            description = "where to write the generated files (current dir by default)")
    private String output = "";

    @Option(name = {"-a", "--auth"}, title = "authorization",
            description = "adds authorization headers when fetching the GraphQL definitions remotely. " +
                    "Pass in a URL-encoded string of name:header with a comma separating multiple values")
    private String auth;

    @Option(name = {"--package"}, title = "java package", description = "Your Java package" +
            "default is com.disney.starwars")
    private String modelPackage;

    @Option(name = {"-u", "--url"}, title = "GraphQL server url", description = "Fully GraphQL server url - " +
            "default is http://graphql-swapi.parseapp.com/graphiql")
    private String serverUrl;

    @Override
    public void run() {

        //set defaults
        if (modelPackage == null || modelPackage.trim().isEmpty()) {
            modelPackage = "com.disney.starwars";
        }
        if (serverUrl == null || serverUrl.trim().isEmpty()) {
            serverUrl = "http://graphql-swapi.parseapp.com/graphiql";
        }

        //only java supported for now
        checkArgument(lang.equals("java"), "-l --lang has to match one of the supported languages");

        String javaDir = modelPackage.replace('.','/');

        JavaGenerator javaGenerator = new JavaGenerator(modelPackage);

        String responseStr = Util.fetchSchemaFromRemote(serverUrl);

        List<GraphQLTypeDescription> types = Util.buildTypes(responseStr);

        types.stream()
                .filter(type -> !type.getName().startsWith("__"))
                .map(javaGenerator::convertFromGraphQLTypeToJava)
                .filter(typeBuilder -> typeBuilder != null)
                .forEach(typeBuilder -> {
                    System.out.println("output : "+ typeBuilder.code());
                    Util.writeToFile(output + javaDir + "/" + typeBuilder.fileName() + typeBuilder.extension(), typeBuilder.code());
                });

        System.out.println("Done");
    }
}
