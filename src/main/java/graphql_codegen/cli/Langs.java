package graphql_codegen.cli;

import graphql_codegen.Code;
import io.airlift.airline.Command;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.ServiceLoader.load;

@Command(name = "langs", description = "Shows available langs")
public class Langs implements Runnable {
    @Override
    public void run() {
        String langs = StreamSupport.stream(load(Code.class).spliterator(), false)
                .map(Code::name)
                .collect(Collectors.joining(","));
        System.out.printf("Available languages: %s", langs);
    }
}