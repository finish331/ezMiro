package ntut.csie.team5.usecase;

import ntut.csie.sslab.ddd.adapter.presenter.cqrs.CqrsCommandPresenter;
import ntut.csie.sslab.ddd.model.DomainEventBus;
import ntut.csie.team5.adapter.gateway.repository.springboot.board.BoardRepositoryImpl;
import ntut.csie.team5.adapter.gateway.repository.springboot.figure.FigureRepositoryImpl;
import ntut.csie.team5.adapter.project.ProjectRepositoryImpl;
import ntut.csie.team5.usecase.board.BoardRepository;
import ntut.csie.team5.usecase.board.create.CreateBoardInput;
import ntut.csie.team5.usecase.board.create.CreateBoardUseCase;
import ntut.csie.team5.usecase.board.create.CreateBoardUseCaseImpl;
import ntut.csie.team5.usecase.eventhandler.NotifyBoard;
import ntut.csie.team5.usecase.figure.note.FigureRepository;
import ntut.csie.team5.usecase.figure.note.post.PostNoteInput;
import ntut.csie.team5.usecase.figure.note.post.PostNoteUseCase;
import ntut.csie.team5.usecase.figure.note.post.PostNoteUseCaseImpl;
import ntut.csie.team5.usecase.project.ProjectRepository;
import ntut.csie.team5.usecase.project.create.CreateProjectInput;
import ntut.csie.team5.usecase.project.create.CreateProjectUseCase;
import ntut.csie.team5.usecase.project.create.CreateProjectUseCaseImpl;
import org.junit.Before;

import java.awt.*;
import java.util.UUID;

public abstract class AbstractTest {

    public ProjectRepository projectRepository;
    public BoardRepository boardRepository;
    public FigureRepository figureRepository;
    public DomainEventBus domainEventBus;

    public NotifyBoard notifyBoard;

    public String teamId;
    public String projectId;
    public String projectName;
    public String boardId;
    public String boardName;
    public Point defaultPosition;
    public Color defaultColor;

    @Before
    public void setUp() throws Exception {
        projectRepository = new ProjectRepositoryImpl();
        boardRepository = new BoardRepositoryImpl();
        figureRepository = new FigureRepositoryImpl();
        domainEventBus = new DomainEventBus();

        notifyBoard = new NotifyBoard(boardRepository, domainEventBus);

        teamId = UUID.randomUUID().toString();
        projectId = UUID.randomUUID().toString();
        projectName = "ezmiro project";
        boardId = UUID.randomUUID().toString();
        boardName = "ezmiro board";
        defaultPosition = new Point(0,0);
        defaultColor = Color.BLACK;
    }

    public String createProject(String teamId, String projectName) {
        CreateProjectUseCase createProjectUseCase = new CreateProjectUseCaseImpl(projectRepository, domainEventBus);
        CreateProjectInput createProjectInput = createProjectUseCase.newInput();
        CqrsCommandPresenter createProjectOutput = CqrsCommandPresenter.newInstance();

        createProjectInput.setTeamId(teamId);
        createProjectInput.setProjectName(projectName);

        createProjectUseCase.execute(createProjectInput, createProjectOutput);

        return createProjectOutput.getId();
    }

    public String createBoard(String projectId, String boardName) {
        CreateBoardUseCase createBoardUseCase = new CreateBoardUseCaseImpl(boardRepository, domainEventBus);
        CreateBoardInput createBoardInput = createBoardUseCase.newInput();
        CqrsCommandPresenter createBoardOutput = CqrsCommandPresenter.newInstance();

        createBoardInput.setProjectId(projectId);
        createBoardInput.setBoardName(boardName);

        createBoardUseCase.execute(createBoardInput, createBoardOutput);
        return createBoardOutput.getId();
    }

    public String postNote(String boardId, Point position, Color color) {
        PostNoteUseCase postNoteUseCase = new PostNoteUseCaseImpl(figureRepository, domainEventBus);
        PostNoteInput postNoteInput = postNoteUseCase.newInput();
        CqrsCommandPresenter postNoteOutput = CqrsCommandPresenter.newInstance();

        postNoteInput.setBoardId(boardId);
        postNoteInput.setPosition(position);
        postNoteInput.setColor(color);

        postNoteUseCase.execute(postNoteInput, postNoteOutput);
        return postNoteOutput.getId();
    }
}