package io.github.splotycode.mosaik.domparsingimpl.dom;

import io.github.splotycode.mosaik.domparsing.dom.DocumentSectionNode;
import io.github.splotycode.mosaik.domparsing.dom.IdentifierNode;
import io.github.splotycode.mosaik.domparsing.dom.value.*;
import io.github.splotycode.mosaik.util.collection.ArrayUtil;
import io.github.splotycode.mosaik.util.collection.CollectionUtil;
import io.github.splotycode.mosaik.util.collection.MultiHashMap;
import io.github.splotycode.mosaik.util.node.Childable;
import io.github.splotycode.mosaik.util.node.NameableNode;
import io.github.splotycode.mosaik.valuetransformer.TransformerManager;

import java.util.Collection;
import java.util.Collections;
import java.util.regex.Pattern;

public class DefaultDocumentSectionNode implements DocumentSectionNode {

    protected MultiHashMap<String, IdentifierNode> nodes = new MultiHashMap<>();

    @Override
    public Collection<IdentifierNode> getNodes() {
        return nodes.getAllValues();
    }

    @Override
    public MultiHashMap<String, IdentifierNode> getNodeMap() {
        return nodes;
    }

    @Override
    public <T extends NameableNode & Childable<ValueNode>> Collection<T> getAllNodes(boolean avoidSections, boolean includeAttributes) {
        /* TODO */
        return (Collection<T>) getNodes();
    }

    @Override
    public ValueNode getNode(String identifier, String depthSeparator) {
        return getValueNode(identifier, depthSeparator, null);
    }

    protected <T extends ValueNode> T getValueNode(String identifier, String depthSeparator,
                                                   Class<T> type) {
        Collection<IdentifierNode> nodes = getNodes0(identifier, depthSeparator, false);
        if (nodes == null) {
            return null;
        }

        T best = null;
        for (IdentifierNode node : nodes) {
            T value = getType(node, type);
            if (value != null) {
                if (node.getChildes().size() == 1) {
                    return value;
                } else if (best == null) {
                    best = value;
                }
            }
        }
        return best;
    }

    protected <T extends ValueNode> T getType(IdentifierNode node, Class<T> type) {
        for (ValueNode value : node.getChildes()) {
            if (type == null) {
                return (T) value;
            }
            if (type.isInstance(value)) {
                return type.cast(value);
            }
        }
        return null;
    }

    protected <T extends ValueNode> Collection<T> getValueNodes(String identifier, String depthSeparator,
                                                                Class<T> type) {
        IdentifierNode best = null;
        for (IdentifierNode node : getNodes0(identifier, depthSeparator, false)) {
            if (type == null || node.areAllOfType(type)) {
                if (node.getChildes().size() != 1) {
                    return (Collection<T>) best.getChildes();
                } else if (best == null) {
                    best = node;
                }
            }
        }
        return (Collection<T>) best.getChildes();
    }

    protected DocumentSectionNode getNodesSection(String identifier, String[] parts, boolean create) {
        DocumentSectionNode current = this;
        for (int i = 0; i < parts.length - 1; i++) {
            String part = parts[i];
            if (part.isEmpty()) {
                throw new IllegalArgumentException("Malformed identifier: " + identifier);
            }
            DocumentSectionNode picked = pickNodeSection(current.getNodeMap().get(part), parts[i + 1]);
            if (picked == null) {
                if (!create) {
                    return null;
                }
                picked = new DefaultDocumentSectionNode();
                current.addNode(new DefaultIdentifierNode(part, picked));
            }
            current = picked;
        }
        return current;
    }

    protected Collection<IdentifierNode> getNodes0(String identifier, String depthSeparator, boolean create) {
        String[] parts = identifier.split(Pattern.quote(depthSeparator));
        DocumentSectionNode section = getNodesSection(identifier, parts, create);

        String identifierName = ArrayUtil.last(parts);

        Collection<IdentifierNode> result = section.getNodeMap().get(identifierName);
        if (create && result == null) {
            return Collections.singletonList(new DefaultIdentifierNode(identifierName));
        }
        return result;
    }

    /*protected DocumentSectionNode getValueNode(String identifier, String depthSeparator, boolean avoidTail, boolean create) {
        DocumentSectionNode current = this;
        String[] parts = identifier.split(depthSeparator);
        int i = 0;
        boolean last = parts.length == 0;
        while (!last) {
            i++;
            last = parts.length == i;

            String part = parts[i];
            if (part.isEmpty()) {
                return null;
            }
            DocumentSectionNode currentNode = pickNodeSection(current.getNodeMap().get(part), !avoidTail && last);
            if (currentNode == null) {
                if (create) {
                    currentNode = new DefaultDocumentSectionNode(part);
                    current.addNode(currentNode);
                } else {
                    return null;
                }
            }
            current = currentNode;
        }
        return current;
    }*/

    protected DocumentSectionNode pickNodeSection(Collection<IdentifierNode> nodes, String next) {
        for (IdentifierNode node : nodes) {
            for (ValueNode value : node.getChildes()) {
                if (value instanceof DocumentSectionNode) {
                    DocumentSectionNode section = (DocumentSectionNode) value;
                    if (section.getNodeMap().containsKey(next)) {
                        return section;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public ValueNode getNode(String identifier) {
        return getNode(identifier, ".");
    }

    /*protected Pair<DocumentSectionNode, String> getParentNode(String identifier, String depthSeparator, boolean create) {
        int index = identifier.lastIndexOf(depthSeparator);
        if (index == identifier.length() - 1) {
            return null;
        }

        if (index == -1) {
            return new Pair<>(this, identifier);
        }
        String basePath = identifier.substring(0, index);
        String childName = identifier.substring(index + 1);
        return new Pair<>(getValueNode(basePath, depthSeparator, true, create), childName);
    }*/

    @Override
    public Collection<ValueNode> getNodes(String identifier, String depthSeparator) {
        return getValueNodes(identifier, depthSeparator, null);
    }

    @Override
    public Collection<ValueNode> getNodes(String identifier) {
        return getNodes(identifier, ".");
    }

    @Override
    public Object getValue(String identifier, String depthSeparator) {
        ValueNode valueNode = getNode(identifier, depthSeparator);
        return valueNode == null ? null : valueNode.getRawValue();
    }

    @Override
    public Collection<Object> getValueArray(String identifier, String depthSeparator) {
        Collection<ValueNode> valueNodes = getNodes(identifier, depthSeparator);
        if (valueNodes == null) return null;
        return CollectionUtil.map2List(valueNodes, ValueNode::getRawValue);
    }

    @Override
    public Object getValue(String identifier) {
        return getValue(identifier, ".");
    }

    @Override
    public Collection<Object> getValueArray(String identifier) {
        return getValueArray(identifier, ".");
    }

    @Override
    public boolean getBoolean(String identifier, String depthSeparator) {
        BooleanValueNode valueNode = getValueNode(identifier, depthSeparator, BooleanValueNode.class);
        return valueNode != null && valueNode.isValue();
    }

    @Override
    public Collection<Boolean> getBooleanArray(String identifier, String depthSeparator) {
        Collection<BooleanValueNode> valueNodes = getValueNodes(identifier, depthSeparator, BooleanValueNode.class);
        if (valueNodes == null) return null;
        return CollectionUtil.map2List(valueNodes, BooleanValueNode::isValue);
    }

    @Override
    public boolean getBoolean(String identifier) {
        return getBoolean(identifier, ".");
    }

    @Override
    public Collection<Boolean> getBooleanArray(String identifier) {
        return getBooleanArray(identifier, ".");
    }

    @Override
    public String getString(String identifier, String depthSeparator) {
        StringValueNode valueNode = getValueNode(identifier, depthSeparator, StringValueNode.class);
        return valueNode == null ? null : valueNode.getValue();
    }

    @Override
    public Collection<String> getStringArray(String identifier, String depthSeparator) {
        Collection<StringValueNode> valueNodes = getValueNodes(identifier, depthSeparator, StringValueNode.class);
        if (valueNodes == null) return null;
        return CollectionUtil.map2List(valueNodes, StringValueNode::getValue);
    }

    @Override
    public String getString(String identifier) {
        return getString(identifier, ".");
    }

    @Override
    public Collection<String> getStringArray(String identifier) {
        return getStringArray(identifier, ".");
    }

    @Override
    public double getDouble(String identifier, String depthSeparator) {
        NumberValueNode valueNode = getValueNode(identifier, depthSeparator, NumberValueNode.class);
        return valueNode == null ? 0 : valueNode.getValue();
    }

    @Override
    public Collection<Double> getDoubleArray(String identifier, String depthSeparator) {
        Collection<NumberValueNode> valueNodes = getValueNodes(identifier, depthSeparator, NumberValueNode.class);
        if (valueNodes == null) return null;
        return CollectionUtil.map2List(valueNodes, NumberValueNode::getValue);
    }

    @Override
    public double getDouble(String identifier) {
        return getDouble(identifier, ".");
    }

    @Override
    public Collection<Double> getDoubleArray(String identifier) {
        return getDoubleArray(identifier, ".");
    }

    @Override
    public DocumentSectionNode getSection(String identifier, String depthSeparator) {
        return getValueNode(identifier, depthSeparator, DocumentSectionNode.class);
    }

    @Override
    public Collection<DocumentSectionNode> getSectionArray(String identifier, String depthSeparator) {
        return getValueNodes(identifier, depthSeparator, DocumentSectionNode.class);
    }

    @Override
    public DocumentSectionNode getSection(String identifier) {
        return getSection(identifier, ".");
    }

    @Override
    public Collection<DocumentSectionNode> getSectionArray(String identifier) {
        return getSectionArray(identifier, ".");
    }

    @Override
    public boolean isNullValue(String identifier, String depthSeparator) {
        return CollectionUtil.getAny(getNodes0(identifier, depthSeparator, false)).hasOnlyNull();
    }

    @Override
    public boolean isNullValue(String identifier) {
        return isNullValue(identifier, ".");
    }


    @Override
    public void addNode(IdentifierNode node) {
        nodes.addToList(node.name(), node);
    }

    @Override
    public void setValue(String identifier, String depthSeparator, ValueNode value) {
        Collection<IdentifierNode> nodes = getNodes0(identifier, depthSeparator, true);
        CollectionUtil.getAny(nodes).addChild(value);
    }

    @Override
    public void setValue(String identifier, ValueNode value) {
        setValue(identifier, ".", value);
    }

    @Override
    public void setValue(String identifier, String depthSeparator, String value) {
        setValue(identifier, depthSeparator, new StringValueNode(value));
    }

    @Override
    public void setValue(String identifier, String value) {
        setValue(identifier, ".", value);
    }

    @Override
    public void setValue(String identifier, String depthSeparator, boolean value) {
        setValue(identifier, depthSeparator, new BooleanValueNode(value));
    }

    @Override
    public void setValue(String identifier, boolean value) {
        setValue(identifier, ".", value);
    }

    @Override
    public void setValue(String identifier, String depthSeparator, double value) {
        setValue(identifier, depthSeparator, new NumberValueNode(value));
    }

    @Override
    public void setValue(String identifier, double value) {
        setValue(identifier, ".", value);
    }

    @Override
    public void setValue(String identifier, String depthSeparator, Object value) {
        String stringValue = TransformerManager.getInstance().transformToString(value);
        setValue(identifier, depthSeparator, stringValue);
    }

    @Override
    public void setValue(String identifier, Object value) {
        setValue(identifier, ".", value);
    }

    @Override
    public void setNullValue(String identifier, String depthSeparator) {
        setValue(identifier, depthSeparator, new NullValueNode());
    }

    @Override
    public void setNullValue(String identifier) {
        setNullValue(identifier, ".");
    }

    @Override
    public void removeNode(String identifier, String depthSeparator) {
        String[] split = identifier.split(Pattern.quote(depthSeparator));
        DocumentSectionNode section = getNodesSection(identifier, split, false);
        if (section != null) {
            section.getNodeMap().remove(ArrayUtil.last(split));
        }
    }

    @Override
    public void removeNode(String identifier) {
        removeNode(identifier, ".");
    }

    @Override
    public Object getRawValue() {
        return this;
    }
}
