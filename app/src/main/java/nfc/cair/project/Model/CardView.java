package nfc.cair.project.Model;

import java.util.jar.Attributes;

public class CardView {
    private Integer id;
    private String doctorNumber;
    private String nameDisease;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDoctorNumber() {
        return doctorNumber;
    }

    public void setDoctorNumber(String doctorNumber) {
        this.doctorNumber = doctorNumber;
    }

    public String getNameDisease() {
        return nameDisease;
    }

    public void setNameDisease(String nameDisease) {
        this.nameDisease = nameDisease;
    }

    public CardView(Integer id, String doctorNumber, String nameDisease) {
        this.id = id;
        this.doctorNumber = doctorNumber;
        this.nameDisease = nameDisease;
    }
}
