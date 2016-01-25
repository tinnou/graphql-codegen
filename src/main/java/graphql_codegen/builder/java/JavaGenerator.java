package graphql_codegen.builder.java;

import graphql_codegen.CodeBuilder;
import graphql_codegen.type.GraphQLTypeDescription;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static graphql_codegen.Util.capitalizeFirstLetter;

public class JavaGenerator {

    private final String basePackage;

    public JavaGenerator(String basePackage) {
        this.basePackage = basePackage;
    }

    public CodeBuilder convertFromGraphQLTypeToJava(GraphQLTypeDescription type) {
        switch (type.getKind()) {
            case OBJECT:
            case INTERFACE:
                if (type.getName() == null) {
                    System.out.println("Object/Interface type must have a name");
                    return null;
                }

                if (type.getFields() == null || type.getFields().isEmpty()) {
                    System.out.println("Object/Interface type must have fields");
                    return null;
                }

                List<JavaField> javaFields = type.getFields().stream()
                        .map(f -> new JavaField(f.getName(),
                                                f.getDescription(),
                                                getJavaTypeReference(f.getType()),
                                                f.isDeprecated(), f.getDeprecationReason()))
                        .collect(Collectors.toList());

                List<JavaTypeReference> interfaces = new ArrayList<>();

                if (type.getInterfaces() != null) {
                    interfaces = type.getInterfaces().stream()
                            .map(i -> { return new JavaTypeReference(i.getName());})
                            .collect(Collectors.toList());
                }

                return JavaTypeBuilder.newJavaTypeBuilder()
                        .withPackagePath(basePackage)
                        .withName(capitalizeFirstLetter(type.getName()))
                        .withMembers(javaFields)
                        .withInheritedTypes(interfaces)
                        //.withSerializable(true)
                        //.withSuperClass("Object")
                        .withDescription(type.getDescription())
                        .build();
            case ENUM:
                if (type.getName() == null) {
                    System.out.println("Enum type must have a name");
                    return null;
                }

                if (type.getEnumValues() == null || type.getEnumValues().isEmpty()) {
                    System.out.println("Enum type must have at least one possible value");
                    return null;
                }

                List<String> enumValues = type.getEnumValues().stream()
                        .map(enumDesc -> enumDesc.getName().toUpperCase())
                        .collect(Collectors.toList());
                return JavaEnumBuilder.newJavaEnumBuilder()
                        .withPackagePath(basePackage)
                        .withName(type.getName())
                        .withMembers(enumValues)
                        .withDescription(type.getDescription())
                        .build();
            default:
                System.out.println("Unable to handle " + type.getKind() + " with name " + type.getName());
                return null;
        }
    }

    public static JavaTypeReference getJavaTypeReference(GraphQLTypeDescription type) {
        switch (type.getKind()) {
            case SCALAR:
                switch (type.getName()) {
                    case "ID":
                        return new JavaTypeReference("String");
                    case "Boolean":
                        return new JavaTypeReference("Boolean");
                    case "Int":
                        return new JavaTypeReference("Integer");
                    default:
                        return new JavaTypeReference(type.getName());
                }
            case LIST:
                if (type.getOfType() == null) {
                    System.out.println("List type missing inner type");
                    return new JavaTypeReference("INVALID_TYPE");
                }
                return new JavaTypeReference("List", Collections.singletonList(getJavaTypeReference(type.getOfType())));
            case NON_NULL:
                if (type.getOfType() == null) {
                    System.out.println("NonNull type missing inner type");
                    return new JavaTypeReference("INVALID_TYPE");
                }
                return getJavaTypeReference(type.getOfType());
            default:
                System.out.println("Unable to resolve type from GraphQL kind " + type.getKind()+ " with name " + type.getName());
                return new JavaTypeReference(type.getName());
        }
    }
}
