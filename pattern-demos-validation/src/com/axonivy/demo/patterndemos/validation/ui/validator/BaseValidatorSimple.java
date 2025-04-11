package com.axonivy.demo.patterndemos.validation.ui.validator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

public abstract class BaseValidatorSimple<B> implements Validator {

	private String beanId;

	public BaseValidatorSimple() {
		this("bean");
	}

	public BaseValidatorSimple(String beanId) {
		this.beanId = beanId;
	}

	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		if(component.getAttributes().get("isValidationDisabled") != null && Boolean.valueOf(component.getAttributes().get("isValidationDisabled").toString())) {
			return;
		}

		@SuppressWarnings("unchecked")
		var bean = (B) component.getAttributes().get(beanId);
		validate(bean, context, component, value);
	}

	protected abstract void validate(B bean, FacesContext context, UIComponent component, Object value);
}
