package org.xmlresolver;

import java.io.InputStream;
import java.net.URI;

public abstract class ResolvedResource {
    public abstract URI getResolvedURI();
    public abstract URI getLocalURI();
    public abstract InputStream getInputStream();
    public abstract String getContentType();
}
