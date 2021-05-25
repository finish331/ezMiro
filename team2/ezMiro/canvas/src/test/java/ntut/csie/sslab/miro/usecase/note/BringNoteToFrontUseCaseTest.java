package ntut.csie.sslab.miro.usecase.note;

import ntut.csie.sslab.ddd.adapter.presenter.cqrs.CqrsCommandPresenter;
import ntut.csie.sslab.ddd.model.DomainEventBus;
import ntut.csie.sslab.miro.adapter.gateway.eventbus.NotifyBoardAdapter;
import ntut.csie.sslab.miro.adapter.gateway.repository.springboot.board.BoardRepositoryImpl;
import ntut.csie.sslab.miro.adapter.gateway.repository.springboot.note.FigureRepositoryImpl;
import ntut.csie.sslab.miro.entity.model.board.CommittedFigure;
import ntut.csie.sslab.miro.entity.model.note.Coordinate;
import ntut.csie.sslab.miro.usecase.DomainEventListener;
import ntut.csie.sslab.miro.usecase.board.BoardRepository;
import ntut.csie.sslab.miro.usecase.board.create.CreateBoardInput;
import ntut.csie.sslab.miro.usecase.board.create.CreateBoardUseCase;
import ntut.csie.sslab.miro.usecase.board.create.CreateBoardUseCaseImpl;
import ntut.csie.sslab.miro.usecase.eventhandler.NotifyBoard;
import ntut.csie.sslab.miro.usecase.note.create.CreateNoteInput;
import ntut.csie.sslab.miro.usecase.note.create.CreateNoteUseCase;
import ntut.csie.sslab.miro.usecase.note.create.CreateNoteUseCaseImpl;
import ntut.csie.sslab.miro.usecase.note.edit.zorder.front.BringNoteToFrontInput;
import ntut.csie.sslab.miro.usecase.note.edit.zorder.front.BringNoteToFrontUseCase;
import ntut.csie.sslab.miro.usecase.note.edit.zorder.front.BringNoteToFrontUseCaseImpl;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BringNoteToFrontUseCaseTest {
    private FigureRepository figureRepository;
    private BoardRepository boardRepository;
    private DomainEventBus domainEventBus;
    private DomainEventListener eventListener;
    private NotifyBoardAdapter notifyBoardAdapter;

    @Before
    public void setUp() {
        figureRepository = new FigureRepositoryImpl();
        boardRepository = new BoardRepositoryImpl();
        domainEventBus = new DomainEventBus();
        eventListener = new DomainEventListener();
        notifyBoardAdapter = new NotifyBoardAdapter(new NotifyBoard(figureRepository, boardRepository, domainEventBus));

        domainEventBus.register(notifyBoardAdapter);
        domainEventBus.register(eventListener);
    }

    @Test
    public void bring_note1_to_front() {
        String boardId = create_board();
        String note1Id = create_note(boardId); // zindex 0
        String note2Id = create_note(boardId); // zindex 1
        eventListener.clear();
        BringNoteToFrontUseCase bringNoteToFrontUseCase = new BringNoteToFrontUseCaseImpl(figureRepository, domainEventBus);
        BringNoteToFrontInput input = bringNoteToFrontUseCase.newInput();
        CqrsCommandPresenter output = CqrsCommandPresenter.newInstance();
        input.setNoteId(note1Id); // zindex 0 -> 1

        bringNoteToFrontUseCase.execute(input, output);

        assertNotNull(output.getId());
        Map<String, CommittedFigure> committedFigures = boardRepository.findById(boardId).get().getCommittedFigures();
        assertEquals(2, committedFigures.size());
        assertEquals(1, committedFigures.get(note1Id).getZOrder());
        assertEquals(0, committedFigures.get(note2Id).getZOrder());
        assertEquals(1, eventListener.getEventCount());
    }

    private String create_note(String boardId) {
        CreateNoteUseCase createNoteUseCase = new CreateNoteUseCaseImpl(figureRepository, domainEventBus);
        CreateNoteInput input = createNoteUseCase.newInput();
        CqrsCommandPresenter output = CqrsCommandPresenter.newInstance();
        input.setBoardId(boardId);
        input.setCoordinate(new Coordinate(9,26));

        createNoteUseCase.execute(input, output);

        return output.getId();
    }

    private String create_board() {
        CreateBoardUseCase createBoardUseCase = new CreateBoardUseCaseImpl(boardRepository, domainEventBus);
        CreateBoardInput input = createBoardUseCase.newInput();
        CqrsCommandPresenter output = CqrsCommandPresenter.newInstance();
        input.setTeamId("TeamId");
        input.setBoardName("Team2sBoard");

        createBoardUseCase.execute(input, output);

        return output.getId();
    }
}