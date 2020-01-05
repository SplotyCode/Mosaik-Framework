package io.github.splotycode.mosaik.domparsing.dom;

import io.github.splotycode.mosaik.domparsing.dom.value.ValueNode;
import io.github.splotycode.mosaik.util.collection.MultiHashMap;
import io.github.splotycode.mosaik.util.node.Childable;
import io.github.splotycode.mosaik.util.node.NameableNode;

import java.util.Collection;

public interface DocumentSectionNode extends ValueNode {

    Collection<IdentifierNode> getNodes();
    MultiHashMap<String, IdentifierNode> getNodeMap();

    <T extends NameableNode & Childable<ValueNode>> Collection<T>  getAllNodes(boolean avoidSections, boolean includeAttributes);

    ValueNode getNode(String identifier, String depthSeparator);
    ValueNode getNode(String identifier);
    Collection<ValueNode> getNodes(String identifier, String depthSeparator);
    Collection<ValueNode> getNodes(String identifier);

    Object getValue(String identifier, String depthSeparator);
    Collection<Object> getValueArray(String identifier, String depthSeparator);
    Object getValue(String identifier);
    Collection<Object> getValueArray(String identifier);

    boolean getBoolean(String identifier, String depthSeparator);
    Collection<Boolean> getBooleanArray(String identifier, String depthSeparator);
    boolean getBoolean(String identifier);
    Collection<Boolean> getBooleanArray(String identifier);

    String getString(String identifier, String depthSeparator);
    Collection<String> getStringArray(String identifier, String depthSeparator);
    String getString(String identifier);
    Collection<String> getStringArray(String identifier);

    double getDouble(String identifier, String depthSeparator);
    Collection<Double> getDoubleArray(String identifier, String depthSeparator);
    double getDouble(String identifier);
    Collection<Double> getDoubleArray(String identifier);

    DocumentSectionNode getSection(String identifier, String depthSeparator);
    Collection<DocumentSectionNode> getSectionArray(String identifier, String depthSeparator);
    DocumentSectionNode getSection(String identifier);
    Collection<DocumentSectionNode> getSectionArray(String identifier);

    boolean isNullValue(String identifier, String depthSeparator);
    boolean isNullValue(String identifier);


    void addNode(IdentifierNode node);
    void setValue(String identifier, String depthSeparator, ValueNode value);
    void setValue(String identifier, ValueNode value);

    void setValue(String identifier,  String depthSeparator, String value);
    void setValue(String identifier, String value);
    void setValue(String identifier,  String depthSeparator, boolean value);
    void setValue(String identifier, boolean value);
    void setValue(String identifier,  String depthSeparator, double value);
    void setValue(String identifier, double value);
    void setValue(String identifier,  String depthSeparator, Object value);
    void setValue(String identifier, Object value);
    void setNullValue(String identifier,  String depthSeparator);
    void setNullValue(String identifier);

    void removeNode(String identifier,  String depthSeparator);
    void removeNode(String identifier);

    @Override
    default boolean supportsToString() {
        return false;
    }
}
