package com.intellij.uiDesigner.core;

import java.awt.*;
import java.util.ArrayList;

final class LayoutState {
    private final Component[] myComponents;
    private final GridConstraints[] myConstraints;
    private final int myColumnCount;
    private final int myRowCount;

    public LayoutState(final GridLayoutManager layout, final boolean ignoreInvisibleComponents) {
        final ArrayList<Component> componentsList = new ArrayList<>(layout.getComponentCount());
        final ArrayList<GridConstraints> constraintsList = new ArrayList<>(layout.getComponentCount());
        for (int i = 0; i < layout.getComponentCount(); ++i) {
            final Component component = layout.getComponent(i);
            if (!ignoreInvisibleComponents || component.isVisible()) {
                componentsList.add(component);
                final GridConstraints constraints = layout.getConstraints(i);
                constraintsList.add(constraints);
            }
        }
        this.myComponents = componentsList.toArray(new Component[componentsList.size()]);
        this.myConstraints = constraintsList.toArray(new GridConstraints[constraintsList.size()]);
        Dimension[] myMinimumSizes = new Dimension[this.myComponents.length];
        Dimension[] myPreferredSizes = new Dimension[this.myComponents.length];
        this.myColumnCount = layout.getColumnCount();
        this.myRowCount = layout.getRowCount();
    }

    public int getComponentCount() {
        return this.myComponents.length;
    }

    public Component getComponent(final int index) {
        return this.myComponents[index];
    }

    public GridConstraints getConstraints(final int index) {
        return this.myConstraints[index];
    }

    public int getColumnCount() {
        return this.myColumnCount;
    }

    public int getRowCount() {
        return this.myRowCount;
    }
}