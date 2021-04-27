package view;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;
import javafx.util.Callback;


public class TableColumnButtonsFactory<S> {

    private final HBox buttons;
    private final TableColumn<S, HBox> column;

    public TableColumnButtonsFactory(TableColumn<S, HBox> column, HBox buttons) {
        this.buttons = buttons;
        this.column = column;
    }

    public TableColumn<S, HBox> getColumn() {
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
                            setGraphic(buttons);
                    }
                };
            }
        };
        this.column.setCellFactory(cellFactory);
        return this.column;
    }
}
