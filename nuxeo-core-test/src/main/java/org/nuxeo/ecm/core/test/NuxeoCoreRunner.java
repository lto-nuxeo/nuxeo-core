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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.test.guice.CoreModule;
import org.nuxeo.runtime.test.runner.NuxeoRunner;
import org.nuxeo.runtime.test.runner.RuntimeModule;

import com.google.inject.Module;
import com.google.inject.Provider;

/**
 * JUni4 runner that provide facilities to setup {@link CoreSession}-based
 * tests.
 */
public class NuxeoCoreRunner extends NuxeoRunner implements Provider<RepoType> {

    private static final Log log = LogFactory.getLog(NuxeoCoreRunner.class);

    private Settings settings;

    private RepoType type;

    public NuxeoCoreRunner(Class<?> classToRun) throws InitializationError {
        this(classToRun, new RuntimeModule(), new CoreModule());
    }

    public NuxeoCoreRunner(Class<?> classToRun, Module... modules)
            throws InitializationError {
        super(classToRun, modules);
        settings = new Settings(getDescription());
    }

    public void setRepoType(RepoType type) {
        this.type = type;
    }

    public static Settings getSettings() {
        return ((NuxeoCoreRunner) getInstance()).settings;
    }

    @Override
    public void beforeRun() {
        if (settings.getCleanUpLevel() == Level.CLASS) {
            cleanupSession();
        }
    }

    protected void cleanupSession() {
        CoreSession session = injector.getInstance(CoreSession.class);
        try {
            session.removeChildren(new PathRef("/"));
        } catch (ClientException e1) {
            System.err.println("Unable to reset repository");
        }
        RepoFactory factory = settings.getRepoFactory();
        if (factory != null) {
            try {
                factory.createRepo(session);
                session.save();
            } catch (ClientException e) {
                log.error(e.toString(), e);
            }
        }
    }

    @Override
    protected Statement methodInvoker(FrameworkMethod method, Object test) {
        Statement statement = super.methodInvoker(method, test);
        if (settings.getCleanUpLevel() == Level.METHOD) {
            cleanupSession();
        }
        return statement;
    }

    public RepoType get() {
        if (this.type == null) {
            // Case the type is specified by the test class
            return this.settings.getRepoType();
        } else {
            // MultiRepo case
            return type;
        }
    }

}