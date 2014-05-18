/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.formio.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import net.formio.FormField;
import net.formio.FormMapping;
import net.formio.validation.ConstraintViolationMessage;

public class FormFiller {
	
	private final Container formFieldsContainer;
	private final Color errorColor = Color.orange;
	private Color defaultColor;
	
	public FormFiller(Container formFieldsContainer) {
		if (formFieldsContainer == null) throw new IllegalArgumentException("formFieldsContainer cannot be null");
		this.formFieldsContainer = formFieldsContainer;
	}
	
	public Component findComponentByName(String name) {
		Component component = null;
		Component[] components = formFieldsContainer.getComponents();
		if (components != null) {
			for (Component c : components) {
				if (c.getName() != null && !c.getName().isEmpty() && c.getName().equals(name)) {
					component = c;
					break;
				}
			}
		}
		return component;
	}
	
	public <T> void fillFormComponents(FormMapping<T> filledForm) {
		fillFormComponentsFromMapping(filledForm);
	}
	
	public void addTextField(FormField formField, ResourceBundle msgBundle) {
		StringBuilder lblText = new StringBuilder();
		if (msgBundle != null) {
			lblText.append(msgBundle.getString(formField.getLabelKey()));
		} else {
			lblText.append(formField.getLabelKey());
		}
		lblText.append(":");
		if (formField.isRequired()) {
			lblText.append(" *");
		}
		
		JLabel label = new JLabel(lblText.toString());
		JTextField field = new JTextField();
		field.setName(formField.getName());
		formFieldsContainer.add(label);
		formFieldsContainer.add(field);
	}
	
	private <T> void fillFormComponentsFromMapping(FormMapping<T> mapping) {
		for (FormField field : mapping.getFields().values()) {
			JComponent c = (JComponent)findComponentByName(field.getName());
			if (c != null) {
				if (defaultColor == null) {
					defaultColor = c.getBackground();
				}
				
				Set<ConstraintViolationMessage> msgs = mapping.getValidationResult().getFieldMessages().get(field.getName());
				if (msgs != null && !msgs.isEmpty()) {
					c.setBackground(errorColor);
					c.setToolTipText(messagesAsString(msgs));
					c.requestFocus();
				} else {
					c.setBackground(defaultColor);
					c.setToolTipText(null);
				}
				if (c instanceof JTextComponent) {
					JTextComponent jtc = (JTextComponent)c;
					jtc.setText(field.getValue());
				}
			}
		}
		for (FormMapping<?> m : mapping.getNested().values()) {
			fillFormComponentsFromMapping(m);
		}
	}
	
	private String messagesAsString(Set<ConstraintViolationMessage> msgs) {
		StringBuilder sb = new StringBuilder();
		if (msgs != null) {
			for (ConstraintViolationMessage m : msgs) {
				if (sb.length() > 0) {
					sb.append(" \n");
				}
				sb.append(m.getText());
			}
		}
		return sb.toString();
	}
}
