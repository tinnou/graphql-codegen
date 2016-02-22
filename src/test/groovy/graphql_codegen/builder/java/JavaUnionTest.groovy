package graphql_codegen.builder.java

import com.fasterxml.jackson.databind.ObjectMapper
import graphql.GraphQL
import graphql_codegen.GarfieldSchema
import graphql_codegen.Util
import graphql_codegen.type.GraphQLTypeDescription
import spock.lang.Specification

import java.util.function.Predicate

class JavaUnionTest extends Specification{

    def mapper = new ObjectMapper();
    def List<GraphQLTypeDescription> types;

    def setup() {
        def schema = GarfieldSchema.GarfieldSchema;
        def result = new GraphQL(schema).execute(Util.introspectionQuery());
        def schemaAsString = mapper.writeValueAsString(result);
        types = Util.buildTypes(schemaAsString);
    }

    def "Union example: Person->pets and each Pet a Union of Cat and Dog"() {
        setup:
        GraphQLTypeDescription unionPersonType = types.stream()
                .filter(new Predicate<GraphQLTypeDescription>() {
                    @Override
                    public boolean test(GraphQLTypeDescription type) {
                        return type.name.equals(GarfieldSchema.PersonType.name);
                    }
                })
                .findFirst()
                .get();

        def javaGen = new JavaGenerator("com.disney.starwars");
        def javaUnionTypeAsField = javaGen.convertGraphQLObjectTypeToCode(unionPersonType);

        expect:
        javaUnionTypeAsField.code().replace("\n", "").replace("\t", "").replace(" ", "") == getExpectedPerson().replace("\n", "").replace("\t", "").replace(" ", "");
    }

    def "UnionType should not generate any code"() {
        setup:
        GraphQLTypeDescription unionPetType = types.stream()
                .filter(new Predicate<GraphQLTypeDescription>() {
            @Override
            public boolean test(GraphQLTypeDescription type) {
                return type.name.equals(GarfieldSchema.PetType.name);
            }
        })
                .findFirst()
                .get();

        def javaGen = new JavaGenerator("com.disney.starwars");
        def javaUnionType = javaGen.convertFromGraphQLTypeToCode(unionPetType);

        expect:
        javaUnionType == null
    }

    def "PersonType has a pets field that is of type union"() {
        setup:
        GraphQLTypeDescription unionPersonType = types.stream()
                .filter(new Predicate<GraphQLTypeDescription>() {
            @Override
            public boolean test(GraphQLTypeDescription type) {
                return type.name.equals(GarfieldSchema.PersonType.name);
            }
        })
                .findFirst()
                .get();

        def javaGen = new JavaGenerator("com.disney.starwars");
        def javaUnionTypeAsField = javaGen.convertGraphQLObjectTypeToCode(unionPersonType);

        expect:
        javaUnionTypeAsField.packagePath == 'com.disney.starwars'
        javaUnionTypeAsField.genericTypes == ['Pet']
    }

    def getExpectedPerson() {
        return """
            package com.disney.starwars;

            import java.util.List;

            public class Person<Pet> implements Named {


                private String name;

                private List<Pet> pets;

                private List<Named> friends;

                public String getName() {
                    return name;
                }
                public void setName(String name) {
                    this.name = name;
                }

                public List<Pet> getPets() {
                    return pets;
                }
                public void setPets(List<Pet> pets) {
                    this.pets = pets;
                }

                public List<Named> getFriends() {
                    return friends;
                }
                public void setFriends(List<Named> friends) {
                    this.friends = friends;
                }

            }
        """.stripIndent();
    }
}
