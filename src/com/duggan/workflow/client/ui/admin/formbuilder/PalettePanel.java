package com.duggan.workflow.client.ui.admin.formbuilder;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.duggan.workflow.client.ui.admin.formbuilder.component.Field;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class PalettePanel extends VerticalPanel {

	private PickupDragController dragController;

	public PalettePanel() {
		
		addStyleName("demo-PalettePanel");
		setSpacing(2);
		setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	}

	public void registerDragController(PickupDragController controller) {
		this.dragController = controller;
		for (int i = 0; i < this.getWidgetCount(); i++) {
			controller.makeDraggable(this.getWidget(i));
		}

	}

	/**
	 * Overloaded method that makes widgets draggable.
	 * 
	 * @param w
	 *  the widget to be added are made draggable
	 */
	public void add(Field w) {
		if(dragController!=null){
			dragController.makeDraggable(w.asWidget());
			System.err.println("Adding widget "+w);
		}
		
		super.add(w);
	}

	/**
	 * Removed widgets that are instances of {@link PaletteWidget} are
	 * immediately replaced with a cloned copy of the original.
	 * 
	 * @param w
	 *            the widget to remove
	 * @return true if a widget was removed
	 */
	@Override
	public boolean remove(Widget w) {
		int index = getWidgetIndex(w);
		if (index != -1 && w instanceof Field) {
			Field clone = ((Field) w).cloneWidget();
			dragController.makeDraggable(clone);
			insert(clone, index);
		}
		return super.remove(w);
	}
}