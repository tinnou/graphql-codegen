package graphql_codegen.builder.java;

import graphql_codegen.Util;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

public class JavaType extends JavaCode {

    private final static String TEMPLATE_FILE_NAME = "type.mustache";

    private final String packagePath;
    private final Set<String> imports;
    private final String name;
    private final String superClass;
    private final List<JavaField> members;
    private final List<JavaTypeReference> inheritedTypes;
    private final boolean serializable;
    private final String description;

    private JavaType(String superClass, Set<String> imports,
                     List<JavaTypeReference> inheritedTypes, List<JavaField> members,
                     String name, String packagePath, boolean serializable, String description) {
        this.superClass = superClass;
        this.imports = imports;
        this.inheritedTypes = inheritedTypes;
        this.members = members;
        this.name = name;
        this.packagePath = packagePath;
        this.serializable = serializable;
        this.description = description;
    }

    @Override
    public String code() {
        return Util.fillOutJavaTemplate(this, TEMPLATE_FILE_NAME);
    }

    public Set<String> getImports() {
        return imports;
    }

    public String fileName() {
        return name;
    }

    public String getPackagePath() {
        return packagePath;
    }

    public List<JavaTypeReference> getInheritedTypes() {
        return inheritedTypes;
    }

    public List<JavaField> getMembers() {
        return members;
    }

    public boolean isSerializable() {
        return serializable;
    }

    public String getSuperClass() {
        return superClass;
    }

    public String getDescription() {
        return description;
    }

    public static Builder newJavaTypeBuilder() {
        return new Builder();
    }

    public static class Builder {
        private String superClass;
        private List<JavaTypeReference> inheritedTypes;
        private List<JavaField> members;
        private String name;
        private String packagePath;
        private boolean serializable;
        private String description;
        private Map<String, String> typeNameToPackageOverrideMap = new HashMap<>();

        public Builder withImportMapOverride(Map<String,String> typeNameToPackageOverrideMap) {
            checkNotNull(typeNameToPackageOverrideMap);
            this.typeNameToPackageOverrideMap = typeNameToPackageOverrideMap;
            return this;
        }


        public Builder addImportOverride(String typeName, String packagePath) {
            typeNameToPackageOverrideMap.put(typeName, packagePath);
            return this;
        }

        public Builder withSuperClass(String superClass) {
            this.superClass = superClass;
            return this;
        }

        public Builder withInheritedTypes(List<JavaTypeReference> inheritedTypes) {
            this.inheritedTypes = inheritedTypes;
            return this;
        }

        public Builder withMembers(List<JavaField> members) {
            this.members = members;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withPackagePath(String packagePath) {
            this.packagePath = packagePath;
            return this;
        }

        public Builder withSerializable(boolean serializable) {
            this.serializable = serializable;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public JavaType build() {
            return new JavaType(superClass, buildImports(), inheritedTypes, members, name, packagePath, serializable, description);
        }

        private Set<String> buildImports() {
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
                        .map(this::getImportFromTypeReference)
                        .filter(importStr -> importStr != null)
                        .collect(Collectors.toSet());

                imports.addAll(memberImports);
            }
            // check inherited types
            if (inheritedTypes != null) {
                Set<String> inheritedTypesImports = inheritedTypes.stream()
                        .map(this::getImportFromTypeReference)
                        .filter(importStr -> importStr != null)
                        .collect(Collectors.toSet());

                imports.addAll(inheritedTypesImports);
            }
            return imports;
        }

        private String getImportFromTypeReference(JavaTypeReference f) {
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
}
