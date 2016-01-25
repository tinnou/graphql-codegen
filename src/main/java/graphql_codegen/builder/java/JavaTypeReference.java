package graphql_codegen.builder.java;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JavaTypeReference {

    private String typeName;
    private List<JavaTypeReference> genericParameters;
    private String generatedType;

    public JavaTypeReference(String typeName, List<JavaTypeReference> genericParameters) {
        this.genericParameters = genericParameters;
        this.typeName = typeName;
        this.generatedType = code();
    }

    public JavaTypeReference(String typeName) {
        this(typeName, new ArrayList<>());
    }

    public String getGeneratedType() {
        return generatedType;
    }

    public String getTypeName() {
        return typeName;
    }

    public String code() {
        switch (typeName) {
            case "List":
                if (genericParameters == null) {
                    return typeName;
                }
                String code =  "List<"
                        + genericParameters.stream()
                            .map(JavaTypeReference::code)
                            .collect(Collectors.joining(", "))
                        +">";
                return code;
            default:
                return typeName;
        }
    }
}
