package ntut.csie.team5.adapter.controller.websocket;

import ntut.csie.sslab.ddd.adapter.presenter.cqrs.CqrsCommandPresenter;
import ntut.csie.team5.application.springboot.web.config.websocket.DomainEventEncoder;
import ntut.csie.team5.application.springboot.web.config.websocket.EndPointConfiguration;
import ntut.csie.team5.usecase.board.BoardSessionBroadcaster;
import ntut.csie.team5.usecase.board.enter.EnterBoardInput;
import ntut.csie.team5.usecase.board.enter.EnterBoardUseCase;
import ntut.csie.team5.usecase.board.leave.LeaveBoardInput;
import ntut.csie.team5.usecase.board.leave.LeaveBoardUseCase;
import ntut.csie.team5.usecase.board.move_cursor.MoveCursorInput;
import ntut.csie.team5.usecase.board.move_cursor.MoveCursorUseCase;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@Component
@ServerEndpoint(value = "/WebSocketServer/{boardId}/{userId}",
                encoders = {DomainEventEncoder.class},
                configurator = EndPointConfiguration.class)
public class WebSocketController {

    private EnterBoardUseCase enterBoardUseCase;
    private LeaveBoardUseCase leaveBoardUseCase;
    private MoveCursorUseCase moveCursorUseCase;
    private BoardSessionBroadcaster boardSessionBroadcaster;

    @Autowired
    public void setEnterBoardUseCase(EnterBoardUseCase enterBoardUseCase) {
        this.enterBoardUseCase = enterBoardUseCase;
    }
    @Autowired
    public void setLeaveBoardUseCase(LeaveBoardUseCase leaveBoardUseCase) {
        this.leaveBoardUseCase = leaveBoardUseCase;
    }

    @Autowired
    public void setMoveCursorUseCase(MoveCursorUseCase moveCursorUseCase) {
        this.moveCursorUseCase = moveCursorUseCase;
    }

    @Autowired
    public void setBoardSessionBroadcaster(BoardSessionBroadcaster boardSessionBroadcaster) {
        this.boardSessionBroadcaster = boardSessionBroadcaster;
    }

    @OnOpen
    public void onOpen(@PathParam(value = "boardId") String boardId, @PathParam(value = "userId") String userId, Session session) {
        EnterBoardInput input = enterBoardUseCase.newInput();
        CqrsCommandPresenter presenter = CqrsCommandPresenter.newInstance();

        input.setBoardId(boardId);
        input.setUserId(userId);

        enterBoardUseCase.execute(input, presenter);

        String message = "有新成員[" + userId + "]加入畫布!";
        System.out.println(message);
        ((WebSocketBroadcaster) boardSessionBroadcaster).addSession(presenter.getId(), session);
//        webSocketBroadcaster.sendMessageToAllUser(boardId, message);
    }

    @OnClose
    public void onClose(@PathParam(value = "boardId") String boardId, @PathParam(value = "userId") String userId, Session session) {
        LeaveBoardInput input = leaveBoardUseCase.newInput();
        CqrsCommandPresenter presenter = CqrsCommandPresenter.newInstance();
        String boardSessionId = ((WebSocketBroadcaster) boardSessionBroadcaster).getBoardSessionIdBySessionId(session.getId());

        input.setBoardId(boardId);
        input.setUserId(userId);
        input.setBoardSessionId(boardSessionId);

        leaveBoardUseCase.execute(input, presenter);

        String message = "成員[" + userId + "]退出畫布!";
        System.out.println(message);
        ((WebSocketBroadcaster) boardSessionBroadcaster).removeSession(presenter.getId());
//        ((WebSocketBroadcaster) boardSessionBroadcaster).sendMessageToAllUser(boardId, message);
    }

    @OnMessage
    public void onMessage(@PathParam(value = "boardId") String boardId, @PathParam(value = "userId") String userId, String message, Session session) {
        MoveCursorInput input = moveCursorUseCase.newInput();
        CqrsCommandPresenter presenter = CqrsCommandPresenter.newInstance();

        input.setBoardId(boardId);
        input.setUserId(userId);

        try {
            JSONObject pointer = new JSONObject(message);
            input.setPositionX(pointer.getInt("x"));
            input.setPositionY(pointer.getInt("y"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        moveCursorUseCase.execute(input, presenter);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("錯誤:" + throwable);
        try {
            session.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        throwable.printStackTrace();
    }
}
