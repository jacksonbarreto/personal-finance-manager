package view;

import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class TableRowFactory<S> {

    public TableRowFactory() {
    }

    public Callback<TableView<S>, TableRow<S>> getRowFactory() {
        return tableView -> {
            final TableRow<S> row = new TableRow<>();
            row.hoverProperty().addListener((observable) -> {
                final S item = row.getItem();
                if (row.isHover() && item != null)
                    row.getTableView().getSelectionModel().clearAndSelect(row.getIndex());
            });
            return row;
        };
    }
}
