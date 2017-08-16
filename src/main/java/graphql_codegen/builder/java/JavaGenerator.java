package graphql_codegen.builder.java;

import graphql_codegen.Code;
import graphql_codegen.Generator;
import graphql_codegen.Util;
import graphql_codegen.type.GraphQLEnumValueDescription;
import graphql_codegen.type.GraphQLTypeDescription;

import java.util.*;
import java.util.stream.Collectors;

import static graphql_codegen.Util.capitalizeFirstLetter;

public class JavaGenerator implements Generator {

    private final String basePackage;
    private final Map<String, String> typeNameToPackageOverrideMap;

    public JavaGenerator(String basePackage) {
        this(basePackage, null);
    }

    public JavaGenerator(String basePackage, Map<String, String> typeNameToPackageOverrideMap) {
        this.basePackage = basePackage;
        this.typeNameToPackageOverrideMap = typeNameToPackageOverrideMap != null ? typeNameToPackageOverrideMap : new HashMap<>();
    }

    public Code convertFromGraphQLTypeToCode(GraphQLTypeDescription type) {
        switch (type.getKind()) {
            case INPUT_OBJECT:
                return convertGraphQLInputObjectTypeToCode(type);
            case OBJECT:
                return convertGraphQLObjectTypeToCode(type);
            case INTERFACE:
                return convertGraphQLInterfaceTypeToCode(type);
            case ENUM:
                return convertFromGraphQLEnumTypeToCode(type);
            case UNION: // is handled with generic types
            default:
                return null;
        }
    }

    public JavaType convertGraphQLInputObjectTypeToCode(GraphQLTypeDescription type) {
        if (type.getName() == null) {
            System.out.println("Input Object type "+type.getName()+" must have a name");
            return null;
        }

        if (type.getInputFields() == null || type.getInputFields().isEmpty()) {
            System.out.println("Input Object type "+type.getName()+" must have fields");
            return null;
        }

        List<JavaField> javaInputFields = type.getInputFields().stream()
                .map(f -> {
                    return new JavaField(f.getName(),
                                         f.getDescription(),
                                         getJavaTypeReference(f.getType()),
                                         false, null);
                })
                .collect(Collectors.toList());

        List<JavaTypeReference> inputInterfaces =
                convertInterfacesToJavaTypeReferences(type.getInterfaces());

        return JavaType.newJavaTypeBuilder()
                .withPackagePath(basePackage)
                .withImportMapOverride(typeNameToPackageOverrideMap)
                .withName(capitalizeFirstLetter(type.getName()))
                .withMembers(javaInputFields)
                .withInheritedTypes(inputInterfaces)
                //.withSerializable(true)
                //.withSuperClass("Object")
                .withDescription(type.getDescription())
                .build();
    }

    public JavaType convertGraphQLObjectTypeToCode(GraphQLTypeDescription type) {
        if (type.getName() == null) {
            System.out.println("Object type "+type.getName()+" must have a name");
            return null;
        }

        if (type.getFields() == null || type.getFields().isEmpty()) {
            System.out.println("Object type "+type.getName()+" must have fields");
            return null;
        }

        List<JavaField> javaFields = type.getFields().stream()
                .map(f -> {
                    return new JavaField(f.getName(),
                                         f.getDescription(),
                                         getJavaTypeReference(f.getType()),
                                         f.isDeprecated(), f.getDeprecationReason());
                })
                .collect(Collectors.toList());

        List<JavaTypeReference> interfaces =
                convertInterfacesToJavaTypeReferences(type.getInterfaces());

        return JavaType.newJavaTypeBuilder()
                .withPackagePath(basePackage)
                .withImportMapOverride(typeNameToPackageOverrideMap)
                .withName(capitalizeFirstLetter(type.getName()))
                .withMembers(javaFields)
                .withInheritedTypes(interfaces)
                //.withSerializable(true)
                //.withSuperClass("Object")
                .withDescription(type.getDescription())
                .build();
    }

    public JavaInterface convertGraphQLInterfaceTypeToCode(GraphQLTypeDescription type) {
        if (type.getName() == null) {
            System.out.println("Interface type "+type.getName()+" must have a name");
            return null;
        }

        if (type.getFields() == null || type.getFields().isEmpty()) {
            System.out.println("Interface type "+type.getName()+" must have fields");
            return null;
        }

        List<JavaField> javaInterfaceFields = type.getFields().stream()
                .map(f -> new JavaField(f.getName(),
                                        f.getDescription(),
                                        getJavaTypeReference(f.getType()),
                                        f.isDeprecated(), f.getDeprecationReason()))
                .collect(Collectors.toList());

        List<JavaTypeReference> superInterfaces =
                convertInterfacesToJavaTypeReferences(type.getInterfaces());

        return JavaInterface.newJavaInterfaceBuilder()
                .withPackagePath(basePackage)
                .withImportMapOverride(typeNameToPackageOverrideMap)
                .withName(capitalizeFirstLetter(type.getName()))
                .withFields(javaInterfaceFields)
                .withSuperInterfaces(superInterfaces)
                //.withSerializable(true)
                .withDescription(type.getDescription())
                .build();
    }

    public JavaEnum convertFromGraphQLEnumTypeToCode(GraphQLTypeDescription type) {
        if (type.getName() == null) {
            System.out.println("Enum type must have a name");
            return null;
        }

        if (type.getEnumValues() == null || type.getEnumValues().isEmpty()) {
            System.out.println("Enum type must have at least one possible value");
            return null;
        }

        List<String> enumValues = type.getEnumValues().stream()
                .map(this::convertToEnumKeyValue)
                .collect(Collectors.toList());
        return JavaEnum.newJavaEnumBuilder()
                .withPackagePath(basePackage)
                .withName(capitalizeFirstLetter(type.getName()))
                .withMembers(enumValues)
                .withDescription(type.getDescription())
                .build();
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
                        return new JavaTypeReference(Util.capitalizeFirstLetter(type.getName()));
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
            case UNION:
                return new JavaTypeReference(Util.capitalizeFirstLetter(type.getName()), true);
            default:
                return new JavaTypeReference(Util.capitalizeFirstLetter(type.getName()));
        }
    }

    private List<JavaTypeReference> convertInterfacesToJavaTypeReferences(List<GraphQLTypeDescription> interfaces) {
        List<JavaTypeReference> convertedInterfaces = new ArrayList<>();

        if (interfaces != null) {
            convertedInterfaces = interfaces.stream()
                    .map(i -> new JavaTypeReference(
                            Util.capitalizeFirstLetter(i.getName())))
                    .collect(Collectors.toList());
        }
        return convertedInterfaces;
    }

    private String convertToEnumKeyValue(GraphQLEnumValueDescription enumDesc) {
        return enumDesc
                .getName().toUpperCase() + "(\"" + enumDesc.getName() +"\")";
    }
}
