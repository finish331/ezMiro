package ntut.csie.islab.miro.usecase.stickyNote;

import ntut.csie.islab.miro.adapter.repository.stickyNote.StickyNoteRepository;
import ntut.csie.islab.miro.entity.ShapeKindEnum;
import ntut.csie.islab.miro.entity.Style;
import ntut.csie.sslab.ddd.adapter.gateway.GoogleEventBus;
import ntut.csie.sslab.ddd.adapter.presenter.cqrs.CqrsCommandPresenter;
import ntut.csie.sslab.ddd.model.DomainEventBus;
import ntut.csie.sslab.ddd.usecase.cqrs.ExitCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EditStickyNoteUseCaseTest {
    public DomainEventBus domainEventBus;
    public StickyNoteRepository stickyNoteRepository;
    @BeforeEach
    public void setUp(){
        domainEventBus = new GoogleEventBus();
        stickyNoteRepository = new StickyNoteRepository();
    }

    @Test
    public void test_edit_sticky_note(){
        CreateStickyNoteUseCase createStickyNoteUseCase = new CreateStickyNoteUseCase(stickyNoteRepository, domainEventBus);
        CreateStickyNoteInput createStickyNoteInput = createStickyNoteUseCase.newInput();
        CqrsCommandPresenter createStickyNoteOutput = CqrsCommandPresenter.newInstance();
        createStickyNoteInput.setBoardId(UUID.randomUUID());
        createStickyNoteInput.setPosition(1.0,1.0);
        createStickyNoteInput.setContent("Content");
        createStickyNoteInput.setStyle(new Style(12, ShapeKindEnum.CIRCLE, 87.87, "#948700"));
        createStickyNoteUseCase.execute(createStickyNoteInput, createStickyNoteOutput);

        EditStickyNoteUseCase editStickyNoteUseCase = new EditStickyNoteUseCase(stickyNoteRepository, domainEventBus);
        EditStickyNoteInput input = editStickyNoteUseCase.newInput();
        CqrsCommandPresenter output = CqrsCommandPresenter.newInstance();

        input.setStickyNoteId(UUID.fromString(createStickyNoteOutput.getId()));
        input.setContent("new content");
        input.setStyle(new Style(10, ShapeKindEnum.TRIANGLE, 0.87, "#009487"));
        editStickyNoteUseCase.execute(input, output);

        assertNotNull(output.getId());
        assertEquals(ExitCode.SUCCESS,output.getExitCode());

        assertNotNull(stickyNoteRepository.findById(UUID.fromString(output.getId())).get());

        assertEquals("new content", stickyNoteRepository.findById(UUID.fromString(output.getId())).get().getContent());
        assertEquals(10, stickyNoteRepository.findById(UUID.fromString(output.getId())).get().getStyle().getFontSize());
        assertEquals(ShapeKindEnum.TRIANGLE, stickyNoteRepository.findById(UUID.fromString(output.getId())).get().getStyle().getShape());
        assertEquals(0.87, stickyNoteRepository.findById(UUID.fromString(output.getId())).get().getStyle().getFigureSize(), 0.001);
        assertEquals("#009487", stickyNoteRepository.findById(UUID.fromString(output.getId())).get().getStyle().getColor());
    }

}