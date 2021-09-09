package com.example.myapplication.Common;

import com.example.myapplication.DTO.PersonDTO;

import java.util.ArrayList;

/**
 * @author Yoon
 * @created 2021-09-09
 */
public class Persons {

    public static Persons persons = null;
    ArrayList<PersonDTO> list = new ArrayList<>();

    private Persons(){}

    public static Persons getPersons(){
        if(persons == null){
            persons = new Persons();
        }
        return persons;
    }

    public int size(){
        return list.size();
    }

    public void append(PersonDTO person){
        list.add(person);
    }

    public void clear(){
        list.clear();
    }

    public ArrayList<PersonDTO> getList(){
        return list;
    }
}
