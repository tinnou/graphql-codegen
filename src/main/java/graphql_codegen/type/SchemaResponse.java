package graphql_codegen.type;

import java.util.List;

public class SchemaResponse {
    private List<GraphQLTypeDescription> types;

    public List<GraphQLTypeDescription> getTypes() {
        return types;
    }

    public SchemaResponse setTypes(List<GraphQLTypeDescription> types) {
        this.types = types;
        return this;
    }
}