package ntut.csie.team5.usecase.eventhandler;

import com.google.common.eventbus.Subscribe;
import ntut.csie.sslab.ddd.model.DomainEvent;
import ntut.csie.sslab.ddd.model.DomainEventBus;
import ntut.csie.team5.entity.model.board.Board;
import ntut.csie.team5.entity.model.board.BoardSession;
import ntut.csie.team5.entity.model.board.event.CursorCreated;
import ntut.csie.team5.entity.model.board.event.CursorDeleted;
import ntut.csie.team5.entity.model.board.event.CursorMoved;
import ntut.csie.team5.entity.model.figure.event.FigureCreated;
import ntut.csie.team5.entity.model.figure.event.FigureDeleted;
import ntut.csie.team5.entity.model.figure.note.event.NoteColorChanged;
import ntut.csie.team5.entity.model.figure.note.event.NoteMoved;
import ntut.csie.team5.entity.model.figure.note.event.NoteResized;
import ntut.csie.team5.entity.model.figure.note.event.NoteTextEdited;
import ntut.csie.team5.usecase.board.BoardRepository;
import ntut.csie.team5.usecase.board.BoardSessionBroadcaster;
import ntut.csie.team5.usecase.board.getcontent.GetBoardContentUseCase;

import java.util.List;

public class NotifyBoardSessionBroadcaster {

    private final BoardRepository boardRepository;
    private final DomainEventBus domainEventBus;
    private final GetBoardContentUseCase getBoardContentUseCase;
    private final BoardSessionBroadcaster boardSessionBroadcaster;

    public NotifyBoardSessionBroadcaster(BoardRepository boardRepository, DomainEventBus domainEventBus,
                                         GetBoardContentUseCase getBoardContentUseCase,
                                         BoardSessionBroadcaster boardSessionBroadcaster) {
        this.boardRepository = boardRepository;
        this.domainEventBus = domainEventBus;
        this.getBoardContentUseCase = getBoardContentUseCase;
        this.boardSessionBroadcaster = boardSessionBroadcaster;
    }

    @Subscribe
    public void whenFigureCreated(FigureCreated figureCreated) {
        broadcastEvent(figureCreated.boardId(), figureCreated);
    }

    @Subscribe
    public void whenFigureDeleted(FigureDeleted figureDeleted) {
        broadcastEvent(figureDeleted.boardId(), figureDeleted);
    }

    @Subscribe
    public void whenNoteMoved(NoteMoved noteMoved) {
        broadcastEvent(noteMoved.boardId(), noteMoved);
    }

    @Subscribe
    public void whenNoteColorChanged(NoteColorChanged noteColorChanged) {
        broadcastEvent(noteColorChanged.boardId(), noteColorChanged);
    }

    @Subscribe
    public void whenNoteResized(NoteResized noteResized) {
        broadcastEvent(noteResized.boardId(), noteResized);
    }

    @Subscribe
    public void whenNoteTextEdited(NoteTextEdited noteTextEdited) {
        broadcastEvent(noteTextEdited.boardId(), noteTextEdited);
    }

    @Subscribe
    public void whenCursorCreated(CursorCreated cursorCreated) {
        broadcastEvent(cursorCreated.boardId(), cursorCreated);
    }

    @Subscribe
    public void whenCursorMoved(CursorMoved cursorMoved) {
        broadcastEvent(cursorMoved.boardId(), cursorMoved);
    }

    @Subscribe
    public void whenCursorDelete(CursorDeleted CursorDeleted) {
        broadcastEvent(CursorDeleted.boardId(), CursorDeleted);
    }

    private void broadcastEvent(String boardId, DomainEvent domainEvent) {
        Board board = boardRepository.findById(boardId).orElse(null);
        if (null == board) {
            return;
        }

        List<BoardSession> boardSessions = board.getBoardSessions();
        boardSessions.forEach(boardSession -> {
            boardSessionBroadcaster.broadcast(boardSession.boardSessionId(), domainEvent);
        });
    }
}
