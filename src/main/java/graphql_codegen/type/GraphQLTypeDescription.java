package graphql_codegen.type;

import java.util.List;

public class GraphQLTypeDescription {

    private GraphQLTypekind kind;
    private String name;
    private String description;
    private List<GraphQLFieldDescription> fields;
    private List<GraphQLTypeDescription> interfaces;
    private List<GraphQLTypeDescription> possibleTypes;
    private List<GraphQLEnumValueDescription> enumValues;
    private List<GraphQLInputFieldDescription> inputFields;
    private GraphQLTypeDescription ofType;

    public String getDescription() {
        return description;
    }

    public GraphQLTypeDescription setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<GraphQLEnumValueDescription> getEnumValues() {
        return enumValues;
    }

    public GraphQLTypeDescription setEnumValues(List<GraphQLEnumValueDescription> enumValues) {
        this.enumValues = enumValues;
        return this;
    }

    public List<GraphQLFieldDescription> getFields() {
        return fields;
    }

    public GraphQLTypeDescription setFields(List<GraphQLFieldDescription> fields) {
        this.fields = fields;
        return this;
    }

    public List<GraphQLInputFieldDescription> getInputFields() {
        return inputFields;
    }

    public GraphQLTypeDescription setInputFields(List<GraphQLInputFieldDescription> inputFields) {
        this.inputFields = inputFields;
        return this;
    }

    public List<GraphQLTypeDescription> getInterfaces() {
        return interfaces;
    }

    public GraphQLTypeDescription setInterfaces(List<GraphQLTypeDescription> interfaces) {
        this.interfaces = interfaces;
        return this;
    }

    public GraphQLTypekind getKind() {
        return kind;
    }

    public GraphQLTypeDescription setKind(GraphQLTypekind kind) {
        this.kind = kind;
        return this;
    }

    public String getName() {
        return name;
    }

    public GraphQLTypeDescription setName(String name) {
        this.name = name;
        return this;
    }

    public GraphQLTypeDescription getOfType() {
        return ofType;
    }

    public GraphQLTypeDescription setOfType(GraphQLTypeDescription ofType) {
        this.ofType = ofType;
        return this;
    }

    public List<GraphQLTypeDescription> getPossibleTypes() {
        return possibleTypes;
    }

    public GraphQLTypeDescription setPossibleTypes(List<GraphQLTypeDescription> possibleTypes) {
        this.possibleTypes = possibleTypes;
        return this;
    }
}
