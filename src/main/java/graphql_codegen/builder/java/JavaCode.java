package graphql_codegen.builder.java;

import graphql_codegen.Code;

import java.util.HashMap;
import java.util.Map;

public abstract class JavaCode implements Code {

    protected final static Map<String, String> STANDARD_IMPORTS = new HashMap<>();

    static
    {
        STANDARD_IMPORTS.put("BigDecimal", "java.math.BigDecimal");
        STANDARD_IMPORTS.put("UUID", "java.util.UUID");
        STANDARD_IMPORTS.put("Date", "java.util.Date");
        STANDARD_IMPORTS.put("Timestamp", "java.sql.Timestamp");
        STANDARD_IMPORTS.put("ZonedDateTime", "java.time.ZonedDateTime");
        STANDARD_IMPORTS.put("OffsetDateTime", "java.time.OffsetDateTime");
        STANDARD_IMPORTS.put("OffsetTime", "java.time.OffsetTime");
        STANDARD_IMPORTS.put("Instant", "java.time.Instant");
        STANDARD_IMPORTS.put("LocalDateTime", "java.time.LocalDateTime");
        STANDARD_IMPORTS.put("LocalDate", "java.time.LocalDate");
        STANDARD_IMPORTS.put("LocalTime", "java.time.LocalTime");
        STANDARD_IMPORTS.put("Duration", "java.time.Duration");
        STANDARD_IMPORTS.put("List", "java.util.List");
        STANDARD_IMPORTS.put("Set", "java.util.Set");
    }

    public String extension() { return ".java"; }
}
