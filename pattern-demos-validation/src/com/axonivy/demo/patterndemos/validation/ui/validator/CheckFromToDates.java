package com.axonivy.demo.patterndemos.validation.ui.validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.ValidatorException;

import com.axonivy.demo.patterndemos.validation.Constants;
import com.axonivy.demo.patterndemos.validation.ui.ServerSideValidationCtrl;

import ch.ivyteam.ivy.environment.Ivy;

@FacesValidator("checkFromToDates")
public class CheckFromToDates extends BaseValidatorSimple<ServerSideValidationCtrl>{

	@Override
	protected void validate(ServerSideValidationCtrl bean, FacesContext context, UIComponent component, Object value) {
		List<FacesMessage> listFacesMessages = new ArrayList<>();

		var fromDate = bean.getFromDate();
		var toDate = bean.getToDate();
		if(fromDate != null && toDate!= null && fromDate.isAfter(toDate)) {
			var fromAfterToMessage = Ivy.cms().co("/Validation/fromAfterToDate", Arrays.asList(toDefaultString(fromDate), toDefaultString(toDate)));
			listFacesMessages.add(new FacesMessage(FacesMessage.SEVERITY_ERROR, fromAfterToMessage, fromAfterToMessage));
		}


		if (!listFacesMessages.isEmpty()) {
			throw new ValidatorException(listFacesMessages);
		}
	}

	/**
	 * Format a {@link LocalDate} with default pattern.
	 *
	 * @param localDate
	 * @return String
	 */
	public String toDefaultString(LocalDate localDate) {
		return localDate == null ? "" : localDate.format(DateTimeFormatter.ofPattern(Constants.DATE_PATTERN));
	}


}
