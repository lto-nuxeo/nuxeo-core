/*
 * Copyright (c) 2006-2011 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bogdan Stefanescu
 *     Florent Guillaume
 */

package org.nuxeo.ecm.core.schema;

import org.nuxeo.ecm.core.schema.types.CompositeType;
import org.nuxeo.ecm.core.schema.types.Schema;

/**
 * A provider of types (schemas, document types, facets).
 */
public interface TypeProvider {

    /** Gets a schema. */
    Schema getSchema(String schema);

    /** Gets the list of schemas. */
    Schema[] getSchemas();

    /** Gets a document type. */
    DocumentType getDocumentType(String docType);

    /** Gets the list of document types. */
    DocumentType[] getDocumentTypes();

    /** Gets a facet. */
    CompositeType getFacet(String name);

    /** Gets the list of facets. */
    CompositeType[] getFacets();

}
