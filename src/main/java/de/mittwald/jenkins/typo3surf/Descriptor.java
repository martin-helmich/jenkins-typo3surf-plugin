package de.mittwald.jenkins.typo3surf;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

@Extension
public final class Descriptor extends BuildStepDescriptor<Builder>
{
	private String	surfPath	= "/usr/bin/surf";
	private String	surfContext	= "Production";

	public FormValidation doCheckSurfPath(@QueryParameter String value)
			throws IOException, ServletException
	{
		if (new File(value + "/flow").canExecute() == false)
		{
			return FormValidation.error("No TYPO3 Surf installation found at "
					+ value);
		}
		return FormValidation.ok();
	}

	public FormValidation doCheckSurfContext(@QueryParameter String value)
			throws IOException, ServletException
	{
		if (!value.equals("Development") && !value.equals("Production"))
		{
			return FormValidation.error("Invalid TYPO3 Flow context: " + value);
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
	public boolean configure(StaplerRequest req, JSONObject formData)
			throws FormException
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
