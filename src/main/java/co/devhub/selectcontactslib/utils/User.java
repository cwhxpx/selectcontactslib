package co.devhub.selectcontactslib.utils;

import co.devhub.retrievecontacts.Contact;

/**
 * Created by Administrator on 2016/1/8.
 */
public class User extends Contact{
    //used for sort
    private String letter;

    //the user has been selected as sharelocation notificateer.
    private boolean selected = false;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }
}
