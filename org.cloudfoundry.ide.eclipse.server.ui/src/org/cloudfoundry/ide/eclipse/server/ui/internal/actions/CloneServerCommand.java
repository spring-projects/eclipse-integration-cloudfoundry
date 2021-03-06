/*******************************************************************************
 * Copyright (c) 2014 Pivotal Software, Inc. 
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, 
 * Version 2.0 (the "License�) you may not use this file except in compliance 
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 *  Contributors:
 *     Keith Chong, IBM - Support more general branded server type IDs via org.eclipse.ui.menus
 ********************************************************************************/
package org.cloudfoundry.ide.eclipse.server.ui.internal.actions;

import org.cloudfoundry.ide.eclipse.server.core.internal.CloudFoundryPlugin;
import org.cloudfoundry.ide.eclipse.server.core.internal.CloudFoundryServer;
import org.cloudfoundry.ide.eclipse.server.ui.internal.CloudUiUtil;
import org.cloudfoundry.ide.eclipse.server.ui.internal.wizards.OrgsAndSpacesWizard;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.progress.UIJob;

public class CloneServerCommand extends BaseCommandHandler {

	private IWorkbenchPart activePart;
	
	public Object execute(ExecutionEvent event) throws ExecutionException {
		initializeSelection(event);
		activePart = HandlerUtil.getActivePart(event);
		
		String error = null;
		CloudFoundryServer cloudServer = selectedServer != null ? (CloudFoundryServer) selectedServer.loadAdapter(
				CloudFoundryServer.class, null) : null;
		if (selectedServer == null) {
			error = "No Cloud Foundry server instance available to run the selected action."; //$NON-NLS-1$
		}

		if (error == null) {
			doRun(cloudServer);
		}
		else {
			CloudFoundryPlugin.logError(error);
		}
		
		return null;
	}
	
	private String getJobName() {
		return "Cloning server to selected space"; //$NON-NLS-1$
	}
	
	public void doRun(final CloudFoundryServer cloudServer) {
		final Shell shell = activePart != null && activePart.getSite() != null ? activePart.getSite().getShell()
				: CloudUiUtil.getShell();

		if (shell != null) {
			UIJob job = new UIJob(getJobName()) {

				public IStatus runInUIThread(IProgressMonitor monitor) {
					OrgsAndSpacesWizard wizard = new OrgsAndSpacesWizard(cloudServer);
					WizardDialog dialog = new WizardDialog(shell, wizard);
					dialog.open();

					return Status.OK_STATUS;
				}
			};
			job.setSystem(true);
			job.schedule();
		}
		else {
			CloudFoundryPlugin.logError("Unable to find an active shell to open the orgs and spaces wizard."); //$NON-NLS-1$
		}

	}


}
