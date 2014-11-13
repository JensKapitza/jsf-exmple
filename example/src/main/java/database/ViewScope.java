package database;

import java.util.Map;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

public class ViewScope implements Scope {

	public Object get(String name, ObjectFactory<?> objectFactory) {
		try {
			Map<String, Object> viewMap = FacesContext.getCurrentInstance()
					.getViewRoot().getViewMap();

			if (viewMap.containsKey(name)) {
				return viewMap.get(name);
			} else {
				Object object = objectFactory.getObject();
				viewMap.put(name, object);

				return object;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return objectFactory.getObject();
		}
	}

	public Object remove(String name) {
		try {
			Map<String, Object> viewMap = FacesContext.getCurrentInstance()
					.getViewRoot().getViewMap();

			if (viewMap.containsKey(name)) {
				return viewMap.remove(name);

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return false;
	}

	public String getConversationId() {
		return null;
	}

	public void registerDestructionCallback(String name, Runnable callback) {
		// Not supported
	}

	public Object resolveContextualObject(String key) {
		return null;
	}
}