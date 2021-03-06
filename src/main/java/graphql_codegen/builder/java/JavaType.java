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
    private final List<String> genericTypes;  // contains the generic types in  "class SomeClass<Type1,Type2>"
    private final boolean serializable;
    private final String description;

    private JavaType(String superClass, Set<String> imports,
                     List<JavaTypeReference> inheritedTypes, List<JavaField> members,
                     String name, String packagePath, List<String> genericTypes, boolean serializable, String description) {
        this.superClass = superClass;
        this.imports = imports;
        this.inheritedTypes = inheritedTypes;
        this.members = members;
        this.name = name;
        this.packagePath = packagePath;
        this.genericTypes = genericTypes;
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

    public List<String> getGenericTypes() {
        return genericTypes;
    }

    public static Builder newJavaTypeBuilder() {
        return new Builder();
    }

    public static class Builder {
        private String superClass;
        private List<JavaTypeReference> inheritedTypes;
        private List<JavaField> members = new ArrayList<>();
        private String name;
        private String packagePath;
        private boolean serializable;
        private String description;
        private Map<String, String> typeNameToPackageOverrideMap = new HashMap<>();
        private List<String> genericTypes;

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
            this.members.addAll(members);
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

            Stream<String> membersStream = members.stream()
                    .filter(javaField -> javaField.getTypeReference() != null && javaField.getTypeReference().isGeneric())
                    .map(javaField -> javaField.getTypeReference().getTypeName());

            Stream<String> membersGenericStream = members.stream()
                    .filter(javaField -> javaField.getTypeReference() != null && !javaField.getTypeReference().getGenericParameters().isEmpty())
                    .flatMap(javaField -> javaField.getTypeReference().getGenericParameters().stream())
                    .filter(JavaTypeReference::isGeneric)
                    .map(JavaTypeReference::getTypeName);

            genericTypes = Stream.concat(membersGenericStream, membersStream).collect(Collectors.toList());

            return new JavaType(superClass, buildImports(members, inheritedTypes, typeNameToPackageOverrideMap), inheritedTypes, members, name, packagePath,
                                genericTypes, serializable, description);
        }
    }
}
