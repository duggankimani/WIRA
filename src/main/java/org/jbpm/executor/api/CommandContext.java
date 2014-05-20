package org.jbpm.executor.api;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.duggan.workflow.server.rest.model.MapAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CommandContext implements Serializable {

    private static final long serialVersionUID = -1440017934399413860L;
   
    @XmlJavaTypeAdapter(MapAdapter.class)
    private Map<String, Object> data = new HashMap<String, Object>();

    public CommandContext() {
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Object getData(String key) {
        return data.get(key);
    }

    public void setData(String key, Object value) {
        data.put(key, value);
    }

    public Set<String> keySet() {
        return data.keySet();
    }

    @Override
    public String toString() {
        return "CommandContext{" + "data=" + data + '}';
    }
}
