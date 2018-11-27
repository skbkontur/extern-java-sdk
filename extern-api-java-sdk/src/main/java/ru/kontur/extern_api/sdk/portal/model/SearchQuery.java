package ru.kontur.extern_api.sdk.portal.model;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Use static methods to create search query acceptable for kontur services.
 * Resulting search query looks like {@code (((a="1")or(b="2"))and(c="3"))}
 */
public class SearchQuery implements Token {

    private Operation operation;

    private SearchQuery(Operation operation) {
        this.operation = operation;
    }

    /**
     * @return {@code (paramName="value")}
     */
    @NotNull
    public static SearchQuery equal(String paramName, String value) {
        return new SearchQuery(new Operation("=", new Primitive(paramName), new Primitive('"' + value + '"')));
    }

    /**
     * @return {@code (query or query or query ...)}
     */
    @NotNull
    public static SearchQuery or(SearchQuery... queries) {
        return or(Arrays.stream(queries));
    }

    /**
     * @return {@code (query and query and query ...)}
     */
    @NotNull
    public static SearchQuery and(SearchQuery... queries) {
        return and(Arrays.stream(queries));
    }

    /**
     * @see SearchQuery#or(SearchQuery...)
     */
    @NotNull
    public static SearchQuery or(Stream<SearchQuery> queries) {
        return createOperation("or", queries);
    }

    /**
     * @see SearchQuery#and(SearchQuery...)
     */
    @NotNull
    public static SearchQuery and(Stream<SearchQuery> queries) {
        return createOperation("and", queries);
    }

    /**
     * @return query matching fieldName any of examples
     */
    @NotNull
    public static SearchQuery searchAll(String fieldName, Collection<String> examples) {
        return searchAll(fieldName, examples.stream());
    }

    /**
     * @see SearchQuery#searchAll(String, Collection)
     */
    @NotNull
    public static SearchQuery searchAll(String fieldName, Stream<String> examples) {
        return or(examples.map(example -> equal(fieldName, example)));
    }

    private static SearchQuery createOperation(String op, Stream<SearchQuery> queries) {
        return new SearchQuery(new Operation(op, queries.map(q -> q.operation).toArray(Token[]::new)));
    }

    @Override
    public String toString() {
        return operation.toString();
    }
}

interface Token {

    default String translate() {
        return toString();
    }

}

class Primitive implements Token {

    private String value;

    Primitive(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

class Operation implements Token {

    private final String op;

    private final Token[] tokens;

    Operation(String op, Token... tokens) {
        this.op = op;
        this.tokens = tokens;
    }

    @Override
    public String toString() {
        String joined = String.join(op, Stream.of(tokens)
                .map(Token::translate)
                .collect(Collectors.toList())
        );

        if (tokens.length <= 1)
            return joined;

        return "(" + joined + ")";
    }
}
