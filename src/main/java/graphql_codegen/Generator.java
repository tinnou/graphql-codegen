package graphql_codegen;

import graphql_codegen.type.GraphQLTypeDescription;

public interface Generator {
    Code convertFromGraphQLTypeToCode(GraphQLTypeDescription type);
}
