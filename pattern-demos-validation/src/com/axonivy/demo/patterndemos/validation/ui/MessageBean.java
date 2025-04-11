package com.axonivy.demo.patterndemos.validation.ui;

import java.util.List;

import javax.faces.bean.ManagedBean;

import ch.ivyteam.ivy.environment.Ivy;

@ManagedBean(name = "messages")
public class MessageBean {

	private static final MessageBean INSTANCE = new MessageBean();

	/**
	 * Get service instance.
	 *
	 * @return Instance of the class
	 */
	public static MessageBean get() {
		return INSTANCE;
	}

	/**
	 * Get message for required field.
	 *
	 * @param cmsOrText if starting with a slash, then the text is interpreted as a CMS path
	 * @return
	 */
	public String requiredMessage(String cmsOrText) {
		return labelMessage("/Validation/isRequired", cmsOrText);
	}

	/**
	 * Get message with a label prefix.
	 *
	 * Gets any message from the CMS and expects one parameter which is the label of a widget.
	 *
	 * @param validatorCms
	 * @param cmsOrText
	 * @return
	 */
	public String labelMessage(String validatorCms, String cmsOrText) {
		return Ivy.cms().co(validatorCms, List.of(cmsOrText(cmsOrText)));
	}

	private String cmsOrText(String text) {
		return text != null && text.charAt(0) == '/' ? Ivy.cms().co(text) : text;
	}
}
