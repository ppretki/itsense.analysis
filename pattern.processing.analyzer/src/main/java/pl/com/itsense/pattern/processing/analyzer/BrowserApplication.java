/*
 * Copyright 2009 IT Mill Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package pl.com.itsense.pattern.processing.analyzer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import org.hibernate.SessionFactory;


import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
public class BrowserApplication extends UI
{
	/** */
	public static final String VIEW_WORKSPACE = "Workspace";
	/** */
	public static final String CTX_ATTRIBUTE_HIBERNATE_SESSION_FACTORY = "HibernateSessionfactory";
	/** */
	private Navigator navigator;
	/** */
	public static SessionFactory sessionFactory;

	@Override
	protected void init(final VaadinRequest request) 
	{
		navigator = new Navigator(this, this);
		navigator.addView(VIEW_WORKSPACE, new WorkspaceView(navigator));
		navigator.navigateTo(VIEW_WORKSPACE);
	}
	
	/**
	 * 
	 * @author ppretki
	 *
	 */
	public static class Servlet extends VaadinServlet 
	{
		/** */
		private static final long serialVersionUID = 1L;
		/**
		 * 
		 */
		@Override
		protected void servletInitialized() throws ServletException 
		{
			final Object sessionFactoryObj = getServletContext().getAttribute(BrowserApplication.CTX_ATTRIBUTE_HIBERNATE_SESSION_FACTORY);
			if (sessionFactoryObj instanceof SessionFactory)
			{
				sessionFactory = (SessionFactory)sessionFactoryObj;		
			}
		}
	}

    
}
