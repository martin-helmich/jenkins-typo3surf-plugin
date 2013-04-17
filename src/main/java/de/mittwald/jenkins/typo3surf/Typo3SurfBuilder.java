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


import hudson.EnvVars;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.tasks.Builder;
import hudson.util.ArgumentListBuilder;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;



/**
 * Builder for TYPO3 Surf deployments.
 *
 * @author Martin Helmich <m.helmich@mittwald.de></m.helmich@mittwald.de>
 */
public class Typo3SurfBuilder extends Builder
{

    protected final String deployment;


    @Extension
    public static final class DescriptorImpl extends Descriptor
    {
    }



    // Fields in config.jelly must match the parameter names in the
    // "DataBoundConstructor"
    @DataBoundConstructor
    public Typo3SurfBuilder(String deployment)
    {
        this.deployment = deployment;
    }



    public String getDeployment()
    {
        return deployment;
    }



    protected boolean deploymentExists(PrintStream out)
    {
        try
        {
            Process process = new ProcessBuilder(getDescriptor().getSurfExecutable(), "surf:list", "--quiet").start();
            BufferedReader bri = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            boolean found = false;

            while ((line = bri.readLine()) != null)
            {
                if (line.equals(deployment))
                {
                    found = true;
                    break;
                }
            }
            bri.close();

            return found;
        }
        catch (Exception e)
        {
            out.println("Error on surf:list " + e.getMessage());
            return false;
        }
    }



    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher,
                           BuildListener listener) throws InterruptedException, IOException
    {
        if (!deploymentExists(listener.getLogger()))
        {
            listener.fatalError("Deployment " + deployment + " does not exist!");
            return false;
        }

        ArgumentListBuilder args = new ArgumentListBuilder();
        EnvVars env = build.getEnvironment(listener);

        env.put("FLOW_CONTEXT", getDescriptor().getSurfContext());
        env.put("FLOW_ROOTPATH", getDescriptor().getSurfPath());

        args.add(getDescriptor().getSurfExecutable(), "surf:deploy", "--disable-ansi", deployment);

        try
        {
            int result = launcher.launch().cmds(args).envs(env).stdout(listener.getLogger()).join();
            return result == 0;
        }
        catch (IOException e)
        {
            listener.fatalError(e.getMessage());
            return false;
        }
    }



    // Overridden for better type safety.
    // If your plugin doesn't really define any property on Descriptor,
    // you don't have to do this.
    @Override
    public Descriptor getDescriptor()
    {
        return (Descriptor) super.getDescriptor();
    }


}
