package graphql_codegen.builder.java

import graphql_codegen.type.GraphQLFieldDescription
import graphql_codegen.type.GraphQLTypeDescription
import graphql_codegen.type.GraphQLTypekind
import spock.lang.Specification

class JavaGeneratorTest extends Specification {

    def "interface type conversion"() {
        setup:
        def javaGen = new JavaGenerator("com.disney.starwars");

        def graphQLTypeDesc = new GraphQLTypeDescription(
                kind: GraphQLTypekind.INTERFACE,
                name: 'Node',
                description: 'An object with an id',
                fields: [
                        new GraphQLFieldDescription(
                                name: 'id',
                                description:  'An ID',
                                type: new GraphQLTypeDescription(
                                        kind: GraphQLTypekind.NON_NULL,
                                        ofType: new GraphQLTypeDescription(
                                                kind: GraphQLTypekind.SCALAR,
                                                name: 'ID'
                                        )
                                )
                        )
                ]
        )

        def javaInterface = javaGen.convertGraphQLInterfaceTypeToCode(graphQLTypeDesc);

        expect:
        javaInterface.packagePath == 'com.disney.starwars'
        javaInterface.imports == [] as Set
        javaInterface.description == 'An object with an id'
        javaInterface.name == 'Node'
        javaInterface.fields.name == ['id']
        javaInterface.fields.description == ['An ID']
        javaInterface.fields.deprecated == [false]
        javaInterface.fields.deprecationReason == [null]
        javaInterface.fields.getter == ['getId']
        javaInterface.fields.setter == ['setId']
        javaInterface.fields.typeReference.typeName == ['String']
    }

}
