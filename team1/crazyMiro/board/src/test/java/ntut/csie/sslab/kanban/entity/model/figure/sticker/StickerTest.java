package ntut.csie.sslab.kanban.entity.model.figure.sticker;

import ntut.csie.sslab.kanban.entity.model.figure.Coordinate;
import ntut.csie.sslab.kanban.entity.model.figure.Sticker;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StickerTest {


    @Test
    public void changer_sticker_content(){
        String boardId = UUID.randomUUID().toString();
        String stickerId = UUID.randomUUID().toString();
        String content = "stickerCreated";
        String newContent = "stickerContentChanged";
        int size = 10;
        String color = "black";
        long x = new Random().nextLong();
        long y = new Random().nextLong();
        Coordinate position = new Coordinate(x, y);
        Sticker sticker = new Sticker(boardId, stickerId, content, size, color, position);

        sticker.changeContent(newContent);

        assertEquals(newContent, sticker.getContent());
    }

    @Test
    public void move_position() {
        String boardId = UUID.randomUUID().toString();
        String stickerId = UUID.randomUUID().toString();
        String content = "stickerCreated";
        int size = 10;
        String color = "black";
        long x = new Random().nextLong();
        long y = new Random().nextLong();
        Coordinate position = new Coordinate(x, y);
        Sticker sticker = new Sticker(boardId, stickerId, content, size, color, position);
        Coordinate newPosition = new Coordinate(new Random().nextLong(), new Random().nextLong());

        sticker.move(newPosition);

        assertTrue(newPosition.equals(sticker.getPosition()));
    }
}