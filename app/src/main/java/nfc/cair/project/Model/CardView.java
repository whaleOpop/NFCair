package nfc.cair.project.Model;

import java.util.jar.Attributes;

public class CardView {
    private Integer id;
    private String iconLink;
    private String textName;

    public CardView(Integer id, String iconLink, String textName) {
        this.id = id;
        this.iconLink = iconLink;
        this.textName = textName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIconLink() {
        return iconLink;
    }

    public void setIconLink(String iconLink) {
        this.iconLink = iconLink;
    }

    public String getTextName() {
        return textName;
    }

    public void setTextName(String textName) {
        this.textName = textName;
    }
}
