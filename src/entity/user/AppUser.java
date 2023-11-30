package entity.user;

import entity.petProfile.PetProfile;

import java.util.ArrayList;
import java.util.List;

import static entity.Constants.*;

public class AppUser {
    private String username;

    private String photoUrl;

    private String bio;
    private String password;
    private String address;
    private String preferredSex;
    private String preferredSize;
    private List<Integer> favPet;
    private List<String> roles;

    private List<Integer> myPet;


    public AppUser(){}
    public AppUser(String username, String password, String address) {
        /*Require password is valid */
        this.username = username;
        this.password = password;
        this.address = address;
        this.favPet = new ArrayList<Integer>();
        this.roles = new ArrayList<String>();
        roles.add(PETFINDER);
    }

    public void certifyAsPetOwner() {
       /*require the user is not a petOwner*/
        roles.add(PETOWNER);
        myPet = new ArrayList<Integer>();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getBio() {
        return bio;
    }

    public String getPreferredSex() { return preferredSex; }

    public String getPreferredSize() {return preferredSize; }

    public String getAddress() {
        return address;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPreferredSize(String size) {
        this.preferredSize = preferredSize;
    }

    public void setPreferredSex(String sex) {
        this.preferredSex = preferredSex;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isPetFinder(){
        return roles.contains(PETFINDER);
    }

    public boolean isPetOwner(){
        return roles.contains(PETOWNER);
    }
    public boolean profileIsFav(PetProfile profile){
        return favPet.contains(profile.getId());
    }
    public void addFavProfile(int petID){
        favPet.add(petID);
    }
    public void deleteFavProfile(int petId){
        /*petId must be in favPet*/
        favPet.remove(petId);
    }
    public List<String> getRoles(){
        return new ArrayList<String>(roles);
    }
    public List<Integer> getFavPet(){
        ArrayList<Integer> favPet = new ArrayList<>();
        favPet.addAll(this.favPet);
        return favPet;
    } //new copy
}