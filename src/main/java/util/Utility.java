package util;

import javafx.scene.control.Alert;
import model.*;
import model.tda.*;
import model.tda.graph.Vertex;

import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.List;

public class Utility {

    //static init
    static {
    }

    public static String format(double value){
        return new DecimalFormat("###,###,###.##").format(value);
    }
    public static String $format(double value){
        return new DecimalFormat("$###,###,###.##").format(value);
    }

    public static void fill(int[] a, int bound) {
        for (int i = 0; i < a.length; i++) {
            a[i] = new Random().nextInt(bound);
        }
    }

    public static int random(int bound) {
        return new Random().nextInt(bound);
    }
    public static int random(int bound1, int bound2) {
        int min = Math.min(bound1, bound2);
        int max = Math.max(bound1, bound2);
        return new Random().nextInt(max - min + 1) + min;
    }

    public static int compare(Object a, Object b) {
        switch (instanceOf(a, b)){
            case "Integer":
                Integer int1 = (Integer)a; Integer int2 = (Integer)b;
                return int1 < int2 ? -1 : int1 > int2 ? 1 : 0; //0 == equal
            case "String":
                String st1 = (String)a; String st2 = (String)b;
                return st1.compareTo(st2)<0 ? -1 : st1.compareTo(st2) > 0 ? 1 : 0;
            case "Character":
                Character c1 = (Character)a; Character c2 = (Character)b;
                return c1.compareTo(c2)<0 ? -1 : c1.compareTo(c2)>0 ? 1 : 0;
            case "Flight":
                Flight fl1 = (Flight)a; Flight fl2 = (Flight)b;
                return compare(fl1.getFlightID(), fl2.getFlightID());
            case "Passenger":
                if (a instanceof Passenger&&b instanceof Passenger){
                Passenger p1 = (Passenger)a; Passenger p2 = (Passenger)b;
                return compare(p1.getId(), p2.getId());}
                if (a instanceof Passenger){
                    Passenger p1 = (Passenger)a;
                    return compare(p1.getId(),b);
                }
                if (b instanceof Passenger){
                    Passenger p1 = (Passenger)b;
                    return compare(p1.getId(),a);
                }
                break;
            case "Airport":
                if (a instanceof Airport&& b instanceof Airport) {
                Airport a1 = (Airport)a; Airport a2 = (Airport)b;
                return compare(a1.getCode(), a2.getCode());}
                if (a instanceof Airport) {
                    Airport a3 = (Airport)a;
                    return compare(a3.getCode(), b);
                }
                if (b instanceof Airport) {
                    Airport a4 = (Airport)b;
                    return compare(a4.getCode(), a);
                }
                break;
            case "Vertex":
                if (a instanceof Vertex&& b instanceof Vertex) {
                    Vertex v1 = (Vertex)a; Vertex v2 = (Vertex)b;
                    return compare(v1.data,v2.data);
                }
                if (a instanceof Vertex) {
                    Vertex v1 = (Vertex)a;
                    return compare(v1.data,b);
                }
                if (b instanceof Vertex) {
                    Vertex v1 = (Vertex)b;
                    return compare(v1.data,a);
                }
                break;
                case "Route":
                    Route r1 = (Route)a; Route r2 = (Route)b;
                    return compare(r1.getRoute_id(), r2.getRoute_id());
            case "User":
                User u1 = (User)a; User u2 = (User)b;
                return compare(u1.getId(), u2.getId());
        }
        return 2; //Unknown
    }

    private static String instanceOf(Object a, Object b) {
        if(a instanceof Integer && b instanceof Integer) return "Integer";
        if(a instanceof String && b instanceof String) return "String";
        if(a instanceof Character && b instanceof Character) return "Character";
        if (a instanceof Flight && b instanceof Flight) return "Flight";
        if (a instanceof Passenger && b instanceof Passenger) return "Passenger";
        if (a instanceof Airport || b instanceof Airport) return "Airport";
        if (a instanceof Vertex || b instanceof Vertex ) return "Vertex";
        if (a instanceof Route && b instanceof Route)return "Route";
        if (a instanceof User&& b instanceof User) return "User";
        return "Unknown";
    }

    public static int maxArray(int[] a) {
        int max = a[0]; //first element
        for (int i = 1; i < a.length; i++) {
            if(a[i]>max){
                max=a[i];
            }
        }
        return max;
    }

    public static int[] getIntegerArray(int n) {
        int[] newArray = new int[n];
        for (int i = 0; i < n; i++) {
            newArray[i] = random(9999);
        }
        return newArray;
    }


    public static int[] copyArray(int[] a) {
        int n = a.length;
        int[] newArray = new int[n];
        for (int i = 0; i < n; i++) {
            newArray[i] = a[i];
        }
        return newArray;
    }

    public static String show(int[] a, int n) {
        String result="";
        for (int i = 0; i < n; i++) {
            result+=a[i]+" ";
        }
        return result;
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes)
                sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error al hashear la contraseña");
        }
    }

    public static void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Error de validación");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    public static int contarPaisesVisitados(SinglyLinkedList sLL) throws ListException, StackException {
        int count;
        LinkedStack stack = new LinkedStack();
        //lleno un stack auxiliar con los paises visitados;
            for (int i=1;i<=sLL.size();i++){
            Flight flight= (Flight) sLL.getNode(i).data;
            String country = flight.getOrigin().getCountry();
            stack.push(country);
        }
        LinkedQueue queue = new LinkedQueue();//cola aux
            while (!stack.isEmpty()){//Remuevo los elemento de la pila y solo encolo paises unicos
                String country = (String) stack.pop();
                if (queue.isEmpty())
                    queue.enQueue(country);
                else if (!queue.contains(country)){
                    queue.enQueue(country);
                }
            }
            count = queue.size();
        return count;
    }
    public static int contarPaisesVisitados(List<Flight> sLL) throws ListException, StackException {
        int count;
        LinkedStack stack = new LinkedStack();
        //lleno un stack auxiliar con los paises visitados;
        for (int i=0;i<sLL.size();i++){
            Flight flight=  sLL.get(i);
            String country = flight.getOrigin().getCountry();
            stack.push(country);
        }
        LinkedQueue queue = new LinkedQueue();//cola aux
        while (!stack.isEmpty()){//Remuevo los elemento de la pila y solo encolo paises unicos
            String country = (String) stack.pop();
            if (queue.isEmpty())
                queue.enQueue(country);
            else if (!queue.contains(country)){
                queue.enQueue(country);
            }
        }
        count = queue.size();
        return count;
    }
    public static int getRandomPriority(){
        return random(3);
    }
    public static String getRandomSeat(Integer priority,Flight flight) {
        Set<Integer> seats=new HashSet<>();
        for (int i=1;i<=flight.getCapacity();i++){
            seats.add(i);
        }
        Set<Integer> ocuppiedSeats=new HashSet<>();
        switch (priority){
            case 3:
                int number=random(seats.size());
                return number +"A";
            case 2:
                number=random(seats.size());
                return number +"B";
            case 1:
                number=random(seats.size());
                return number +"C";
                default:
                    return random(seats.size())+"";
        }
    }
    public static List<LocalTime> getDepartureHours(){
        List<LocalTime> list=new ArrayList();
        list=List.of(
                LocalTime.of(5, 0),
                LocalTime.of(6, 30),
                LocalTime.of(8, 0),
                LocalTime.of(9, 30),
                LocalTime.of(11, 0),
                LocalTime.of(13, 0),
                LocalTime.of(15, 30),
                LocalTime.of(17, 0),
                LocalTime.of(19, 30),
                LocalTime.of(21, 0),
                LocalTime.of(23, 0));
        return list;
    }

    public static String formatedDate(LocalDateTime departureTime) {
        String r="";
        String minute= String.valueOf(departureTime.getMinute()>9?departureTime.getMinute():"0"+departureTime.getMinute());
        r+=departureTime.getDayOfMonth()+"-"+departureTime.getMonthValue()
                +"-"+departureTime.getYear()+": "
                +departureTime.getHour()+":"+minute;
        return r;
    }
}
