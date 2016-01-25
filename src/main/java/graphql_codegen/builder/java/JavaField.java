package graphql_codegen.builder.java;

import static graphql_codegen.Util.capitalizeFirstLetter;

public class JavaField {

    private String name;
    private String description;
    private JavaTypeReference typeReference;
    private String setter;
    private String getter;
    private boolean isDeprecated;
    private String deprecationReason;


    public JavaField(String name, String description,
                     JavaTypeReference typeReference,
                     boolean isDeprecated, String deprecationReason) {
        this.name = name;
        this.description = description;
        this.typeReference = typeReference;
        this.isDeprecated = isDeprecated;
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
        return isDeprecated;
    }

    public String getDeprecationReason() {
        return deprecationReason;
    }

    public String getName() {
        return name;
    }
}
