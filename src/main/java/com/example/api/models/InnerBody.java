package com.example.api.models;

import java.util.Objects;

public class InnerBody {
    private Line line;
    private String heading;
    private String message;


    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InnerBody innerBody = (InnerBody) o;
        return Objects.equals(line, innerBody.line) &&
                Objects.equals(heading, innerBody.heading) &&
                Objects.equals(message, innerBody.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(line, heading, message);
    }
}
