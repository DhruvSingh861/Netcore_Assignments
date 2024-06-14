package org.example;

import de.bytefish.pgbulkinsert.mapping.AbstractMapping;

public class PersonMapping extends AbstractMapping<Employee> {
    public PersonMapping(){
        super("","employee");
        mapText("name", Employee::getName);
        mapText("designation",Employee::getDesignation);
    }
}
