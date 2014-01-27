/*******************************************************************************
 * Copyright (c) 2013 Pivotal Software, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Pivotal Software, Inc. - initial API and implementation
 *******************************************************************************/
package org.cloudfoundry.ide.eclipse.internal.server.ui.wizards;

import org.cloudfoundry.ide.eclipse.internal.server.core.application.IApplicationDelegate;

/**
 * 
 * This contains a reference to the core-level application delegate, which
 * contains API to push an application to a CF server. Since a wizard delegate
 * does NOT require a map to a core level application delegate, the link between
 * the two is not pushed up to the parent.
 */
public abstract class ApplicationWizardDelegate implements IApplicationWizardDelegate {

	private IApplicationDelegate appDelegate;

	public void setApplicationDelegate(IApplicationDelegate appDelegate) {
		this.appDelegate = appDelegate;
	}

	/**
	 * Corresponding core level application delegate that contains API for
	 * pushing an app to a CF server. This may be null, as a wizard delegate may
	 * not be mapped to an app delegate (in the event it uses a default app
	 * delegate from the CF Application framework) .
	 * @return Corresponding Application delegate, if it exists, or null.
	 */
	public IApplicationDelegate getApplicationDelegate() {
		return appDelegate;
	}

}
