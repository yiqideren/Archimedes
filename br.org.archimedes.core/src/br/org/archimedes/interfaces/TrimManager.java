/**
 * Copyright (c) 2008, 2009 Hugo Corbucci and others.<br>
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html<br>
 * <br>
 * Contributors:<br>
 * Cecilia Fernandes - initial API and implementation<br>
 * Jonas K. Hirata, Hugo Corbucci - later contributions<br>
 * <br>
 * This file was created on 2008/06/05, 09:46:26, by Cecilia Fernandes.<br>
 * It is part of package br.org.archimedes.interfaces on the br.org.archimedes.core project.<br>
 */
package br.org.archimedes.interfaces;

import java.util.Collection;

import br.org.archimedes.exceptions.NullArgumentException;
import br.org.archimedes.model.Element;
import br.org.archimedes.model.Point;

/**
 * Belongs to package br.org.archimedes.interfaces.
 * 
 * @author ceci
 */
public interface TrimManager {

	/**
     * Returns the result of trimming the element (a collection of elements).
     * 
     * @param element
     *            The element to be trimmed.
     * @param cutPoints
     *            Points defining where to cut the element
     * @param click
     * 			  Point where user clicked to choose which parts of the element shall be trimmed
     * @return The collection of trimmed elements.
     * @throws NullArgumentException
     *             If element or references is null
     */
	Collection<Element> getTrimOf (Element element,
	            Collection<Point> cutPoints, Point click) throws NullArgumentException;

}
