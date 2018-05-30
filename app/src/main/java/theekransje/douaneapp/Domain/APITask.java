package theekransje.douaneapp.Domain;

import org.json.JSONObject;

import java.util.ArrayList;

import theekransje.douaneapp.API.APIMethodes;
import theekransje.douaneapp.Interfaces.OnApiCallback;


/**
 * Created by Sander on 5/29/2018.
 */

public class APITask {
    private JSONObject jsonObject;
    private APIMethodes apiMethod;
    private String endpoint;
    private String id;


    public static final String TABLE_NAME = "Tasks";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_OBJECT = "JSONOBJECT";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_OBJECT + " TEXT"
                    + ")";


    public APITask(JSONObject jsonObject, APIMethodes apiMethod, String endpoint) {
        this.jsonObject = jsonObject;
        this.apiMethod = apiMethod;
        this.endpoint = endpoint;
    }


    public APITask(JSONObject jsonObject) {
        try{
            this.apiMethod = (APIMethodes) jsonObject.get("METHOD");
            this.endpoint = (String) jsonObject.get("ENDPOINT");
            this.jsonObject = (JSONObject) jsonObject.get("JSONOBJECT");

        }catch (Exception e){
        }
    }

    public JSONObject getTaskAsJSON(){
        try{
            JSONObject j = new JSONObject();

            j.put("METHOD",this.apiMethod);
            j.put("ENDPOINT", this.endpoint);
            j.put("JSONOBJECT", this.jsonObject);

            return j;
        }catch (Exception e){

        }


        return null;
    }

    public JSONObject getJSONOBJECT(){

        return this.jsonObject;
    }


    public APIMethodes getApiMethod() {
        return apiMethod;
    }

    public String getEndpoint() {
        return endpoint;
    }

    @Override
    public String toString() {
        return "" + this.endpoint + " " + this.apiMethod.toString() + " " + this.jsonObject.toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
