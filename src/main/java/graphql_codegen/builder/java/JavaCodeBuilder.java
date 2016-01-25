package graphql_codegen.builder.java;

import graphql_codegen.CodeBuilder;

public interface JavaCodeBuilder extends CodeBuilder {
    default String extension() { return ".java"; }
}
