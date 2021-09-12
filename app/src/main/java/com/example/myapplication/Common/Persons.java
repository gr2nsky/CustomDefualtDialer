package com.example.myapplication.Common;

import androidx.annotation.NonNull;

import com.example.myapplication.DTO.PersonDTO;

import java.util.ArrayList;

/**
 * @author Yoon
 * @created 2021-09-09
 */
public class Persons {

    public static Persons persons = null;
    private ArrayList<PersonDTO> list = new ArrayList<>();

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

    public void remove(PersonDTO person){
        list.remove(person);
    }

    public void modify(PersonDTO oldPerson, PersonDTO person){
        list.set(list.indexOf(oldPerson), person);
    }

    public void clear(){
        list.clear();
    }

    public void setList(ArrayList<PersonDTO> list) {
        this.list = list;
    }

    public ArrayList<PersonDTO> getList(){
        return list;
    }
}
