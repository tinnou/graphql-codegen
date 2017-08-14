package graphql_codegen.builder.java

import graphql_codegen.type.GraphQLFieldDescription
import graphql_codegen.type.GraphQLTypeDescription
import graphql_codegen.type.GraphQLTypekind
import spock.lang.Specification

class JavaInterfaceImplementingGeneratorTest extends Specification {

    def "interface type conversion"() {
        setup:
        def javaGen = new JavaGenerator("com.disney.starwars");

        def graphQLTypeDesc = new GraphQLTypeDescription(
                kind: GraphQLTypekind.OBJECT,
                name: 'myClass',
                description: 'Some description',
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
                ],
                interfaces: [
                        new GraphQLTypeDescription(
                                kind: GraphQLTypekind.INTERFACE,
                                name: 'firstInterface'),
                        new GraphQLTypeDescription(
                                kind: GraphQLTypekind.INTERFACE,
                                name: 'secondInterface')
                ]
        )

        def javaInterface = javaGen.convertGraphQLObjectTypeToCode(graphQLTypeDesc);

        expect:
        javaInterface.packagePath == 'com.disney.starwars'
        javaInterface.imports == [] as Set
        javaInterface.description == 'Some description'
        javaInterface.name == 'MyClass'

        javaInterface.inheritedTypes.size() == 2
        javaInterface.inheritedTypes.get(0).typeName == 'FirstInterface'
        javaInterface.inheritedTypes.get(1).typeName == 'SecondInterface'

        javaInterface.members.size() == 1
        javaInterface.members.get(0)
        javaInterface.members.get(0).name == 'id'
        javaInterface.members.get(0).description == 'An ID'
        javaInterface.members.get(0).deprecated == false
        javaInterface.members.get(0).deprecationReason == null
        javaInterface.members.get(0).getter == 'getId'
        javaInterface.members.get(0).setter == 'setId'
        javaInterface.members.get(0).typeReference.typeName == 'String'
    }

}
