package com.intellij.uiDesigner.core;

import java.awt.*;

public abstract class AbstractLayout implements LayoutManager2 {
    private static final Component[] COMPONENT_EMPTY_ARRAY;

    static {
        COMPONENT_EMPTY_ARRAY = new Component[0];
    }

    // --Commented out by Inspection (3/1/2017 3:04 PM):public static final int DEFAULT_HGAP = 10;
    // --Commented out by Inspection (3/1/2017 3:04 PM):public static final int DEFAULT_VGAP = 5;
    private Component[] myComponents;
    private GridConstraints[] myConstraints;
    private Insets myMargin;
    private int myHGap;

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public AbstractLayout() {
//        this.myComponents = AbstractLayout.COMPONENT_EMPTY_ARRAY;
//        this.myConstraints = GridConstraints.EMPTY_ARRAY;
//        this.myMargin = new Insets(0, 0, 0, 0);
//        this.myHGap = -1;
//        this.myVGap = -1;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public final Insets getMargin() {
//        return (Insets)this.myMargin.clone();
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public final int getHGap() {
//        return this.myHGap;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    protected static int getHGapImpl(Container container) {
//        if (container == null) {
//            throw new IllegalArgumentException("container cannot be null");
//        }
//        while (container != null) {
//            if (container.getLayout() instanceof AbstractLayout) {
//                final AbstractLayout layout = (AbstractLayout)container.getLayout();
//                if (layout.getHGap() != -1) {
//                    return layout.getHGap();
//                }
//            }
//            container = container.getParent();
//        }
//        return 10;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public final void setHGap(final int hGap) {
//        if (hGap < -1) {
//            throw new IllegalArgumentException("wrong hGap: " + hGap);
//        }
//        this.myHGap = hGap;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public final int getVGap() {
//        return this.myVGap;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    protected static int getVGapImpl(Container container) {
//        if (container == null) {
//            throw new IllegalArgumentException("container cannot be null");
//        }
//        while (container != null) {
//            if (container.getLayout() instanceof AbstractLayout) {
//                final AbstractLayout layout = (AbstractLayout)container.getLayout();
//                if (layout.getVGap() != -1) {
//                    return layout.getVGap();
//                }
//            }
//            container = container.getParent();
//        }
//        return 5;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public final void setVGap(final int vGap) {
//        if (vGap < -1) {
//            throw new IllegalArgumentException("wrong vGap: " + vGap);
//        }
//        this.myVGap = vGap;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public final void setMargin(final Insets margin) {
//        if (margin == null) {
//            throw new IllegalArgumentException("margin cannot be null");
//        }
//        this.myMargin = (Insets)margin.clone();
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    final int getComponentCount() {
//        return this.myComponents.length;
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    final Component getComponent(final int index) {
//        return this.myComponents[index];
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

    // --Commented out by Inspection START (3/1/2017 3:04 PM):
//    final GridConstraints getConstraints(final int index) {
//        return this.myConstraints[index];
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)
    private int myVGap;

    public void addLayoutComponent(final Component comp, final Object constraints) {
        if (!(constraints instanceof GridConstraints)) {
            throw new IllegalArgumentException("constraints: " + constraints);
        }
        final Component[] newComponents = new Component[this.myComponents.length + 1];
        System.arraycopy(this.myComponents, 0, newComponents, 0, this.myComponents.length);
        newComponents[this.myComponents.length] = comp;
        this.myComponents = newComponents;
        final GridConstraints[] newConstraints = new GridConstraints[this.myConstraints.length + 1];
        System.arraycopy(this.myConstraints, 0, newConstraints, 0, this.myConstraints.length);
        newConstraints[this.myConstraints.length] = (GridConstraints) ((GridConstraints) constraints).clone();
        this.myConstraints = newConstraints;
    }

    public final void addLayoutComponent(final String name, final Component comp) {
        throw new UnsupportedOperationException();
    }

// --Commented out by Inspection START (3/1/2017 3:04 PM):
//    public GridConstraints getConstraintsForComponent(final Component comp) {
//        final int i = this.getComponentIndex(comp);
//        if (i == -1) {
//            throw new IllegalArgumentException("component was not added: " + comp);
//        }
//        return this.myConstraints[i];
//    }
// --Commented out by Inspection STOP (3/1/2017 3:04 PM)

    public final void removeLayoutComponent(final Component comp) {
        final int i = this.getComponentIndex(comp);
        if (i == -1) {
            throw new IllegalArgumentException("component was not added: " + comp);
        }
        if (this.myComponents.length == 1) {
            this.myComponents = AbstractLayout.COMPONENT_EMPTY_ARRAY;
        } else {
            final Component[] newComponents = new Component[this.myComponents.length - 1];
            System.arraycopy(this.myComponents, 0, newComponents, 0, i);
            System.arraycopy(this.myComponents, i + 1, newComponents, i, this.myComponents.length - i - 1);
            this.myComponents = newComponents;
        }
        if (this.myConstraints.length == 1) {
            this.myConstraints = GridConstraints.EMPTY_ARRAY;
        } else {
            final GridConstraints[] newConstraints = new GridConstraints[this.myConstraints.length - 1];
            System.arraycopy(this.myConstraints, 0, newConstraints, 0, i);
            System.arraycopy(this.myConstraints, i + 1, newConstraints, i, this.myConstraints.length - i - 1);
            this.myConstraints = newConstraints;
        }
    }

    private int getComponentIndex(final Component comp) {
        for (int i = 0; i < this.myComponents.length; ++i) {
            final Component component = this.myComponents[i];
            if (component == comp) {
                return i;
            }
        }
        return -1;
    }

    public final float getLayoutAlignmentX(final Container container) {
        return 0.5f;
    }

    public final float getLayoutAlignmentY(final Container container) {
        return 0.5f;
    }

    public abstract Dimension maximumLayoutSize(final Container p0);

    public abstract void invalidateLayout(final Container p0);

    public abstract Dimension preferredLayoutSize(final Container p0);

    public abstract Dimension minimumLayoutSize(final Container p0);

    public abstract void layoutContainer(final Container p0);
}