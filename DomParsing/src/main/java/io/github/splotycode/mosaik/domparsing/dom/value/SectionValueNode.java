package io.github.splotycode.mosaik.domparsing.dom.value;

import io.github.splotycode.mosaik.domparsing.dom.DocumentSectionNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class SectionValueNode implements ValueNode {

    @Getter private DocumentSectionNode section;

    @Override
    public Object getRawValue() {
        return section;
    }

    @Override
    public boolean supportsToString() {
        return false;
    }
}
