package ntut.csie.selab.entity.model.board;

import java.util.List;

public class CommittedWidget {
    private String boardId;
    private String widgetId;
    private int zOrder;

    public CommittedWidget(String boardId, String widgetId, int zOrder) {
        this.boardId = boardId;
        this.widgetId = widgetId;
        this.zOrder = zOrder;
    }

    public String getBoardId() {
        return boardId;
    }

    public String getWidgetId() {
        return widgetId;
    }

    public int getZOrder() {
        return zOrder;
    }
}
