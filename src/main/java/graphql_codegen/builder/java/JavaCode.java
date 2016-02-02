package graphql_codegen.builder.java;

import graphql_codegen.Code;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class JavaCode implements Code {

    protected final static Map<String, String> STANDARD_IMPORTS = new HashMap<>();

    static
    {
        STANDARD_IMPORTS.put("BigDecimal", "java.math.BigDecimal");
        STANDARD_IMPORTS.put("UUID", "java.util.UUID");
        STANDARD_IMPORTS.put("Date", "java.util.Date");
        STANDARD_IMPORTS.put("Timestamp", "java.sql.Timestamp");
        STANDARD_IMPORTS.put("ZonedDateTime", "java.time.ZonedDateTime");
        STANDARD_IMPORTS.put("OffsetDateTime", "java.time.OffsetDateTime");
        STANDARD_IMPORTS.put("OffsetTime", "java.time.OffsetTime");
        STANDARD_IMPORTS.put("Instant", "java.time.Instant");
        STANDARD_IMPORTS.put("LocalDateTime", "java.time.LocalDateTime");
        STANDARD_IMPORTS.put("LocalDate", "java.time.LocalDate");
        STANDARD_IMPORTS.put("LocalTime", "java.time.LocalTime");
        STANDARD_IMPORTS.put("Duration", "java.time.Duration");
        STANDARD_IMPORTS.put("List", "java.util.List");
        STANDARD_IMPORTS.put("Set", "java.util.Set");
    }

    public String extension() { return ".java"; }


    protected static Set<String> buildImports(List<JavaField> members, List<JavaTypeReference> inheritedTypes, Map<String, String> typeNameToPackageOverrideMap) {
        Set<String> imports = new HashSet<>();
        // check members
        if (members != null) {
            // field type
            Stream<JavaTypeReference> fieldTypeRef = members.stream()
                    .map(JavaField::getTypeReference);
            // generic params type
            Stream<JavaTypeReference> genericParamsTypeRef = members.stream()
                    .map(JavaField::getTypeReference)
                    .filter(f -> f.getGenericParameters() != null)
                    .flatMap(f -> f.getGenericParameters().stream());

            Set<String> memberImports = Stream.concat(fieldTypeRef, genericParamsTypeRef)
                    .map(tr -> getImportFromTypeReference(tr, typeNameToPackageOverrideMap))
                    .filter(importStr -> importStr != null)
                    .collect(Collectors.toSet());

            imports.addAll(memberImports);
        }
        // check inherited types
        if (inheritedTypes != null) {
            Set<String> inheritedTypesImports = inheritedTypes.stream()
                    .map(tr -> getImportFromTypeReference(tr, typeNameToPackageOverrideMap))
                    .filter(importStr -> importStr != null)
                    .collect(Collectors.toSet());

            imports.addAll(inheritedTypesImports);
        }
        return imports;
    }


    protected static String getImportFromTypeReference(JavaTypeReference f, Map<String, String> typeNameToPackageOverrideMap) {
        // check from overrides
        if (typeNameToPackageOverrideMap.containsKey(f.getTypeName())) {
            return typeNameToPackageOverrideMap.get(f.getTypeName());
        }

        // check if it's a standard java type
        if (STANDARD_IMPORTS.containsKey(f.getTypeName())) {
            return STANDARD_IMPORTS.get(f.getTypeName());
        }

        // is unknown
        return null;
    }
}
