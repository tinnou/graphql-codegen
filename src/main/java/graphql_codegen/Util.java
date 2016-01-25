package graphql_codegen;

import com.samskivert.mustache.Mustache;

import java.io.*;
import java.net.URISyntaxException;

public class Util {

    public static String capitalizeFirstLetter(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public static String code(Object context, String fileName) {
        InputStream in = Thread.currentThread().getClass().getResourceAsStream("/Java/"+ fileName);
        Reader reader = new InputStreamReader(in);

        Writer writer = new StringWriter();

        // allowed loading sub template
        final File templateDir;
        try {
            templateDir = new File(Thread.currentThread().getClass().getResource("/Java/").toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        Mustache.Compiler c = Mustache.compiler().withLoader(new Mustache.TemplateLoader() {
            public Reader getTemplate (String name) {
                try {
                    return new FileReader(new File(templateDir, name + ".mustache"));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        c.compile(reader).execute(context, writer);

        return writer.toString();
    }
}
