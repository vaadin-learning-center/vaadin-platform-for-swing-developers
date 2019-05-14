package org.vaadin.vaadinui.layout;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.vaadin.domain.model.Customer;

/**
 *
 * @author Matti Tahvonen
 */
public class CustomerForm extends FormLayout implements ComponentEventListener<ClickEvent<Button>> {

    TextField firstName = new TextField();
    TextField lastName = new TextField();
    TextField email = new TextField();
    Button create = new Button("Create");
    Button update = new Button("Update");
    Button delete = new Button("Delete");

    private final VaadinApplication application;
    private Customer editedCustomer;

    public CustomerForm(VaadinApplication application) {
        this.application = application;

        setResponsiveSteps(new ResponsiveStep("0", 1, ResponsiveStep.LabelsPosition.ASIDE));

        addFormItem(firstName, "First name:");
        addFormItem(lastName, "Last name:");
        addFormItem(email, "Email:");

        final HorizontalLayout actionButtons = new HorizontalLayout();

        actionButtons.add(create);
        actionButtons.add(update);
        actionButtons.add(delete);

        add(actionButtons);

        create.addClickListener(this);
        update.addClickListener(this);
        delete.addClickListener(this);

        updateButtonStates();
    }

    @Override
    public void onComponentEvent(ClickEvent<Button> e) {
        if (e.getSource() == delete) {
            application.getCustomerFacade().remove(editedCustomer);
            application.deselect();
            clear();
        } else {
            Customer c = editedCustomer;
            if (e.getSource() == create) {
                c = new Customer();
            }
            c.setFirstName(firstName.getValue());
            c.setLastName(lastName.getValue());
            c.setEmail(email.getValue());
            application.getCustomerFacade().save(c);
        }
        application.refreshData();
    }

    void editCustomer(Customer c) {
        this.editedCustomer = c;
        firstName.setValue(c.getFirstName());
        lastName.setValue(c.getLastName());
        email.setValue(c.getEmail());
        updateButtonStates();
    }

    void clear() {
        editedCustomer = null;
        firstName.setValue("");
        lastName.setValue("");
        email.setValue("your@email.com");
        updateButtonStates();
    }

    private void updateButtonStates() {
        update.setEnabled(editedCustomer != null);
        delete.setEnabled(editedCustomer != null);
        create.setEnabled(editedCustomer == null);
    }
}
