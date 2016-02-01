package graphql_codegen.cli;

import graphql_codegen.Util;
import graphql_codegen.builder.java.JavaGenerator;
import graphql_codegen.type.GraphQLTypeDescription;
import io.airlift.airline.Command;
import io.airlift.airline.Option;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Command(name = "java", description = "Generate code for java")
public class GenerateJava extends Generate {

    @Option(name = {"--override"}, title = "imports override", description = "For custom GraphQL scalars of for external " +
            "libraries such as joda you can specify your own import for a given type. The format must be " +
            "type::package Package wildcard is allowed. e.g: DateTime::org.joda.time.DateTime or MyCustomList::com.my.MyCustomList " +
            "or e.g: DateTime::org.joda.time.*")
    private List<String> importOverrides = new ArrayList<>();

    @Option(name = {"--package"}, title = "java package", description = "Classes output package " +
            "(default: my.custom.package)")
    private String modelPackage;

    @Override
    public void run() {

        String javaDir = modelPackage.replace('.','/');

        Map<String, String> importOverridesMap = importOverrides.stream()
                .map(String::trim)
                .collect(Collectors.toMap(i -> i.split("\\s*::\\s*")[0], i -> i.split("\\s*::\\s*")[1]));

        JavaGenerator javaGenerator = new JavaGenerator(modelPackage, importOverridesMap);

        String responseStr = Util.fetchSchemaFromRemote(serverUrl, authUsername, authPassword);

        List<GraphQLTypeDescription> types = Util.buildTypes(responseStr);

        types.stream()
                .filter(type -> !type.getName().startsWith("__"))
                .map(javaGenerator::convertFromGraphQLTypeToCode)
                .filter(typeBuilder -> typeBuilder != null)
                .forEach(typeBuilder -> {
                    String outputFilePath = output + "/" +javaDir + "/" + typeBuilder.fileName() + typeBuilder.extension();
                    System.out.println("output file: "+ outputFilePath);
                    Util.writeToFile(outputFilePath, typeBuilder.code());
                });

        System.out.println("Done");
    }
}
