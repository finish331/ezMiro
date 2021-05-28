package ntut.csie.selab.usecase.widget;

import ntut.csie.selab.adapter.gateway.repository.springboot.widget.WidgetRepositoryPeer;
import ntut.csie.selab.adapter.widget.WidgetRepositoryImpl;
import ntut.csie.selab.entity.model.widget.Coordinate;
import ntut.csie.selab.entity.model.widget.StickyNote;
import ntut.csie.selab.entity.model.widget.Widget;
import ntut.csie.selab.model.DomainEventBus;
import ntut.csie.selab.usecase.JpaApplicationTest;
import ntut.csie.selab.usecase.widget.edit.text.EditTextOfStickyNoteInput;
import ntut.csie.selab.usecase.widget.edit.text.EditTextOfStickyNoteOutput;
import ntut.csie.selab.usecase.widget.edit.text.EditTextOfStickyNoteUseCase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes= JpaApplicationTest.class)
@Rollback(false)
public class EditTextOfStickyNoteUseCaseTest {

    @Autowired
    private WidgetRepositoryPeer widgetRepositoryPeer;

    @Test
    public void edit_text_of_sticky_note_should_succeed() {
        // Arrange
        WidgetRepository widgetRepository = new WidgetRepositoryImpl(widgetRepositoryPeer);
        String stickyNoteId = "1";
        Coordinate stickyNoteCoordinate = new Coordinate(1, 1, 2, 2);
        Widget stickyNote = new StickyNote(stickyNoteId, "0", stickyNoteCoordinate);
        widgetRepository.save(stickyNote);
        DomainEventBus domainEventBus = new DomainEventBus();
        EditTextOfStickyNoteUseCase editTextOfStickyNoteUseCase = new EditTextOfStickyNoteUseCase(widgetRepository, domainEventBus);
        EditTextOfStickyNoteInput input = new EditTextOfStickyNoteInput();
        EditTextOfStickyNoteOutput output = new EditTextOfStickyNoteOutput();
        input.setStickyNoteId(stickyNoteId);
        input.setText("modified text");

        // Act
        editTextOfStickyNoteUseCase.execute(input, output);

        // Assert
        Assert.assertEquals("modified text", output.getText());
        Assert.assertEquals(1, domainEventBus.getCount());
    }
}
