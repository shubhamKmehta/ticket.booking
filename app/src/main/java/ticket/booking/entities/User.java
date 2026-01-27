package ticket.booking.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private String name;
    private String password;
    private String hashedPassword;
    private List<Ticket> ticketBooked;
    private String userId;

    public User(String name,String password,String hashedPassword,List<Ticket> ticketBooked,String userId){
        this.name=name;
        this.password=password;
        this.hashedPassword=hashedPassword;
        this.ticketBooked=ticketBooked;
        this.userId=userId;
    }

    public User(){}

    public String getName(){return name;}
    public String getPassword(){return password;}
    public String getHashedPassword(){return  hashedPassword;}
    public List<Ticket> getTicketBooked(){ return ticketBooked;}
    public String getUserId(){ return userId;}

    public void printTickets(){
        for(int i=0;i<ticketBooked.size();i++){
            System.out.println(ticketBooked.get(i).getTicketInfo());
        }
    }
    public void setName(String name){this.name=name;}
    public void setPassword(String password){this.password=password;}

    public void setTicketBooked(List<Ticket> ticketBooked) {this.ticketBooked=ticketBooked;}
    public void setUserId(String userId){this.userId=userId;}
}

