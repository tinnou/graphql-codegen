package graphql_codegen.builder.java;

import graphql_codegen.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

public class JavaInterface extends JavaCode {

    private final static String TEMPLATE_FILE_NAME = "interface.mustache";

    private final String packagePath;
    private final Set<String> imports;
    private final String name;
    private final List<JavaTypeReference> superInterfaces;
    private final List<JavaField> fields;
    private final String description;

    private JavaInterface(List<JavaTypeReference> superInterfaces, Set<String> imports,
                          List<JavaField> fields,
                          String name, String packagePath, String description) {
        this.superInterfaces = superInterfaces;
        this.imports = imports;
        this.fields = fields;
        this.name = name;
        this.packagePath = packagePath;
        this.description = description;
    }

    @Override
    public String code() {
        return Util.fillOutJavaTemplate(this, TEMPLATE_FILE_NAME);
    }

    @Override
    public String fileName() {
        return name;
    }

    public Set<String> getImports() {
        return imports;
    }

    public String getPackagePath() {
        return packagePath;
    }

    public List<JavaField> getFields() {
        return fields;
    }

    public String getName() {
        return name;
    }

    public List<JavaTypeReference> getSuperInterfaces() {
        return superInterfaces;
    }

    public String getDescription() {
        return description;
    }

    public static Builder newJavaInterfaceBuilder() {
        return new Builder();
    }

    public static class Builder {
        private List<JavaTypeReference> superInterfaces;
        private List<JavaField> fields;
        private String name;
        private String packagePath;
        private String description;
        private Map<String, String> typeNameToPackageOverrideMap = new HashMap<>();

        public Builder withImportMapOverride(Map<String,String> typeNameToPackageOverrideMap) {
            checkNotNull(typeNameToPackageOverrideMap);
            this.typeNameToPackageOverrideMap = typeNameToPackageOverrideMap;
            return this;
        }

        public Builder withSuperInterfaces(List<JavaTypeReference> superInterfaces) {
            this.superInterfaces = superInterfaces;
            return this;
        }

        public Builder withFields(List<JavaField> fields) {
            this.fields = fields;
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

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public JavaInterface build() {
            return new JavaInterface(superInterfaces, buildImports(fields, superInterfaces, typeNameToPackageOverrideMap), fields, name, packagePath, description);
        }
    }
}
