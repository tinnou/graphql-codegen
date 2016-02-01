package graphql_codegen;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.mashape.unirest.request.body.RequestBodyEntity;
import com.samskivert.mustache.Mustache;
import graphql_codegen.type.GraphQLTypeDescription;
import graphql_codegen.type.SchemaResponse;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Util {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Unirest.setObjectMapper(new com.mashape.unirest.http.ObjectMapper() {
            @Override
            public <T> T readValue(String value, Class<T> valueType) {
                try {
                    return objectMapper.readValue(value, valueType);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public String writeValue(Object value) {
                try {
                    return objectMapper.writeValueAsString(value);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public static String capitalizeFirstLetter(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public static String fillOutJavaTemplate(Object context, String fileName) {
        return fillOutTemplate(context, fileName, "java");
    }

    public static String fillOutTemplate(Object context, String fileName, final String language) {
        // open template for filling
        InputStream in = Thread.currentThread().getClass().getResourceAsStream("/"+language+"/"+ fileName);
        Reader reader = new InputStreamReader(in);

        Writer writer = new StringWriter();

        // allow loading sub template from same directory
        Mustache.Compiler c = Mustache.compiler().withLoader(new Mustache.TemplateLoader() {
            public Reader getTemplate (String name) {
                InputStream in = Thread.currentThread().getClass().getResourceAsStream("/"+language+"/"+ name + ".mustache");
                return new InputStreamReader(in);
            }
        });
        c.compile(reader).execute(context, writer);

        return writer.toString();
    }

    public static String convertStreamToString(InputStream is, String charsetName) {
        Scanner s = new Scanner(is, charsetName).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static String fetchSchemaFromRemote(String url, String basicAuthUsername, String basicAuthPassword) {
        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("query", introspectionQuery());
        bodyMap.put("variables", null);

        HttpRequestWithBody requestWithBody = Unirest.post(url)
                .header("Content-Type", "application/json")
                .header("accept", "application/json");

        // basic auth
        if (basicAuthUsername != null && basicAuthPassword != null) {
            requestWithBody.basicAuth(basicAuthUsername, basicAuthPassword);
        }

        // body
        RequestBodyEntity requestBodyEntity = requestWithBody.body(bodyMap);

        HttpResponse<JsonNode> jsonNodeHttpResponse;
        try {
            jsonNodeHttpResponse = requestBodyEntity.asJson();
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }

        return Util.convertStreamToString(jsonNodeHttpResponse.getRawBody(), "UTF-8");
    }

    public static List<GraphQLTypeDescription> buildTypes(String input) {
        com.fasterxml.jackson.databind.JsonNode response;
        try {
            response = objectMapper.readTree(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        com.fasterxml.jackson.databind.JsonNode typesNode = response.path("data").path("__schema");

        if (typesNode.path("types").isMissingNode()) {
            throw new RuntimeException("data.__schema.types is missing");
        }
        try {
            return objectMapper.treeToValue(typesNode, SchemaResponse.class).getTypes();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String introspectionQuery() {
        return "query IntrospectionQuery { \n" +
                "  __schema { \n" +
                "    queryType { name } \n" +
                "    mutationType { name } \n" +
                "    subscriptionType { name } \n" +
                "    types { ...FullType } \n" +
                "    directives { \n" +
                "      name \n" +
                "      description \n" +
                "      args { ...InputValue } \n" +
                "      onOperation \n" +
                "      onFragment \n" +
                "      onField \n" +
                "    } \n" +
                "  } \n" +
                "} \n" +
                "\n" +
                "fragment FullType on __Type { \n" +
                "  kind \n" +
                "  name \n" +
                "  description \n" +
                "  fields(includeDeprecated: true) { \n" +
                "    name \n" +
                "    description \n" +
                "    args { ...InputValue } \n" +
                "    type { ...TypeRef } \n" +
                "    isDeprecated \n" +
                "    deprecationReason \n" +
                "  } \n" +
                "  inputFields { ...InputValue } \n" +
                "  interfaces { ...TypeRef } \n" +
                "  enumValues(includeDeprecated: true) { \n" +
                "    name \n" +
                "    description \n" +
                "    isDeprecated \n" +
                "    deprecationReason \n" +
                "  } \n" +
                "  possibleTypes { ...TypeRef } \n" +
                "} \n" +
                "fragment InputValue on __InputValue { \n" +
                "  name \n" +
                "  description \n" +
                "  type { ...TypeRef } \n" +
                "  defaultValue \n" +
                "} \n" +
                "fragment TypeRef on __Type { \n" +
                "  kind \n" +
                "  name \n" +
                "  ofType { \n" +
                "    kind \n" +
                "    name \n" +
                "    ofType { \n" +
                "      kind \n" +
                "      name \n" +
                "      ofType { \n" +
                "        kind \n" +
                "        name \n" +
                "      } \n" +
                "    } \n" +
                "  } \n" +
                "}\n";
    }

    public static void writeToFile(String outputFilePath, String generated) {
        File outputFile = new File(outputFilePath);

        //create dirs if not exist
        if (outputFile.getParent() != null && !new File(outputFile.getParent()).exists()) {
            File parent = new File(outputFile.getParent());
            parent.mkdirs();
        }
        //write
        try (PrintWriter output = new PrintWriter(outputFile, Charset.forName("UTF-8").name())) {
            output.print(generated);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            System.out.println("Error while writing to file :" + e);
            throw new RuntimeException(e);
        }
    }
}
