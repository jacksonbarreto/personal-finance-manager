package view;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.util.function.Supplier;

public class TableColumnButtonsFactory<S> {

    private final Supplier<HBox> buttonFactory;
    private final String columnName;

    public TableColumnButtonsFactory(String columnName, Supplier<HBox> buttonFactory) {
        this.buttonFactory = buttonFactory;
        this.columnName = columnName;
    }

    public TableColumn<S, HBox> getColumn() {
        final TableColumn<S, HBox> column = new TableColumn<>(columnName);
        Callback<TableColumn<S, HBox>, TableCell<S, HBox>> cellFactory = new Callback<>() {

            @Override
            public TableCell<S, HBox> call(final TableColumn<S, HBox> sButtonTableColumn) {
                return new TableCell<>() {
                    @Override
                    public void updateItem(HBox item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty)
                            setGraphic(null);
                        else
                            setGraphic(buttonFactory.get());
                    }
                };
            }
        };
        column.setCellFactory(cellFactory);
        return column;
    }

    public Callback<TableColumn<S, HBox>, TableCell<S, HBox>> getFactory() {
        return new Callback<>() {
            @Override
            public TableCell<S, HBox> call(final TableColumn<S, HBox> sButtonTableColumn) {
               TableCell<S, HBox> cell =  new TableCell<>() {
                    @Override
                    public void updateItem(HBox item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty)
                            setGraphic(null);
                        else
                            setGraphic(buttonFactory.get());
                    }
                };
                return cell;
            }
        };
    }
}
