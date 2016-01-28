package graphql_codegen.builder.java;

import graphql_codegen.Util;

import java.util.List;

public class JavaEnum implements JavaCode {

    private static final String TEMPLATE_FILE_NAME = "enum.mustache";

    private final String name;
    private final String description;
    private final String packagePath;

    private final List<String> members;

    JavaEnum(String packagePath, List<String> members, String name, String description) {
        this.packagePath = packagePath;
        this.members = members;
        this.name = name;
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

    public String getDescription() {
        return description;
    }

    public List<String> getMembers() {
        return members;
    }

    public String getPackagePath() {
        return packagePath;
    }

    public static Builder newJavaEnumBuilder() {
        return new Builder();
    }

    public static class Builder {
        private String packagePath;
        private List<String> members;
        private String name;
        private String description;

        private Builder() {
        }

        public Builder withMembers(List<String> members) {
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

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public JavaEnum build() {
            return new JavaEnum(packagePath, members, name, description);
        }
    }

}
