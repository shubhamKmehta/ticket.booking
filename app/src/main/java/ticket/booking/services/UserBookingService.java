package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;


public class UserBookingService {
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<User> userList;
    private User user;


    private final static String USERS_PATH ="app/src/main/java/ticket/booking/localDb/users.json";

    public UserBookingService() throws IOException {
        this.userList=loadUsers();
    }
    public UserBookingService(User user) throws IOException {
        this.user=user;
        this.userList=loadUsers();
    }

    public List<User> loadUsers() throws IOException {
        File users = new File(USERS_PATH);
        if(!users.exists()){
            return new ArrayList<>();
        }
        return objectMapper.readValue(users,new TypeReference<List<User>>(){});
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
            return Boolean.FALSE;
        }
    }

    // yaha ho rha h serialization...user.json me users add ho rhe hain 0 or 1 ke byte form me
    private void saveUserListToFile() throws IOException {
        File usersFile=new File(USERS_PATH);
        objectMapper.writeValue(usersFile,userList);
    }

    public void fetchBooking(){
        Optional<User> userFetched = userList.stream().filter(user1 -> {
            return user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword());
        }).findFirst();
        if(userFetched.isPresent()){
            userFetched.get().printTickets();
        }
    }


    public List<List<Integer>> fetchSeats(Train train){
        return train.getSeats();
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

    public List<Train> getTrains(String source, String destination) {
        try{
            TrainService trainService = new TrainService();
            return trainService.searchTrains(source.toLowerCase(), destination.toLowerCase());
        }catch (IOException e){
            return new ArrayList<>();
        }

    }



    public boolean bookTrainSeat(Train train,int row,int seat){
        try
        {
            TrainService trainService = new TrainService();
            List<List<Integer>> seats = train.getSeats();
            if(row >= 0 && row < seats.size() && seat >= 0 && seat < seats.get(row).size()){
                if(seats.get(row).get(seat) == 0){
                    seats.get(row).set(seat,1);
                    train.setSeats(seats);
                    trainService.addTrain(train);
                    return  true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }catch (IOException e){
            return Boolean.FALSE;
        }
    }
}
