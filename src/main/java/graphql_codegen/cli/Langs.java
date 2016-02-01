package graphql_codegen.cli;

import io.airlift.airline.Command;

@Command(name = "langs", description = "Shows available langs")
public class Langs implements Runnable {
    @Override
    public void run() {
        System.out.printf("Available languages: %s", "java");
    }
}