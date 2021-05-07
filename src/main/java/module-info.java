module personal.finance.manager {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.persistence;
    requires java.sql;
    requires java.naming;
    requires org.hibernate.orm.core;
    requires com.fasterxml.classmate;
    requires net.bytebuddy;
    requires com.sun.xml.bind;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;

    opens bll.entities;
    opens dal.infra;
    opens dal.converters to org.hibernate.orm.core;
    opens view;
    exports view;
    exports bll;
    exports bll.factories;
    exports bll.builders;
    exports bll.exceptions;
    exports bll.entities;
    exports bll.enumerators;
    exports bll.valueObjects;
    exports dal.converters;
    exports dal.infra;
}