package org.xmlresolver;

import org.xmlresolver.cache.ResourceCache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * An individual resolver feature. The complete set of known features is instantiated as a
 * collection of static final fields.
 *
 * @param <T> The type of the feature.
 */

public class ResolverFeature<T> {
    private String name = null;
    private T defaultValue = null;

    private static final Map<String, ResolverFeature<?>> index = new TreeMap<String, ResolverFeature<?>>();

    protected ResolverFeature(String name, T defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
        index.put(name, this);
    }

    /**
     * Get the name of the feature.
     *
     * @return The feature name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the default value of the feature.
     *
     * @return The feature default value.
     */
    public T getDefaultValue() {
        return defaultValue;
    }

    /**
     * Find a known static feature by name.
     *
     * @param name The feature name.
     * @return The instance of that feature.
     */
    public static ResolverFeature<?> byName(String name) {
        return index.get(name);
    }

    /**
     * Iterates over all of the known feature names.
     *
     * @return An iterator over the feature names.
     */
    public static Iterator<String> getNames() {
        return index.keySet().iterator();
    }

    /**
     * Sets the list of catalog files.
     */
    public static final ResolverFeature<List<String>> CATALOG_FILES = new ResolverFeature<>(
            "http://xmlresolver.org/feature/catalog-files", Collections.unmodifiableList(new ArrayList<>()));

    /**
     * Sets the list of additional catalog files.
     */
    public static final ResolverFeature<List<String>> CATALOG_ADDITIONS = new ResolverFeature<>(
            "http://xmlresolver.org/feature/catalog-additions", Collections.unmodifiableList(new ArrayList<>()));

    /**
     * Determines whether or not public IDs are preferred..
     */
    public static final ResolverFeature<Boolean> PREFER_PUBLIC = new ResolverFeature<>(
            "http://xmlresolver.org/feature/prefer-public", true);

    /**
     * Determines whether property file values are preferred over
     * system property values.
     *
     * <p>In earlier versions of this API, this was effectively always true.
     * The default is now false which allows system property values to override property file values.
     * Set this to <code>true</code> in your property file to preserve the old behavior.</p>
     */
    public static final ResolverFeature<Boolean> PREFER_PROPERTY_FILE = new ResolverFeature<>(
            "http://xmlresolver.org/feature/prefer-property-file", false);

    /**
     * Determines whether or not the catalog PI in a document
     * may change the list of catalog files to be consulted.
     *
     * <p>It defaults to <code>true</code>, but there's a small performance cost. Each parse needs
     * it's own copy of the configuration if you enable this feature (otherwise, the PI in one document
     * might have an effect on other documents). If you know you aren't using the PI, it might be sensible
     * to make this <code>false</code>.</p>
     */
    public static final ResolverFeature<Boolean> ALLOW_CATALOG_PI = new ResolverFeature<>(
            "http://xmlresolver.org/feature/allow-catalog-pi", true);

    /**
     * Sets the location of the cache directory.
     *
     * <p>If the value
     * is <code>null</code>, and <code>CACHE_UNDER_HOME</code> is <code>false</code>, no cache will
     * be used.</p>
     */
    public static final ResolverFeature<String> CACHE_DIRECTORY = new ResolverFeature<>(
            "http://xmlresolver.org/feature/cache-directory", (String) null);

    /**
     * Determines if a default cache location of <code>.xmlresolver.org/cache</code>
     * under the users home directory should be used for the cache.
     *
     * <p>This only applies if <code>CATALOG_CACHE</code>
     * is <code>null</code>.</p>
     */
    public static final ResolverFeature<Boolean> CACHE_UNDER_HOME = new ResolverFeature<>(
            "http://xmlresolver.org/feature/cache-under-home", true);

    /**
     * Provides access to the {@link ResourceCache} that the resolver is using.
     */
    public static final ResolverFeature<ResourceCache> CACHE = new ResolverFeature<>(
            "http://xmlresolver.org/feature/cache", (ResourceCache) null);

    /**
     * Provides access to the {@link CatalogManager} that
     * the resolver is  using.
     */
    public static final ResolverFeature<CatalogManager> CATALOG_MANAGER = new ResolverFeature<>(
            "http://xmlresolver.org/feature/catalog-manager", (CatalogManager) null);

    /**
     * Determines whether or not <code>uri</code> catalog entries
     * can be used to resolve external identifiers.
     *
     * <p>This only applies if resolution fails through
     * system and public entries.</p>
     */
    public static final ResolverFeature<Boolean> URI_FOR_SYSTEM = new ResolverFeature<>(
            "http://xmlresolver.org/feature/uri-for-system", true);

    /**
     * Determines whether http: and https: URIs compare the same.
     *
     * <p>Historically, most web servers used <code>http:</code>, now most use <code>https:</code>.
     * There are existing catalogs that can't practically be updated that use <code>http:</code> for
     * system identifiers and URIs. But authors copying and pasting are likely to get <code>https:</code>
     * URIs. If this option is true, then <code>http:</code> and <code>https:</code> are considred
     * the same <em>for the purpose of comparison in the catalog</em>.</p>
     *
     * <p>This option has no effect on the URIs returned; it only influences catalog URI comparisons.</p>
     */
    public static final ResolverFeature<Boolean> MERGE_HTTPS = new ResolverFeature<>(
            "http://xmlresolver.org/feature/merge-https", true);

    /**
     * Determines whether a classpath: or jar: URI is returned by the resolver.
     *
     * <p>When the resolver finds a resource, for example a schema or a stylesheet, it returns
     * the location of the resolved resource as the base URI for the resource. This enables
     * the following common scenario:</p>
     *
     * <ul>
     *     <li>Download a distribution and store it locally.</li>
     *     <li>Create a catalog that maps from the entry point(s) into the local
     *     distribution: http://example.com/acme-schema/start/here -> /opt/acme-schema-1.0/here</li>
     *     <li>Profit.</li>
     * </ul>
     *
     * <p>A document requests <code>http://example.com/acme-schema/start/here</code>, the resolver
     * returns <code>/opt/achme-schema-1.0/here</code>. When the schema attempts to import a library,
     * the URI for that library is resolved against the base URI on the filesystem, a new path is
     * constructed, and it all just works.</p>
     *
     * <p>Adding support for <code>classpath</code> and <code>jar:</code> URIs to XML Resolver 3.0 has enabled another
     * very attractive scenario:</p>
     *
     * <ul>
     *     <li>Put the distribution in a jar file with an included catalog.</li>
     *     <li>Arrange for the project to depend on that jar file, so it'll be on the classpath.</li>
     *     <li>Add <code>classpath:/org/example/acme-schema/catalog.xml</code> to your catalog list.</li>
     *     <li>More profit!</li>
     * </ul>
     *
     * <p>Trouble is, the resolved URI will be something like this:</p>
     *
     * <p><code>jar:file:///where/the/jar/is/acme-schema-1.0.jar!/org/example/acme-schema/here</code></p>
     *
     * <p>And the trouble with that is, Java doesn't think the <code>classpath:</code> and <code>jar:</code>
     * URI schemes are hierarchical and won't resolve the URI for the imported library correctly. It will
     * work just fine for any document that doesn't include parts with relative URIs. The DocBook schema,
     * for example, is distributed as a single RELAX NG file, so it works. But the xslTNG stylesheets would
     * not.</p>
     *
     * <p>If <code>MASK_JAR_URIS</code> is true, the resolver will return the local resource from the jar
     * file, but will leave the URI unchanged. As long as the catalog has a mapping for all of the resources,
     * and not just the entry point(s), this will do exactly the right thing.</p>
     *
     * <p>Often, this can be achieved with, for example, a rewrite rule:</p>
     *
     * <pre>&lt;rewriteURI uriStartString="http://example.com/acme-start/"
     *             rewritePrefix="acme-start/"&gt;
     * </pre>
     *
     * <p>Assuming the catalog is in a location where that rewrite prefix works, the entry point
     * will be remapped and the local resource returned. The resource it imports will be resolved against
     * the http: URI, but that will also be remapped, and everyone wins.</p>
     *
     * <p>You don't need to use a rewrite rule, you can use any combination of catalog rules you like
     * as long as each of the requested URIs will be mapped.</p>
     */
    public static final ResolverFeature<Boolean> MASK_JAR_URIS = new ResolverFeature<>(
            "http://xmlresolver.org/feature/mask-jar-uris", true);
}
