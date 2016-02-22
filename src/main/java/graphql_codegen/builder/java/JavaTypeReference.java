package graphql_codegen.builder.java;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

public class JavaTypeReference {

    private String typeName;
    private List<JavaTypeReference> genericParameters;
    private String generatedType;
    private boolean generic; // whether it's a genericType e.g <T>

    public JavaTypeReference(String typeName, List<JavaTypeReference> genericParameters) {
        checkNotNull(genericParameters);
        this.genericParameters = genericParameters;
        this.typeName = typeName;
        this.generatedType = code();
        this.generic = false;
    }

    public JavaTypeReference(String typeName) {
        this(typeName, new ArrayList<>());
    }

    public JavaTypeReference(String typeName, boolean generic) {
        this(typeName, new ArrayList<>());
        this.generic = generic;
    }

    public String getGeneratedType() {
        return generatedType;
    }

    public String getTypeName() {
        return typeName;
    }

    public List<JavaTypeReference> getGenericParameters() {
        return genericParameters;
    }

    public boolean isGeneric() {
        return generic;
    }

    public String code() {
        switch (typeName) {
            case "List":
                if (genericParameters == null) {
                    return typeName;
                }
                return "List<"
                        + genericParameters.stream()
                            .map(JavaTypeReference::getTypeName)
                            .collect(Collectors.joining(",")) /* what about List<List<String>>, is that even possible? */
                        +">";
            default:
                return typeName;
        }
    }
}
