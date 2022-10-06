package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/** Сущность, описывающая запись в таблице operday(факт открытия/закрытия опердня) */
@Entity
@Table(name = "operday")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperdayEntity {
    @Id
    private Integer id;
    private Boolean opensign;
    private Boolean closesign;
}
