package com.incquerylabs.arrowhead.gen;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.UUID;

public class JsonGenerator {

    public static JSONObject correctSystemRegistrationForm(){
        JSONObject out = new JSONObject();
        out.put("systemName", UUID.randomUUID().toString());
        out.put("address", "");
        out.put("address", "");
        out.put("address", "");
        return out;
    }

    public static JSONArray randomSystemRegistrationForm(){
        return null;
    }

    public static JSONArray correctSystemRegistrationForm(int number){
        return null;
    }

    public static JSONArray mixedSystemRegistrationForm(int number){
        return null;
    }

    public static JSONArray allWrongSystemRegistrationForm(){
        return null;
    }

    public static JSONObject correctServiceRegistrationForm(){
        return null;
    }

    public static JSONArray randomServiceRegistrationForm(){
        return null;
    }

    public static JSONArray correctServiceRegistrationForms(int number){
        return null;
    }

    public static JSONArray mixedServiceRegistrationForms(int number){
        return null;
    }

    public static JSONArray allWrongServiceRegistrationForms(){
        return null;
    }

    public static JSONObject correctArrowheadJson(int systems, int services, int lines){
        return null;
    }

    public static JSONArray randomArrowheadJson(int systems, int services, int lines){
        return null;
    }

    public static JSONArray correctArrowheadJsons(int number, int systems, int services, int lines){
        return null;
    }

    public static JSONArray mixedArrowheadJsons(int number, int systems, int services, int lines){
        return null;
    }

    public static JSONArray allWrongArrowheadJsons(){
        return null;
    }
}
