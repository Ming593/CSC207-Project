package DataAcessObject;

import Entity.User.AppUser;
import Entity.User.AppUserFactory;
import com.google.gson.Gson;

import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;


public class CommonUserDataAccessObject implements UserDataAcessInterface {
    private final String add = "add";
    private final String update = "update";

    private final String collection = "users";
    private final String database = "207DataBase";
    private final String dataSource = "ClusterCSC207Pro";
    private final HashMap<String, AppUser> nameToUser = new HashMap<>();
    private final OkHttpClient client = new OkHttpClient().newBuilder().build();
    private final String apikey = "";

    private final String baseURL = "https://us-east-2.aws.data.mongodb-api.com/app/data-xfyvk/endpoint/data/v1/action/";


    /*Class make assumption that the database follows the representation invariant:
    * users all have different usernames
    * the database store only users
    *
    * */

//    public class UserDAOException extends Exception{
//        public UserDAOException(String message){
//            super(message);
//        }
//    }

    public CommonUserDataAccessObject(){
        ArrayList<AppUser> users = retrieveAllUser();
        for(AppUser user: users){
            nameToUser.put(user.getUsername(), user);
        }
    }
    //Helper
    private String retrieveAllUserJson() {
        String json = getAllconvertMongoMatchJsonFormat();
        RequestBody body = RequestBody.create(json.getBytes(StandardCharsets.UTF_8));
        Request request = new Request.Builder().
                url(baseURL + "find")
                .addHeader("api-key", apikey)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException(String.valueOf(response));
            }
            return response.body().string();
        } catch (IOException e) {
            return e.getMessage();
        }

    }
    private ArrayList<AppUser> retrieveAllUser(){
        String users = new JSONObject(retrieveAllUserJson()).getJSONArray("documents").toString();
        Type listUser = new TypeToken<ArrayList<AppUser>>(){}.getType();
        Gson gson = new Gson();
        return gson.fromJson(users, listUser);
    }


    private String convertMongodMatchJsonFormat(AppUser user, String operation) {

        String userJsonStr = new Gson().toJson(user);
        JSONObject userJson = new JSONObject(userJsonStr);
        JSONObject dataLoadingJson = new JSONObject();
        dataLoadingJson.put("collection", collection);
        dataLoadingJson.put("database", database);
        dataLoadingJson.put("dataSource", dataSource);
        if (operation.equals(add)) {
            dataLoadingJson.put("document", userJson);
        } else if (operation.equals(update)) {
            JSONObject compFilt = new JSONObject();
            compFilt.put("username", user.getUsername());
            dataLoadingJson.put("filter", compFilt);
            JSONObject compUpda = new JSONObject();
            compUpda.put("$set", userJson);
            dataLoadingJson.put("update", compUpda);

        }
        return dataLoadingJson.toString();


    }

    private String deleteconvertMongoMatchJsonFormat(String username) {
        JSONObject dataLoadingJson = new JSONObject();
        dataLoadingJson.put("collection", collection);
        dataLoadingJson.put("database", database);
        dataLoadingJson.put("dataSource", dataSource);
        JSONObject compFilt = new JSONObject();
        compFilt.put("username", username);
        dataLoadingJson.put("filter", compFilt);
        return dataLoadingJson.toString();
    }

    private String getAllconvertMongoMatchJsonFormat() {
        JSONObject dataLoadingJson = new JSONObject();
        dataLoadingJson.put("collection", collection);
        dataLoadingJson.put("database", database);
        dataLoadingJson.put("dataSource", dataSource);
        dataLoadingJson.put("filter", JSONObject.NULL);
        JSONObject compProj = new JSONObject();
        compProj.put("_id", 0);
        dataLoadingJson.put("projection", compProj);
        return dataLoadingJson.toString();
    }
    @Override
    public void add(AppUser user) {
        if (exist(user.getUsername())){
            throw new RuntimeException("User already exists");
        }
        String json = convertMongodMatchJsonFormat(user, add);
        RequestBody body = RequestBody.create(json.getBytes(StandardCharsets.UTF_8));
        Request request = new Request.Builder()
                .url(baseURL + "insertOne")
                .addHeader("Content-Type", "application/json")
                .addHeader("api-key", apikey)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("API call fail for reason" + response.body().string());
            }
            nameToUser.put(user.getUsername(),user);
        }
        catch(IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void update(AppUser user){
        if(!exist(user.getUsername())){
            throw new RuntimeException("User does not exists");

        }
        String json = convertMongodMatchJsonFormat(user, update);
        RequestBody body = RequestBody.create(json.getBytes(StandardCharsets.UTF_8));
        Request request = new Request.Builder()
                .url(baseURL + "updateOne")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("api-key", apikey)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("API call fail for reason" + response.body().string());
            }

        }
        catch(IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void delete(String username) {
        if(!exist(username)){
            throw new RuntimeException("User does not exists");
        }
        String json = deleteconvertMongoMatchJsonFormat(username);
        RequestBody body = RequestBody.create(json.getBytes(StandardCharsets.UTF_8));
        Request request = new Request.Builder()
                .url(baseURL + "deleteOne")
                .addHeader("api-key", apikey)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("API call fail for reason" + response.body().string());
            }
            nameToUser.remove(username);
        }
        catch(IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public AppUser retrieve(String username) {
        if (!exist(username)){
            throw new RuntimeException("User does not exists ");
        }
        return nameToUser.get(username);
    }

    @Override
    public boolean exist(String username) {
        return nameToUser.containsKey(username);
    }




}
