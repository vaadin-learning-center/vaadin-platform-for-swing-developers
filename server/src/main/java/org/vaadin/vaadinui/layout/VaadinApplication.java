/**
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.vaadin.vaadinui.layout;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.vaadin.backend.ejb.CustomerFacadeRemote;
import org.vaadin.domain.model.Customer;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import java.util.List;
import java.util.Properties;

@Route("")
public class VaadinApplication extends VerticalLayout {

    CustomerForm form;
    Span countLabel = new Span();
    Button newCustomer = new Button("Add new");

    String[] columnNames = new String[]{"firstName", "lastName", "email"};
    private Grid<Customer> table;

    private List<Customer> customers;

    private CustomerFacadeRemote customerFacade;

    void deselect() {
        table.getSelectionModel().deselectAll();
    }

    public VaadinApplication() {
        createUI();
    }

    private void createUI() {
        newCustomer.addClickListener(buttonClickEvent -> form.clear());

        form = new CustomerForm(this);

        HorizontalLayout hbox = new HorizontalLayout();
        hbox.setAlignItems(Alignment.BASELINE);
        hbox.setWidthFull();

        hbox.add(newCustomer);
        hbox.add(countLabel);
        add(hbox);

        table = new Grid<>(Customer.class);
        table.setColumns(columnNames);
        table.setSelectionMode(Grid.SelectionMode.SINGLE);
        table.addSelectionListener(selectionEvent -> {
                    Customer c = selectionEvent.getFirstSelectedItem().orElse(null);
                    if (c == null) {
                        form.clear();
                    } else {
                        form.editCustomer(c);
                    }
                });
        add(table);

        add(form);

        refreshData();
        setSizeFull();
    }

    protected void refreshData() {
        customers = getCustomerFacade().findAll();
        table.setItems(customers);
        countLabel.setText("Customers in DB: " + customers.size());
    }

    public CustomerFacadeRemote getCustomerFacade() {
        if (customerFacade == null) {
            try {
                final Object ref = new InitialContext(
                        getJndiPropsCustomerServer()).lookup(
                                "CustomerFacadeRemote");
                customerFacade = (CustomerFacadeRemote) PortableRemoteObject.
                        narrow(ref, CustomerFacadeRemote.class);
            } catch (NamingException ex) {
                throw new RuntimeException(ex);
            }
        }
        return customerFacade;
    }

    protected Properties getJndiPropsCustomerServer() {
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY,
                "org.apache.openejb.client.RemoteInitialContextFactory");
        props.put(Context.PROVIDER_URL, "http://127.0.0.1:8080/tomee/ejb");
        return props;
    }
}
