package com.example.masterdetail;

import java.util.Objects;

public class Note {
    private long id;

    private String title;

    private String detail;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", detail='" + detail + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return getId() == note.getId() &&
                getTitle().equals(note.getTitle()) &&
                getDetail().equals(note.getDetail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getDetail());
    }
}
