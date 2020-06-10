package com.skillbox.socialnetwork.main.exception.not.found;

public class PersonNotFoundException extends NotFoundException{
    public PersonNotFoundException(String message, String error){
        super(error, message);
    }
}
