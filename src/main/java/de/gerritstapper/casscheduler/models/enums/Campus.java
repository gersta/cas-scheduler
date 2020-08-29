package de.gerritstapper.casscheduler.models.enums;

public enum Campus {
    RV("Ravensburg"),
    HDH("Heidenheim"),
    HN("Heilbronn"),
    KA("Karlsruhe"),
    MOS("Mosbach"),
    LO("Lörrach"),
    FN("Friedrichshafen"),
    S("Stuttgart"),
    MA("Mannheim");

    private String campus;

    Campus(String campus) {
        this.campus = campus;
    }

    public String getCampus() {
        return campus;
    }
}
