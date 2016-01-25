package graphql_codegen.builder.java;

import com.samskivert.mustache.Mustache;

import java.io.*;
import java.util.List;

public class JavaEnumBuilder implements JavaCodeBuilder {

    private static final String TEMPLATE_FILE_NAME = "enum.mustache";

    private final String name;
    private final String description;
    private final String packagePath;

    private final List<String> members;

    JavaEnumBuilder(String packagePath, List<String> members, String name, String description) {
        this.packagePath = packagePath;
        this.members = members;
        this.name = name;
        this.description = description;
    }

    @Override
    public String code() {
        return code(this, TEMPLATE_FILE_NAME);
    }

    public String code(Object context, String fileName) {
        InputStream in = getClass().getResourceAsStream("/Java/"+ fileName);
        Reader reader = new InputStreamReader(in);

        Writer writer = new StringWriter();

        Mustache.compiler().compile(reader).execute(context, writer);

        return writer.toString();
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

        public JavaEnumBuilder build() {
            return new JavaEnumBuilder(packagePath, members, name, description);
        }
    }

}
