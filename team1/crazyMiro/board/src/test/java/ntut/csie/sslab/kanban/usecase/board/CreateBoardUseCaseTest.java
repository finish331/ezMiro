package ntut.csie.sslab.kanban.usecase.board;

import ntut.csie.sslab.ddd.adapter.presenter.cqrs.CqrsCommandPresenter;
import ntut.csie.sslab.kanban.adapter.gateway.repository.springboot.board.BoardRepositoryImpl;
import ntut.csie.sslab.kanban.entity.model.board.Board;
import ntut.csie.sslab.kanban.usecase.board.create.CreateBoardInput;
import ntut.csie.sslab.kanban.usecase.board.create.CreateBoardUseCase;
import ntut.csie.sslab.kanban.usecase.board.create.CreateBoardUseCaseImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateBoardUseCaseTest {

    public BoardRepository boardRepository;

    @BeforeEach
    public void setUp() {
        boardRepository = new BoardRepositoryImpl();
    }

    @Test
    void create_a_board(){
        String boardId = UUID.randomUUID().toString();
        String boardName = "BoardName";
        CreateBoardUseCase createBoardUseCase = new CreateBoardUseCaseImpl(boardRepository);
        CreateBoardInput input = createBoardUseCase.newInput();
        CqrsCommandPresenter output = CqrsCommandPresenter.newInstance();
        input.setBoardId(boardId);
        input.setBoardName(boardName);

        createBoardUseCase.execute(input, output);

        assertEquals(boardId, output.getId());
        assertTrue(boardRepository.getBoardById(input.getBoardId()).isPresent());
        Board board = boardRepository.getBoardById(input.getBoardId()).get();
        assertEquals(boardId, board.getBoardId());
        assertEquals(boardName, board.getBoardName());
    }
}