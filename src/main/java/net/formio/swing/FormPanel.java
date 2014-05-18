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

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.formio.FormData;
import net.formio.FormField;
import net.formio.FormMapping;
import net.formio.Forms;
import net.formio.swing.domain.Person;
import net.formio.validation.ValidationResult;

class FormPanel extends JPanel {
	private static final long serialVersionUID = 1566539977242052303L;
	
	private static final FormMapping<Person> personForm = Forms.basic(Person.class, "person")
		// whitelist of properties to bind
		.fields("firstName", "lastName", "phone", "salary")
		.build();
	
	private static ResourceBundle msgBundle = ResourceBundle.getBundle(Person.class.getName().replace(".", "/"));
	
	private JButton saveBtn;
	private JLabel statusLabel;
	private Person person;
	private final FormFiller formFiller;

	FormPanel() {
		this.formFiller = new FormFiller(this);
		this.person = initPerson();
		addFormFields();
		addSaveBtn();
		addStatusLabel();
		fillForm(new FormData<Person>(this.person, ValidationResult.empty));
	}
	
	protected void addFormFields() {
		Map<String, FormField> fields = personForm.getFields();
		this.formFiller.addTextField(fields.get("firstName"), msgBundle);
		this.formFiller.addTextField(fields.get("lastName"), msgBundle);
		this.formFiller.addTextField(fields.get("phone"), msgBundle);
		this.formFiller.addTextField(fields.get("salary"), msgBundle);
		
		GridLayout layout = new GridLayout(personForm.getFields().size() + 1, 2);
		setLayout(layout);
	}
	
	protected void addSaveBtn() {
		this.saveBtn = new JButton("Save");
		this.saveBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onSaveBtnClicked(e);
			}
		});
		add(this.saveBtn);
	}
	
	protected void onSaveBtnClicked(ActionEvent e) {
		this.statusLabel.setText("");
		FormData<Person> formData = personForm.bind(new SwingRequestParams(this));
		if (formData.isValid()) {
			// save the person
			this.person = formData.getData();
			formData = new FormData<Person>(this.person, ValidationResult.empty);
			this.statusLabel.setText("Successfully saved.");
		} else {
			this.statusLabel.setText("Form contains validation errors.");
		}
		fillForm(formData);
	}
	
	private void addStatusLabel() {
		this.statusLabel = new JLabel();
		add(this.statusLabel);
	}
	
	private Person initPerson() {
		return new Person("Johny", "Cash");
	}
	
	private void fillForm(FormData<Person> formData) {
		this.formFiller.fillFormComponents(personForm.fill(formData));
	}
}
