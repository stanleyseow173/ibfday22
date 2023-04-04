package sg.edu.nus.IBF_paf_day22.repository;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.mysql.cj.x.protobuf.MysqlxExpect.Open.Condition.Key;

import sg.edu.nus.IBF_paf_day22.model.RSVP;

import static sg.edu.nus.IBF_paf_day22.repository.DBQueries.*;

@Repository
public class RSVPRepository {
    
    @Autowired
    JdbcTemplate jdbcTemplate;

    /*
     * Fetch all rsvp
     */

    public List<RSVP> getAllRSVP(){
        List<RSVP> rsvps = new ArrayList<RSVP>();

        SqlRowSet rs = jdbcTemplate.queryForRowSet(SELECT_ALL_RSVP);

        while (rs.next()){
            rsvps.add(RSVP.create(rs));
        }

        return rsvps;
    }


    public List<RSVP> getRSVPByName(String name){
        List<RSVP> rsvps = new ArrayList<RSVP>();
        System.out.println("Query --------> " + SELECT_RSVP_BY_NAME);
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SELECT_RSVP_BY_NAME,name);

        while (rs.next()){
            rsvps.add(RSVP.create(rs));
        }

        return rsvps;
    }

    private RSVP getRSVPByEmail(String email){
        List<RSVP> rsvps = new ArrayList<RSVP>();
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SELECT_RSVP_BY_EMAIL, email);
        
        System.out.println("went in here");
        while (rs.next()){
            rsvps.add(RSVP.create(rs));
        }

        if(rsvps.isEmpty()){
            System.out.println("went trhough empty");
            return null;
        }else{
            System.out.println("went trhough not empty");
            return rsvps.get(0);

        }
    }

    public RSVP createRsvp(RSVP rsvp){
        KeyHolder keyHolder = new GeneratedKeyHolder();

        RSVP existingRSVP = getRSVPByEmail(rsvp.getEmail());

        if(Objects.isNull(existingRSVP)){
            jdbcTemplate.update(conn -> {
                PreparedStatement statement = conn.prepareStatement(    
                INSERT_NEW_RSVP, Statement.RETURN_GENERATED_KEYS);

                statement.setString(1, rsvp.getName());
                statement.setString(2, rsvp.getEmail());
                statement.setString(3, rsvp.getPhone());
                statement.setTimestamp(4, new Timestamp(rsvp.getConfirmation_date().toDateTime().getMillis()));
                statement.setString(5, rsvp.getComments());
                return statement;
            },keyHolder);
            BigInteger primaryKey = (BigInteger)keyHolder.getKey();
            rsvp.setId(primaryKey.intValue());
        }else{
            existingRSVP.setName(rsvp.getName());
            existingRSVP.setPhone(rsvp.getPhone());
            existingRSVP.setConfirmation_date(rsvp.getConfirmation_date());
            existingRSVP.setComments(rsvp.getComments());

            boolean isUpdated = updateRSVP(existingRSVP);
            
            if(isUpdated){
                rsvp.setId(existingRSVP.getId());
            }

        }
        return rsvp;
    }

    private boolean updateRSVP(RSVP existingRSVP){
        return jdbcTemplate.update(UPDATE_RSVP_BY_EMAIL, 
                existingRSVP.getName(),
                existingRSVP.getPhone(),
                new Timestamp(existingRSVP.getConfirmation_date().getMillis()), 
                existingRSVP.getComments(),existingRSVP.getEmail())>0;
    }
}
