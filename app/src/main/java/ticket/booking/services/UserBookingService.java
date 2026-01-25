package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.User;
import ticket.booking.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;


public class UserBookingService {
    private User user;
    private List<User> userList;
    private ObjectMapper objectMapper = new ObjectMapper();
    private static final String USERS_PATH ="app/src/main/java/ticket/booking/localDb/users.json";

    public UserBookingService(User user1) throws IOException {
        this.user=user1;
        File users = new File(USERS_PATH);
        userList = objectMapper.readValue(users, new TypeReference<List<User>>(){});
    }

    public Boolean loginUser(){
        Optional<User> foundUser = userList.stream().filter(user1 -> {
            return user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(),user1.getHashedPassword());
        }).findFirst();
        return foundUser.isPresent();
    }

    public Boolean signUp(User user1){
        try{
            userList.add(user1);
            saveUserListToFile();
            return Boolean.TRUE;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // yaha ho rha h serialization...user.json me users add ho rhe hain 0 or 1 ke byte form me
    private void saveUserListToFile() throws IOException {
        File usersFile=new File(USERS_PATH);
        objectMapper.writeValue(usersFile,userList);
    }

    public void fetchBooking(){
        user.printTickets();
    }

    public Boolean cancelBooking(String ticketId){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter ticket id to cancel");
        ticketId = scanner.next();

        if(ticketId == null || ticketId.isEmpty()){
            System.out.println("Ticket id cannot be null or empty");
        }

        String finalTicketId = ticketId;

        boolean removed = user.getTicketBooked().removeIf(ticket -> ticket.getTicketId().equals(finalTicketId));

        if(removed){
            System.out.println("Ticket with id "+ ticketId + "has been canceled");
            return Boolean.TRUE;
        }else {
            System.out.println("No ticket found with ticket id "+ ticketId);
            return Boolean.FALSE;
        }

    }
}
