package inventory.content;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class SlotPos {

    private final int row;
    private final int column;

    public SlotPos(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public static SlotPos of(int row, int column) {
        return new SlotPos(row, column);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        SlotPos slotPos = (SlotPos) obj;
        return new EqualsBuilder().append(row, slotPos.row).append(column, slotPos.column).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(row).append(column).toHashCode();
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

}
