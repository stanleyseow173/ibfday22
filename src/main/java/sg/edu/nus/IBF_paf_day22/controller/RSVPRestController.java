package sg.edu.nus.IBF_paf_day22.controller;

import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import sg.edu.nus.IBF_paf_day22.model.RSVP;
import sg.edu.nus.IBF_paf_day22.repository.RSVPRepository;

@RestController
@RequestMapping(path="/api")
public class RSVPRestController {
    
    RSVPRepository repository;

    RSVPRestController(RSVPRepository repository){
        this.repository = repository;
    }

    @GetMapping(path="/rsvps")
    public ResponseEntity<String> getAllRsvps(){
        List<RSVP> rsvps = repository.getAllRSVP();

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        for (RSVP r : rsvps) {
            arrayBuilder.add(r.toJson());
        }

        JsonArray result = arrayBuilder.build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(result.toString());
    }

    @GetMapping(path="/rsvp")
    public ResponseEntity<String> getRSVPByName(@RequestParam String q){
        List<RSVP> rsvps = repository.getRSVPByName(q);

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        for (RSVP r : rsvps) {
            arrayBuilder.add(r.toJson());
        }

        JsonArray result = arrayBuilder.build();

        if(rsvps.isEmpty()){
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body("{'error_code':" + HttpStatus.NOT_FOUND +"}");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(result.toString());
    }


    @PostMapping(path = "/rsvp")
    public ResponseEntity<String> insertUpdateRSVP(@RequestBody String json){
        RSVP rsvp = new RSVP();

        rsvp = rsvp.create(json);

        RSVP result = repository.createRsvp(rsvp);

        JsonObject jsonObject = Json.createObjectBuilder()
                                    .add("id",result.getId())
                                    .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(jsonObject.toString());
    }
}
