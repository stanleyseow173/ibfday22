package sg.edu.nus.IBF_paf_day22.model;

import java.io.StringReader;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;

public class RSVP {
    private Integer id;
    private String name;
    private String email;
    private String phone;
    private DateTime confirmation_date;
    private String comments;

    public RSVP() {
    }

    public RSVP(Integer id, String name, String email, String phone, DateTime confirmation_date, String comments) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.confirmation_date = confirmation_date;
        this.comments = comments;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public DateTime getConfirmation_date() {
        return confirmation_date;
    }
    public void setConfirmation_date(DateTime confirmation_date) {
        this.confirmation_date = confirmation_date;
    }
    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }


    public static RSVP create(SqlRowSet rs){
        RSVP rsvp = new RSVP();
        rsvp.setId(rs.getInt("id"));
        rsvp.setName(rs.getString("name"));
        rsvp.setEmail(rs.getString("email"));
        rsvp.setPhone(rs.getString("phone"));
        //rsvp.setConfirmation_date(new DateTime(DateTimeFormat.forPattern("yyyy-mm-dd'T'hh:mm").parseDateTime(rs.getString("confirmation_date"))));
        rsvp.setConfirmation_date(new DateTime(DateTime.parse(rs.getString("confirmation_date"))));
        rsvp.setComments(rs.getString("comments"));
        return rsvp;
    }

    public static RSVP createFromJSON(String json){
        System.out.println("went into method and json string---->"+ json);
        RSVP rsvp = new RSVP();
        JsonReader r = Json.createReader(new StringReader(json));
        JsonObject o = r.readObject();
        //System.out.println("integer id = ")
        //rsvp.setId(o.getInt("id"));
        rsvp.setName(o.getString("name"));
        rsvp.setEmail(o.getString("email"));
        rsvp.setPhone(o.getString("phone"));
        rsvp.setConfirmation_date(new DateTime(DateTime.parse(o.getString("confirmation_date"))));
        //rsvp.setConfirmation_date(new DateTime(DateTimeFormat.forPattern("yyyy-mm-dd'T'hh:mm").parseDateTime(o.getString("confirmation_date"))));
        rsvp.setComments(o.getString("comments"));
        return rsvp;
    }

    @Override
    public String toString() {
        return "RSVP [id=" + id + ", name=" + name + ", email=" + email + ", phone=" + phone + ", confirmation_date="
                + confirmation_date + ", comments=" + comments + "]";
    }

    public JsonValue toJson() {

        return Json.createObjectBuilder()
                    .add("id",getId())
                    .add("name",getName())
                    .add("email",getEmail())
                    .add("phone",getPhone())
                    .add("confirmation_date", getConfirmation_date().toString(DateTimeFormat.forPattern("dd-MM-yyyy")))
                    .add("comments", getComments())
                    .build();
    }

    
    
}
