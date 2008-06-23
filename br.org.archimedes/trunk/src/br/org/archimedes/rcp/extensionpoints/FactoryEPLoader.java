/*
 * Created on Jun 16, 2008 for br.org.archimedes
 */

package br.org.archimedes.rcp.extensionpoints;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;

import br.org.archimedes.factories.CommandFactory;
import br.org.archimedes.factories.SelectorFactory;
import br.org.archimedes.model.Element;
import br.org.archimedes.rcp.ExtensionLoader;
import br.org.archimedes.rcp.ExtensionTagHandler;

/**
 * Belongs to package br.org.archimedes.rcp.extensionpoints.
 * 
 * @author night
 */
public class FactoryEPLoader implements ExtensionTagHandler {

    private static final String CLASS_ATTRIBUTE_NAME = "class";

    private static final String FACTORY_EXTENSION_POINT_ID = "br.org.archimedes.factory";

    private static final String SHORTCUT_ATTRIBUTE_NAME = "shortcut";

    private static final String ID_ATTRIBUTE_NAME = "id";

    private static final String HANDLED_ELEMENT_ID = "handledElementId";

    private static final String HANDLES_DOUBLE_CLICKS = "handlesDoubleClicks";

    private static final Map<String, CommandFactory> idToFactoryMap = new HashMap<String, CommandFactory>();

    private static final Map<Class<? extends Element>, Class<? extends SelectorFactory>> elementToDoubleClickHandler = new HashMap<Class<? extends Element>, Class<? extends SelectorFactory>>();

    private ElementEPLoader elementLoader;


    /**
     * Default constructor. Loads the maps if they are empty (not loaded or
     * without any element).
     */
    public FactoryEPLoader () {

        elementLoader = new ElementEPLoader();
        if (idToFactoryMap.isEmpty()) {
            ExtensionLoader loader = new ExtensionLoader(
                    FACTORY_EXTENSION_POINT_ID);
            loader.loadExtension(this);
        }
    }

    /**
     * @see br.org.archimedes.rcp.ExtensionTagHandler#handleTag(org.eclipse.core.runtime.IConfigurationElement)
     */
    public void handleTag (IConfigurationElement element) throws CoreException {

        CommandFactory factory = (CommandFactory) element
                .createExecutableExtension(CLASS_ATTRIBUTE_NAME);

        // Name is not unique. Factories with repeated names will be lost.
        // When this becomes a problem, we must do a "Shortcut Editing Window"
        // so that this may be a bit more independent of the plugin writer.
        addCommand(element.getAttribute(ID_ATTRIBUTE_NAME), factory); //$NON-NLS-1$
        String factoryName = factory.getName();
        if (factoryName != null) {
            addCommand(factoryName, factory);
        }

        IConfigurationElement[] shortcuts = element
                .getChildren(SHORTCUT_ATTRIBUTE_NAME); //$NON-NLS-1$
        if (shortcuts != null) {
            for (IConfigurationElement shortcut : shortcuts) {
                String name = shortcut.getAttribute(ID_ATTRIBUTE_NAME); //$NON-NLS-1$
                addCommand(name, factory);
            }
        }

        String handlesDoubleClicks = element
                .getAttribute(HANDLES_DOUBLE_CLICKS);
        if (handlesDoubleClicks != null) {
            loadDoubleClickHandler(element, handlesDoubleClicks);
        }
    }

    /**
     * @param factoryTag
     *            The tag that contains all necessary info
     * @param shouldHandle
     *            "true" if this factory should handle double click, any other
     *            thing otherwise.
     * @throws CoreException
     *             Thrown if the selector factory cannot be created
     */
    private void loadDoubleClickHandler (IConfigurationElement factoryTag,
            String shouldHandle) throws CoreException {

        if (Boolean.valueOf(shouldHandle).booleanValue() == true) {

            String handledElementId = factoryTag
                    .getAttribute(HANDLED_ELEMENT_ID);
            String factoryClass = factoryTag.getAttribute(CLASS_ATTRIBUTE_NAME);

            if (handledElementId != null && factoryClass != null) {
                Class<? extends Element> target = elementLoader
                        .getElementClass(handledElementId);
                SelectorFactory factory = (SelectorFactory) factoryTag
                        .createExecutableExtension(CLASS_ATTRIBUTE_NAME);
                // TODO Understand why this must hold the class and
                // not the instance
                if (target != null && factory != null) {
                    elementToDoubleClickHandler.put(target, factory.getClass());
                }
            }
        }
    }

    /**
     * @param key
     *            The key to identify the factory being added
     * @param factory
     *            The factory to be added to this key
     */
    private void addCommand (String key, CommandFactory commandFactory) {

        idToFactoryMap.put(key.toLowerCase(), commandFactory);
    }

    /**
     * @return A map get all commands that access factory maps
     */
    public Map<String, CommandFactory> getFatoryMap () {

        return idToFactoryMap;
    }

    /**
     * Finds a Factory that registered as a double click handler for elements of
     * this type.
     * 
     * @param element
     *            Element
     */
    public SelectorFactory getDoubleClickFactoryFor (Element element) {

        Class<? extends SelectorFactory> factoryClass = elementToDoubleClickHandler
                .get(element.getClass());

        if (factoryClass != null) {
            try {
                SelectorFactory factoryInstance = factoryClass.newInstance();
                return factoryInstance;
            }
            catch (InstantiationException e) {
                e.printStackTrace();
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}