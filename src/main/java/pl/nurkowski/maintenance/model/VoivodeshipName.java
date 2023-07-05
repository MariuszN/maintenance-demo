package pl.nurkowski.maintenance.model;

import java.util.HashMap;
import java.util.Map;

public enum VoivodeshipName {

    DOLNOSLASKIE("02", "Dolnośląskie"),
    KUJAWSKO_POMORSKIE("04", "Kujawsko-Pomorskie"),
    LUBELSKIE("06", "Lubelskie"),
    LUBUSKIE("08", "Lubuskie"),
    LODZKIE("10", "Łódzkie"),
    MALOPOLSKIE("12", "Małopolskie"),
    MAZOWIECKIE("14", "Mazowieckie"),
    OPOLSKIE("16", "Opolskie"),
    PODKARPACKIE("18", "Podkarpackie"),
    PODLASKIE("20", "Podlaskie"),
    POMORSKIE("22", "Pomorskie"),
    SLASKIE("24", "Śląskie"),
    SWIETOKRZYSKIE("26", "Świętokrzyskie"),
    WARMINSKO_MAZURSKIE("28", "Warmińsko-Mazurskie"),
    WIELKOPOLSKIE("30", "Wielkopolskie"),
    ZACHODNIOPOMORSKIE("32", "Zachodniopomorskie");

    private String code;
    private String name;

    VoivodeshipName(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static Map<String, String> getCodeMap() {
        Map<String, String> codeMap = new HashMap<>();
        for (VoivodeshipName voivodeship : values()) {
            codeMap.put(voivodeship.getCode(), voivodeship.getName());
        }
        return codeMap;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("VoivodeshipName{");
        sb.append("code=").append(code);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
