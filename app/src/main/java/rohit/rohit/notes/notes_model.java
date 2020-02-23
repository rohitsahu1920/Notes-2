package rohit.rohit.notes;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class notes_model implements Serializable {

    private String title;
    private String body;
    private byte[] image;

    public notes_model(String title, String body, byte[] image) {
        this.title = title;
        this.body = body;
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public byte[] getImage() {
        return image;
    }

    @NonNull
    @Override
    public String toString()
    {
        return getTitle();
    }
}
