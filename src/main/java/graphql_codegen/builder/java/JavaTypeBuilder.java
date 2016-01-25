package graphql_codegen.builder.java;

import graphql_codegen.Util;

import java.util.Collections;
import java.util.List;

public class JavaTypeBuilder implements JavaCodeBuilder {

    private final static String TEMPLATE_FILE_NAME = "type.mustache";

    private final String packagePath;
    private final List<String> imports;
    private final String name;
    private final String superClass;
    private final List<JavaField> members;
    private final List<JavaTypeReference> inheritedTypes;
    private final boolean serializable;
    private final String description;

    private JavaTypeBuilder(String superClass, List<String> imports,
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
        return Util.code(this, TEMPLATE_FILE_NAME);
    }

    public List<String> getImports() {
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

        public JavaTypeBuilder build() {
            return new JavaTypeBuilder(superClass, Collections.emptyList(), inheritedTypes, members, name, packagePath, serializable,
                                       description);
        }
    }
}
