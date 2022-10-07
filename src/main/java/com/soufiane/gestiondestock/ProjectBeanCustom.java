package com.soufiane.gestiondestock;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.Date;

public class ProjectBeanCustom extends ProjectBean {

    String etat;

    public ProjectBeanCustom(String titre, int numero, String dateDebut, String dateFin, String etat) {
        super(titre, numero, dateDebut, dateFin);
        this.etat = etat;
    }

    public ProjectBeanCustom(String titre, int numero, String dateDebut, String dateFin) {
        super(titre, numero, dateDebut, dateFin);
    }

    public static void main(String[] args) {

        Date d1 = new Date(2022, 12, 1);
        Date d2 = new Date(2023, 12, 1);

        LocalDate date1 = d1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate date2 = d2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        System.out.println(String.valueOf(ChronoUnit.DAYS.between(date1, date2)));

    }

}


