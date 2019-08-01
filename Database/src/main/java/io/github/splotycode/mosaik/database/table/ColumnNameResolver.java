package io.github.splotycode.mosaik.database.table;

import io.github.splotycode.mosaik.util.EnumUtil;
import io.github.splotycode.mosaik.util.commoni.Nameable;

public interface ColumnNameResolver {

    interface EnumColumnName extends ColumnNameResolver, Nameable {

        @Override
        default String getColumnName() {
            return EnumUtil.toDisplayName(name(), "", false);
        }
    }

    String getColumnName();

}
