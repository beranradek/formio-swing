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

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MainWindow extends JFrame {
	private static final long serialVersionUID = -8231290725078682892L;

	public MainWindow() {
		setTitle("Person form");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 200);
		
		FormPanel formPanel = new FormPanel();
		add(formPanel);
	}
	
	public static void main(String[] args) throws Exception {
		
		EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                	// ignored
                }

                MainWindow mainWindow = new MainWindow();
        		mainWindow.pack();
        		mainWindow.setSize(400, mainWindow.getHeight());
        		mainWindow.setVisible(true);
            }
        });
    }
}
