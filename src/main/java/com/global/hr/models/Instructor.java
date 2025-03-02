package com.global.hr.models;

import java.util.List;
import jakarta.persistence.*;


@Entity
@Table(name = "instructor")
public class Instructor extends User {


    private String professionalTitle;

    private List<String> personalLinks;

    

    public String getProfessionalTitle() {
        return professionalTitle;
    }

    public void setProfessionalTitle(String professionalTitle) {
        this.professionalTitle = professionalTitle;
    }

    public List<String> getPersonalLinks() {
        return personalLinks;
    }

    public void setPersonalLinks(List<String> personalLinks) {
        this.personalLinks = personalLinks;
    }
}