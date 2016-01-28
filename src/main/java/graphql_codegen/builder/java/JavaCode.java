package graphql_codegen.builder.java;

import graphql_codegen.Code;

public interface JavaCode extends Code {
    default String extension() { return ".java"; }
    default String name() { return "java"; }
}
