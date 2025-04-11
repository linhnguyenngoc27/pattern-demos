package com.axonivy.demo.patterndemos.placeholder.ui;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.event.ActionEvent;

import com.axonivy.demo.patterndemos.placeholder.service.ReplacementService;

public class PlaceholderDemoCtrl {
	private String template =
			"""
			A task was assigned to you on {{created}}.

			Please finish the task '{{title}}' until {{until}}!

			Thank you! 
			""";
	private String text;
	private List<StringPair> entries = List.of(
			new StringPair("title", "Please finish your time report."),
			new StringPair("created", LocalDate.now().toString()),
			new StringPair("until", LocalDate.now().plusDays(7).toString()),
			new StringPair("", ""),
			new StringPair("", "")
			);


	public void replace(ActionEvent event) {
		var replacement = entries.stream().collect(
				Collectors.toMap(StringPair::getKey, StringPair::getValue, (o, n) -> n));

		text = ReplacementService.get().replacePlaceholders(template, replacement);
	}


	public String getTemplate() {
		return template;
	}


	public void setTemplate(String template) {
		this.template = template;
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}


	public List<StringPair> getEntries() {
		return entries;
	}


	public void setEntries(List<StringPair> entries) {
		this.entries = entries;
	}


	public class StringPair {
		private String key;
		private String value;

		public StringPair(String key, String value) {
			this.key = key;
			this.value = value;
		}
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
	}
}
