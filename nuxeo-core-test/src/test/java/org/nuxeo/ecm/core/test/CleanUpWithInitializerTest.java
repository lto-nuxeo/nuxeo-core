/*
 * (C) Copyright 2006-2009 Nuxeo SA (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     Damien Metzler (Leroy Merlin, http://www.leroymerlin.fr/)
 */
package org.nuxeo.ecm.core.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryCleanup;
import org.nuxeo.ecm.core.test.annotations.RepositoryInitializer;

import com.google.inject.Inject;

@RunWith(NuxeoCoreRunner.class)
@RepositoryCleanup(Granularity.METHOD)
@RepositoryInitializer(DefaultRepositoryInit.class)
public class CleanUpWithInitializerTest {

    @Inject
    CoreSession session;

    @Test
    public void iCreateADoc() throws Exception {
        DocumentModel doc = session.createDocumentModel(
                "/default-domain/workspaces/", "myWorkspace", "Workspace");
        doc.setProperty("dublincore", "title", "My Workspace");
        doc = session.createDocument(doc);
        session.saveDocument(doc);
        session.save();
        assertTrue(session.exists(new PathRef(
                "/default-domain/workspaces/myWorkspace")));
    }

    @Test
    public void myWorkspaceIsNotHereAnymore() throws Exception {
        assertTrue(session.exists(new PathRef("/default-domain/workspaces/")));
        assertFalse(session.exists(new PathRef(
                "/default-domain/workspaces/myWorkspace")));
    }

}