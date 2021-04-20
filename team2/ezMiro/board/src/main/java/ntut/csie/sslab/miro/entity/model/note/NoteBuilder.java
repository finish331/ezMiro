package ntut.csie.sslab.miro.entity.model.note;

import java.util.UUID;

public class NoteBuilder {
    private String boardId;
    private String noteId;
    private String description;
    private String color;

    public static NoteBuilder newInstance() { return new NoteBuilder(); }

    public NoteBuilder boardId(String boardId) {
        this.boardId = boardId;
        return this;
    }

    public NoteBuilder noteId(String noteId) {
        this.noteId = noteId;
        return this;
    }

    public NoteBuilder description(String description) {
        this.description = description;
        return this;
    }

    public NoteBuilder color(String color) {
        this.color = color;
        return this;
    }

    public Note build() {
        noteId = UUID.randomUUID().toString();
        Note note = new Note(boardId, noteId, description, color);
        return note;
    }
}