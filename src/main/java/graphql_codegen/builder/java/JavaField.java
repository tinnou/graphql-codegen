package graphql_codegen.builder.java;

import static graphql_codegen.Util.capitalizeFirstLetter;

public class JavaField {

    private String name;
    private String description;
    private JavaTypeReference typeReference;
    private String setter;
    private String getter;
    private boolean deprecated;
    private String deprecationReason;


    public JavaField(String name, String description,
                     JavaTypeReference typeReference,
                     boolean deprecated, String deprecationReason) {
        this.name = name;
        this.description = description;
        this.typeReference = typeReference;
        this.deprecated = deprecated;
        this.deprecationReason = deprecationReason;
        this.getter = "get" + capitalizeFirstLetter(name);
        this.setter = "set" + capitalizeFirstLetter(name);
    }


    public JavaTypeReference getTypeReference() {
        return typeReference;
    }

    public String getDescription() {
        return description;
    }

    public String getGetter() {
        return getter;
    }

    public String getSetter() {
        return setter;
    }

    public boolean isDeprecated() {
        return deprecated;
    }

    public String getDeprecationReason() {
        return deprecationReason;
    }

    public String getName() {
        return name;
    }
}
