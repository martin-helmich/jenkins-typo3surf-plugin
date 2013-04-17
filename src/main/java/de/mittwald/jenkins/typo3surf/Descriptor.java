package de.mittwald.jenkins.typo3surf;


/*
 * TYPO3 Surf Builder for Jenkins
 * Copyright (C) 2013  Martin Helmich <m.helmich@mittwald.de>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;



/**
 * Descriptor class for TYPO3 Surf build steps.
 *
 * @author Martin Helmich <m.helmich@mittwald.de>
 */
public class Descriptor extends BuildStepDescriptor<Builder>
{
    /**
     * Path to TYPO3 Surf installation (root directory, NOT the executable!)
     */
    protected String surfPath = "/var/surf";


    /**
     * TYPO3 Flow context to be used for Surf execution.
     */
    protected String surfContext = "Production";



    /**
     * Validate the TYPO3 Surf path.
     *
     * Checks if a "flow" executable exists in the given path and if the TYPO3.Surf package is installed.
     *
     * @param value The given TYPO3 Surf path.
     * @return OK, if the given path is a valid TYPO3 Surf installation, otherwise ERROR.
     *
     * @throws IOException
     * @throws ServletException
     */
    public FormValidation doCheckSurfPath(@QueryParameter String value)
            throws IOException, ServletException
    {
        if (!new File(value + "/flow").canExecute())
        {
            return FormValidation.error("No TYPO3 Flow installation found at " + value);
        }
        if (!new File(value + "/Packages/Application/TYPO3.Surf").isDirectory())
        {
            return FormValidation.error(
                    value + " contains a TYPO3 Flow installation, but the TYPO3.Surf package is not installed!");
        }
        return FormValidation.ok();
    }



    /**
     * Validates the TYPO3 Flow context (must be either "Production" or "Development").
     *
     * @param value The Flow context to validate.
     * @return OK if valid, otherwise ERROR.
     *
     * @throws IOException
     * @throws ServletException
     */
    public FormValidation doCheckSurfContext(@QueryParameter String value) throws IOException, ServletException
    {
        if (!value.equals("Development") && !value.equals("Production"))
        {
            return FormValidation.error("Invalid TYPO3 Flow context: "
                                        + value);
        }
        return FormValidation.ok();
    }



    @SuppressWarnings("rawtypes")
    public boolean isApplicable(Class<? extends AbstractProject> aClass)
    {
        return true;
    }



    public String getDisplayName()
    {
        return "TYPO3 Surf Deployment";
    }



    @Override
    public boolean configure(StaplerRequest req, JSONObject formData) throws FormException
    {
        surfPath = formData.getString("surfPath");
        surfContext = formData.getString("surfContext");

        save();

        return super.configure(req, formData);
    }



    public String getSurfPath()
    {
        return surfPath;
    }



    public String getSurfExecutable()
    {
        return surfPath + "/flow";
    }



    public String getSurfContext()
    {
        return surfContext;
    }
}