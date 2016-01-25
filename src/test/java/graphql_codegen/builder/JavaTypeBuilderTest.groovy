package graphql_codegen.builder

import graphql_codegen.builder.java.JavaTypeBuilder
import spock.lang.Specification;

class JavaTypeBuilderTest extends Specification{

    def "Sample Java Type"() {
        given:
        def type = JavaTypeBuilder.newJavaTypeBuilder()
                .withName("Human")
                .withPackagePath("com.lucasArts.films")
                .withImports(Collections.singletonList("Character"))
                .withSerializable(true)
                .withMembers()


        //then:
    }
}
