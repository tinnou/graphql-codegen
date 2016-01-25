package graphql_codegen.type;

import java.util.List;

public class GraphQLFieldDescription {

    private String name;
    private String description;
    private List<GraphQLInputFieldDescription> args;
    private GraphQLTypeDescription type;
    private boolean isDeprecated;
    private String deprecationReason;


    public List<GraphQLInputFieldDescription> getArgs() {
        return args;
    }

    public GraphQLFieldDescription setArgs(List<GraphQLInputFieldDescription> args) {
        this.args = args;
        return this;
    }

    public String getDeprecationReason() {
        return deprecationReason;
    }

    public GraphQLFieldDescription setDeprecationReason(String deprecationReason) {
        this.deprecationReason = deprecationReason;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public GraphQLFieldDescription setDescription(String description) {
        this.description = description;
        return this;
    }

    public boolean isDeprecated() {
        return isDeprecated;
    }

    public GraphQLFieldDescription setDeprecated(boolean deprecated) {
        isDeprecated = deprecated;
        return this;
    }

    public String getName() {
        return name;
    }

    public GraphQLFieldDescription setName(String name) {
        this.name = name;
        return this;
    }

    public GraphQLTypeDescription getType() {
        return type;
    }

    public GraphQLFieldDescription setType(GraphQLTypeDescription type) {
        this.type = type;
        return this;
    }
}
