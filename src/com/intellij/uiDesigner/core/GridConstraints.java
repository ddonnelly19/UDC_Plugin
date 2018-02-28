package com.intellij.uiDesigner.core;

import java.awt.*;

final class GridConstraints implements Cloneable {
    static {
        GridConstraints[] EMPTY_ARRAY = new GridConstraints[0];
    }

    private final Dimension myMinimumSize;
    private final Dimension myPreferredSize;
    private final Dimension myMaximumSize;
    // --Commented out by Inspection (3/1/2017 3:04 PM):private static final GridConstraints[] EMPTY_ARRAY;
    // --Commented out by Inspection (3/1/2017 3:04 PM):public static final int FILL_NONE = 0;
    // --Commented out by Inspection (3/1/2017 3:04 PM):public static final int FILL_HORIZONTAL = 1;
    // --Commented out by Inspection (3/1/2017 3:04 PM):public static final int FILL_VERTICAL = 2;
    // --Commented out by Inspection (3/1/2017 3:04 PM):public static final int FILL_BOTH = 3;
    // --Commented out by Inspection (3/1/2017 3:04 PM):public static final int ANCHOR_CENTER = 0;
    // --Commented out by Inspection (3/1/2017 3:04 PM):public static final int ANCHOR_NORTH = 1;
    // --Commented out by Inspection (3/1/2017 3:04 PM):public static final int ANCHOR_SOUTH = 2;
    // --Commented out by Inspection (3/1/2017 3:04 PM):public static final int ANCHOR_EAST = 4;
    // --Commented out by Inspection (3/1/2017 3:04 PM):public static final int ANCHOR_WEST = 8;
    // --Commented out by Inspection (3/1/2017 3:04 PM):public static final int ANCHOR_NORTHEAST = 5;
    // --Commented out by Inspection (3/1/2017 3:04 PM):public static final int ANCHOR_SOUTHEAST = 6;
    // --Commented out by Inspection (3/1/2017 3:04 PM):public static final int ANCHOR_SOUTHWEST = 10;
    // --Commented out by Inspection (3/1/2017 3:04 PM):public static final int ANCHOR_NORTHWEST = 9;
    // --Commented out by Inspection (3/1/2017 3:04 PM):public static final int SIZEPOLICY_FIXED = 0;
    // --Commented out by Inspection (3/1/2017 3:04 PM):public static final int SIZEPOLICY_CAN_SHRINK = 1;
    // --Commented out by Inspection (3/1/2017 3:04 PM):public static final int SIZEPOLICY_CAN_GROW = 2;
    // --Commented out by Inspection (3/1/2017 3:04 PM):public static final int SIZEPOLICY_WANT_GROW = 4;
    // --Commented out by Inspection (3/1/2017 3:04 PM):public static final int ALIGN_LEFT = 0;
    // --Commented out by Inspection (3/1/2017 3:04 PM):public static final int ALIGN_CENTER = 1;
    // --Commented out by Inspection (3/1/2017 3:04 PM):public static final int ALIGN_RIGHT = 2;
    // --Commented out by Inspection (3/1/2017 3:04 PM):public static final int ALIGN_FILL = 3;
    private int myRow;
    private int myColumn;
    private int myRowSpan;
    private int myColSpan;
    private int myVSizePolicy;
    private int myHSizePolicy;
    private int myFill;
    private int myAnchor;
    private int myIndent;
    private boolean myUseParentLayout;

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    private GridConstraints(final int row, final int column, final int rowSpan, final int colSpan, final int anchor, final int fill, final int HSizePolicy, final int VSizePolicy, final Dimension minimumSize, final Dimension preferredSize, final Dimension maximumSize) {
//        this.myRow = row;
//        this.myColumn = column;
//        this.myRowSpan = rowSpan;
//        this.myColSpan = colSpan;
//        this.myAnchor = anchor;
//        this.myFill = fill;
//        this.myHSizePolicy = HSizePolicy;
//        this.myVSizePolicy = VSizePolicy;
//        this.myMinimumSize = ((minimumSize != null) ? new Dimension(minimumSize) : new Dimension(-1, -1));
//        this.myPreferredSize = ((preferredSize != null) ? new Dimension(preferredSize) : new Dimension(-1, -1));
//        this.myMaximumSize = ((maximumSize != null) ? new Dimension(maximumSize) : new Dimension(-1, -1));
//        this.myIndent = 0;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    private GridConstraints(final int row, final int column, final int rowSpan, final int colSpan, final int anchor, final int fill, final int HSizePolicy, final int VSizePolicy, final Dimension minimumSize, final Dimension preferredSize, final Dimension maximumSize, final int indent) {
//        this(row, column, rowSpan, colSpan, anchor, fill, HSizePolicy, VSizePolicy, minimumSize, preferredSize, maximumSize);
//        this.myIndent = indent;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    private GridConstraints(final int row, final int column, final int rowSpan, final int colSpan, final int anchor, final int fill, final int HSizePolicy, final int VSizePolicy, final Dimension minimumSize, final Dimension preferredSize, final Dimension maximumSize, final int indent, final boolean useParentLayout) {
//        this(row, column, rowSpan, colSpan, anchor, fill, HSizePolicy, VSizePolicy, minimumSize, preferredSize, maximumSize, indent);
//        this.myUseParentLayout = useParentLayout;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

    private GridConstraints(int myRow, int myColumn, int myRowSpan, int myColSpan, int myAnchor, int myFill, int myHSizePolicy, int myVSizePolicy, Dimension dimension, Dimension dimension1, Dimension dimension2, int myIndent, boolean myUseParentLayout) {
        this.myRowSpan = 1;
        this.myColSpan = 1;
        this.myVSizePolicy = 3;
        this.myHSizePolicy = 3;
        this.myFill = 0;
        this.myAnchor = 0;
        this.myMinimumSize = new Dimension(-1, -1);
        this.myPreferredSize = new Dimension(-1, -1);
        this.myMaximumSize = new Dimension(-1, -1);
        this.myIndent = 0;
    }

    public Object clone() {
        return new GridConstraints(this.myRow, this.myColumn, this.myRowSpan, this.myColSpan, this.myAnchor, this.myFill, this.myHSizePolicy, this.myVSizePolicy, new Dimension(this.myMinimumSize), new Dimension(this.myPreferredSize), new Dimension(this.myMaximumSize), this.myIndent, this.myUseParentLayout);
    }

    private int getColumn() {
        return this.myColumn;
    }

    private void setColumn(final int column) {
        if (column < 0) {
            throw new IllegalArgumentException("wrong column: " + column);
        }
        this.myColumn = column;
    }

    private int getRow() {
        return this.myRow;
    }

    private void setRow(final int row) {
        if (row < 0) {
            throw new IllegalArgumentException("wrong row: " + row);
        }
        this.myRow = row;
    }

    private int getRowSpan() {
        return this.myRowSpan;
    }

    private void setRowSpan(final int rowSpan) {
        if (rowSpan <= 0) {
            throw new IllegalArgumentException("wrong rowSpan: " + rowSpan);
        }
        this.myRowSpan = rowSpan;
    }

    private int getColSpan() {
        return this.myColSpan;
    }

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public int getHSizePolicy() {
//        return this.myHSizePolicy;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public void setHSizePolicy(final int sizePolicy) {
//        if (sizePolicy < 0 || sizePolicy > 7) {
//            throw new IllegalArgumentException("invalid sizePolicy: " + sizePolicy);
//        }
//        this.myHSizePolicy = sizePolicy;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public int getVSizePolicy() {
//        return this.myVSizePolicy;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public void setVSizePolicy(final int sizePolicy) {
//        if (sizePolicy < 0 || sizePolicy > 7) {
//            throw new IllegalArgumentException("invalid sizePolicy: " + sizePolicy);
//        }
//        this.myVSizePolicy = sizePolicy;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public int getAnchor() {
//        return this.myAnchor;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public void setAnchor(final int anchor) {
//        if (anchor < 0 || anchor > 15) {
//            throw new IllegalArgumentException("invalid anchor: " + anchor);
//        }
//        this.myAnchor = anchor;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public int getFill() {
//        return this.myFill;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public void setFill(final int fill) {
//        if (fill != 0 && fill != 1 && fill != 2 && fill != 3) {
//            throw new IllegalArgumentException("invalid fill: " + fill);
//        }
//        this.myFill = fill;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public int getIndent() {
//        return this.myIndent;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public void setIndent(final int indent) {
//        this.myIndent = indent;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public boolean isUseParentLayout() {
//        return this.myUseParentLayout;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public void setUseParentLayout(final boolean useParentLayout) {
//        this.myUseParentLayout = useParentLayout;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public GridConstraints store() {
//        final GridConstraints copy = new GridConstraints();
//        copy.setRow(this.myRow);
//        copy.setColumn(this.myColumn);
//        copy.setColSpan(this.myColSpan);
//        copy.setRowSpan(this.myRowSpan);
//        copy.setVSizePolicy(this.myVSizePolicy);
//        copy.setHSizePolicy(this.myHSizePolicy);
//        copy.setFill(this.myFill);
//        copy.setAnchor(this.myAnchor);
//        copy.setIndent(this.myIndent);
//        copy.setUseParentLayout(this.myUseParentLayout);
//        copy.myMinimumSize.setSize(this.myMinimumSize);
//        copy.myPreferredSize.setSize(this.myPreferredSize);
//        copy.myMaximumSize.setSize(this.myMaximumSize);
//        return copy;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public void restore(final GridConstraints constraints) {
//        this.myRow = constraints.myRow;
//        this.myColumn = constraints.myColumn;
//        this.myRowSpan = constraints.myRowSpan;
//        this.myColSpan = constraints.myColSpan;
//        this.myHSizePolicy = constraints.myHSizePolicy;
//        this.myVSizePolicy = constraints.myVSizePolicy;
//        this.myFill = constraints.myFill;
//        this.myAnchor = constraints.myAnchor;
//        this.myIndent = constraints.myIndent;
//        this.myUseParentLayout = constraints.myUseParentLayout;
//        this.myMinimumSize.setSize(constraints.myMinimumSize);
//        this.myPreferredSize.setSize(constraints.myPreferredSize);
//        this.myMaximumSize.setSize(constraints.myMaximumSize);
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

    private void setColSpan(final int colSpan) {
        if (colSpan <= 0) {
            throw new IllegalArgumentException("wrong colSpan: " + colSpan);
        }
        this.myColSpan = colSpan;
    }

    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GridConstraints)) {
            return false;
        }
        final GridConstraints gridConstraints = (GridConstraints) o;
        if (this.myAnchor != gridConstraints.myAnchor) {
            return false;
        }
        if (this.myColSpan != gridConstraints.myColSpan) {
            return false;
        }
        if (this.myColumn != gridConstraints.myColumn) {
            return false;
        }
        if (this.myFill != gridConstraints.myFill) {
            return false;
        }
        if (this.myHSizePolicy != gridConstraints.myHSizePolicy) {
            return false;
        }
        if (this.myRow != gridConstraints.myRow) {
            return false;
        }
        if (this.myRowSpan != gridConstraints.myRowSpan) {
            return false;
        }
        if (this.myVSizePolicy != gridConstraints.myVSizePolicy) {
            return false;
        }
        Label_0158:
        {
            if (this.myMaximumSize != null) {
                if (this.myMaximumSize.equals(gridConstraints.myMaximumSize)) {
                    break Label_0158;
                }
            } else if (gridConstraints.myMaximumSize == null) {
                break Label_0158;
            }
            return false;
        }
        Label_0191:
        {
            if (this.myMinimumSize != null) {
                if (this.myMinimumSize.equals(gridConstraints.myMinimumSize)) {
                    break Label_0191;
                }
            } else if (gridConstraints.myMinimumSize == null) {
                break Label_0191;
            }
            return false;
        }
        if (this.myPreferredSize != null) {
            if (this.myPreferredSize.equals(gridConstraints.myPreferredSize)) {
                return this.myIndent == gridConstraints.myIndent && this.myUseParentLayout == gridConstraints.myUseParentLayout;
            }
        } else if (gridConstraints.myPreferredSize == null) {
            return this.myIndent == gridConstraints.myIndent && this.myUseParentLayout == gridConstraints.myUseParentLayout;
        }
        return false;
    }

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public int getCell(final boolean isRow) {
//        return isRow ? this.getRow() : this.getColumn();
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public void setCell(final boolean isRow, final int value) {
//        if (isRow) {
//            this.setRow(value);
//        }
//        else {
//            this.setColumn(value);
//        }
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public int getSpan(final boolean isRow) {
//        return isRow ? this.getRowSpan() : this.getColSpan();
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public void setSpan(final boolean isRow, final int value) {
//        if (isRow) {
//            this.setRowSpan(value);
//        }
//        else {
//            this.setColSpan(value);
//        }
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public boolean contains(final boolean isRow, final int cell) {
//        if (isRow) {
//            return cell >= this.myRow && cell < this.myRow + this.myRowSpan;
//        }
//        return cell >= this.myColumn && cell < this.myColumn + this.myColSpan;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

    public int hashCode() {
        int result = this.myRow;
        result = 29 * result + this.myColumn;
        result = 29 * result + this.myRowSpan;
        result = 29 * result + this.myColSpan;
        result = 29 * result + this.myVSizePolicy;
        result = 29 * result + this.myHSizePolicy;
        result = 29 * result + this.myFill;
        result = 29 * result + this.myAnchor;
        result = 29 * result + ((this.myMinimumSize != null) ? this.myMinimumSize.hashCode() : 0);
        result = 29 * result + ((this.myPreferredSize != null) ? this.myPreferredSize.hashCode() : 0);
        result = 29 * result + ((this.myMaximumSize != null) ? this.myMaximumSize.hashCode() : 0);
        result = 29 * result + this.myIndent;
        result = 29 * result + (this.myUseParentLayout ? 1 : 0);
        return result;
    }

    public String toString() {
        return "GridConstraints (row=" + this.myRow + ", col=" + this.myColumn + ", rowspan=" + this.myRowSpan + ", colspan=" + this.myColSpan + ")";
    }
}