package io.swagger.v3.core.issues;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * Reproduces https://github.com/swagger-api/swagger-core/issues/5292: resolving a densely
 * cross-referenced, cyclic third-party bean living under a "system" package - {@code javax.jcr.*} is
 * treated as a JDK package by {@link io.swagger.v3.core.util.ReflectionUtils#isSystemType}, so it's
 * never assigned a component name and can never be collapsed to a {@code $ref} - used to exhaust the
 * heap.
 * <p>
 * Root cause: with no component name, the resolver's cache still correctly reuses, by reference, the
 * one already-built Schema instance for a given class across every differently-named property that
 * reaches it - but the resolver then had to deep-clone that shared instance (via a JSON round-trip) to
 * give each property occurrence its own {@code name}. Cloning a value that is itself shared/cyclic this
 * way re-expands every shared subtree at every occurrence, which blows up combinatorially with depth -
 * see the fix in {@code ModelResolver.cloneResolvedProperty()}.
 * <p>
 * This is a different mechanism than https://github.com/swagger-api/swagger-core/issues/5091 (fixed by
 * #5114): that one was a resolve()-call-count explosion; this one is a single-value size explosion - the
 * number of resolve() calls stays modest (bounded assertions below), but the schema <em>object graph</em>
 * one of those calls builds is what blows up once something (here, the resolver's own per-property
 * relabeling clone) tries to deep-copy or serialize it.
 * <p>
 * Known residual limitation, out of scope for this fix: the schema this resolves to is still, internally,
 * a graph with shared/cyclic object references (that's unavoidable without giving javax.jcr.* types a
 * component name, which would defeat the "system types are inlined, not $ref'd" convention). Handing that
 * result to a generic JSON serializer - e.g. {@code Json.pretty()} on the resolved schema directly, or on
 * a full OpenAPI document that embeds it - can still blow up the same way, since ordinary Jackson bean
 * serialization has no notion of shared references and re-expands every occurrence. Guarding against that
 * would mean changing general-purpose Schema serialization itself, not just the resolver's internal
 * bookkeeping, so it isn't exercised here.
 */
public class Issue5292Test {

    @Test
    public void resolvingCyclicUnnamedSystemPackageBeanDoesNotExplode() {
        assertResolvesQuickly(javax.jcr.Node.class);
        assertResolvesQuickly(javax.jcr.Session.class);
        assertResolvesQuickly(javax.jcr.Workspace.class);
        assertResolvesQuickly(javax.jcr.Item.class);
    }

    @Test
    public void resolvingCyclicUnnamedSystemPackageBeanDoesNotExplodeWhenResolvedAsRef() {
        // resolveAsRef(true) is what the real io.swagger.v3.jaxrs2.Reader entry point uses for a
        // response/request body type - the issue also reported the OOM reproducing through Reader.
        long start = System.currentTimeMillis();
        ResolvedSchema resolved = ModelConverters.getInstance()
                .resolveAsResolvedSchema(new AnnotatedType(javax.jcr.Node.class).resolveAsRef(true));
        long elapsed = System.currentTimeMillis() - start;

        assertTrue(resolved.schema != null, "expected a schema to be resolved for javax.jcr.Node");
        assertTrue(elapsed < 10_000,
                "resolving javax.jcr.Node (resolveAsRef=true) took " + elapsed
                        + "ms - see https://github.com/swagger-api/swagger-core/issues/5292");
    }

    private void assertResolvesQuickly(Class<?> type) {
        long start = System.currentTimeMillis();
        ModelConverters.getInstance().readAll(type);
        long elapsed = System.currentTimeMillis() - start;

        assertTrue(elapsed < 10_000,
                "resolving " + type.getName() + " took " + elapsed
                        + "ms - see https://github.com/swagger-api/swagger-core/issues/5292");
    }
}
