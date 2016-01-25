package graphql_codegen.type;

public class GraphQLEnumValueDescription {

    private String name;
    private String description;
    private boolean isDeprecated;
    private String deprecationReason;


    public String getDeprecationReason() {
        return deprecationReason;
    }

    public GraphQLEnumValueDescription setDeprecationReason(String deprecationReason) {
        this.deprecationReason = deprecationReason;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public GraphQLEnumValueDescription setDescription(String description) {
        this.description = description;
        return this;
    }

    public boolean isDeprecated() {
        return isDeprecated;
    }

    public GraphQLEnumValueDescription setDeprecated(boolean deprecated) {
        isDeprecated = deprecated;
        return this;
    }

    public String getName() {
        return name;
    }

    public GraphQLEnumValueDescription setName(String name) {
        this.name = name;
        return this;
    }
}
