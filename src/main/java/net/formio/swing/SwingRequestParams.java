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

import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.text.JTextComponent;

import net.formio.upload.RequestProcessingError;
import net.formio.upload.UploadedFile;

public class SwingRequestParams extends AbstractRequestParams {

	private final Container container;
	
	public SwingRequestParams(final Container formFieldsContainer) {
		if (formFieldsContainer == null) throw new IllegalArgumentException("formFieldsContainer cannot be null");
		this.container = formFieldsContainer;
	}
	
	@Override
	public Iterable<String> getParamNames() {
		List<String> names = new ArrayList<String>();
		for (Component c : container.getComponents()) {
			if (c.getName() != null && !c.getName().isEmpty()) {
				names.add(c.getName());
			}
		}
		return Collections.unmodifiableList(names);
	}

	@Override
	public String[] getParamValues(String paramName) {
		List<String> values = new ArrayList<String>();
		for (Component c : container.getComponents()) {
			if (c.getName() != null && !c.getName().isEmpty() && c.getName().equals(paramName)) {
				if (c instanceof JTextComponent) {
					values.add(((JTextComponent)c).getText());
				}
			}
		}
		return values.toArray(new String[0]);
	}

	@Override
	public UploadedFile[] getUploadedFiles(String paramName) {
		return new UploadedFile[0];
	}

	@Override
	public RequestProcessingError getRequestError() {
		return null;
	}

}
