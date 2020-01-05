package io.github.splotycode.mosaik.domparsing.dom.value;

import io.github.splotycode.mosaik.domparsing.dom.DocumentSectionNode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SectionValueNode implements ValueNode {

    private DocumentSectionNode section;

    @Override
    public Object getRawValue() {
        return section;
    }

    @Override
    public String name() {
        return "Section";
    }
}
