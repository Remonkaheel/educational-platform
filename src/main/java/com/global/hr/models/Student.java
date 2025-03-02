package com.global.hr.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "student") // تأكد من أن الاسم مطابق تمامًا
@PrimaryKeyJoinColumn(
    name = "user_id", 
    referencedColumnName = "id"
)public class Student extends User {

   
}
