package graphql_codegen.type;

public class GraphQLInputFieldDescription {

    private String name;
    private String description;
    private GraphQLTypeDescription type;
    private String defaultValue;


    public String getDefaultValue() {
        return defaultValue;
    }

    public GraphQLInputFieldDescription setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public GraphQLInputFieldDescription setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getName() {
        return name;
    }

    public GraphQLInputFieldDescription setName(String name) {
        this.name = name;
        return this;
    }

    public GraphQLTypeDescription getType() {
        return type;
    }

    public GraphQLInputFieldDescription setType(GraphQLTypeDescription type) {
        this.type = type;
        return this;
    }
}
