module personal.finance.manager {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.persistence;
    requires org.hibernate.orm.core;

    opens  view to javafx.fxml;
    exports view;
    exports bll;
    exports bll.factories;
    exports bll.builders;
    exports bll.exceptions;
    exports bll.entities;
    exports bll.enumerators;
    exports bll.valueObjects;
    exports dal.converters;
}