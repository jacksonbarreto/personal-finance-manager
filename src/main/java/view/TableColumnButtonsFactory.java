package view;

import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.util.List;

public class TableColumnButtonsFactory<S> {

    private final List<Button> buttons;
    private final TableColumn<S, Button> column;

    public TableColumnButtonsFactory(TableColumn<S, Button> column, List<Button> buttons) {
        this.buttons = buttons;
        this.column = column;
    }

    public TableColumn<S, Button> getColumn() {
        Callback<TableColumn<S, Button>, TableCell<S, Button>> cellFactory = new Callback<>() {

            @Override
            public TableCell<S, Button> call(final TableColumn<S, Button> sButtonTableColumn) {
                return new TableCell<>() {
                    @Override
                    public void updateItem(Button item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty)
                            setGraphic(null);
                        else
                            buttons.forEach(this::setGraphic);
                    }
                };
            }
        };
        return this.column;
    }
}
