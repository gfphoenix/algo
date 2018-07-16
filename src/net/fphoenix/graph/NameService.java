package net.fphoenix.graph;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wuhao on 7/24/16.
 */
public class NameService {
    private static class NodeKey {
        final int id;
        final String name;

        private NodeKey(String name, int id) {
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }
    }

    int count;
    final Map<Integer, String> i2s = new HashMap<Integer, String>();
    final Map<String, Integer> s2i = new HashMap<String, Integer>();

    public NameService(int count0) {
        this.count = count0;
    }

    public NameService() {
        this(1);
    }
    public String defaultI2S(int id) {
        return "id_" + id;
    }
    private NodeKey putNodeKey(NodeKey nodeKey) {
        i2s.put(nodeKey.getId(), nodeKey.getName());
        s2i.put(nodeKey.getName(), nodeKey.getId());
        return nodeKey;
    }

    public NodeKey makeNode() {
        int id = count++;
        NodeKey node = new NodeKey(defaultI2S(id), id);
        return putNodeKey(node);
    }

    public NodeKey makeNode(String name) {
        if (name == null)
            throw new IllegalArgumentException("name is null");
        if (s2i.get(name) != null)
            throw new IllegalArgumentException("name is exists");
        NodeKey node = new NodeKey(name, count++);
        return putNodeKey(node);
    }

    // true means ok
    public NodeKey makeNode(String name, int id) {
        Integer i = s2i.get(name);
        if (i != null) return null;
        String s = i2s.get(id);
        if (s != null) return null;
        if (count <= id) count = id + 1;
        NodeKey node = new NodeKey(name, id);
        return putNodeKey(node);
    }

    public NodeKey makeNode(int id) {
        String s = i2s.get(id);
        if (s != null) return null;
        if (count <= id) count = id + 1;
        NodeKey node = new NodeKey(defaultI2S(id), id);
        return putNodeKey(node);
    }

    public boolean clearNode(int id) {
        String s = i2s.get(id);
        if (s == null) return false;
        i2s.remove(id);
        s2i.remove(s);
        return true;
    }

    public boolean clearNode(String name) {
        Integer id = s2i.get(name);
        if (id == null) return false;
        s2i.remove(name);
        i2s.remove(id);
        return true;
    }

    public String getName(int id) {
        return i2s.get(id);
    }

    public Integer getId(String name) {
        return s2i.get(name);
    }
}
