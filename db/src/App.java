import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class App {

    private final String url = "jdbc:postgresql://localhost/pss";
    private final String user = "postgres";
    private final String password = "1969";
    private final static FileSystem fs = new FileSystem();

    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void insertUsers(List<User> list) {
        String SQL = "INSERT INTO public.\"user\"(first_name, last_name, email, age, country, city, street, genre, song) "
                + "VALUES(?,?,?,?,?,?,?,?,?)";
        try (
            Connection conn = connect();
            PreparedStatement statement = conn.prepareStatement(SQL);) {
            int count = 0;

            for (User user : list) {
                statement.setString(1, user.getFirstName());
                statement.setString(2, user.getLastName());
                statement.setString(3, user.getEmail());
                statement.setInt(4, user.getAge());
                statement.setString(5, user.getCountry());
                statement.setString(6, user.getCity());
                statement.setString(7, user.getStreet());
                statement.setString(8, user.getGenre());
                statement.setString(9, user.getSong());

                statement.addBatch();
                count++;
                if (count % 100 == 0 || count == list.size()) {
                    statement.executeBatch();
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void getUsers() {
        String SQL = "SELECT * FROM public.\"user\"";

        try (Connection conn = connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL)) {
            displayUsers(rs);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void displayUsers(ResultSet rs) throws SQLException {
        while (rs.next()) {
            String result = rs.getString("user_id") + " "
            + rs.getString("first_name") + " "
            + rs.getString("last_name") + " "
            + rs.getString("email") + " "
            + rs.getString("age") + " "
            + rs.getString("country") + " "
            + rs.getString("city") + " "
            + rs.getString("street") + " "
            + rs.getString("genre") + " "
            + rs.getString("song") + " "
            + rs.getString("created_on");
            fs.write("data", "modifiedData", result);
        }
    }

    public int updateAge() {
        String SQL = "UPDATE public.\"user\" " + "SET age = (age * age)/(age * age) ";
        int affectedrows = 0;

        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            affectedrows = pstmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return affectedrows;
    }

    public int deleteUsers() {
        String SQL = "DELETE FROM public.\"user\"";
        int affectedrows = 0;

        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            affectedrows = pstmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return affectedrows;
    }

    public ArrayList<User> convertDataIntoUser(String[][] data) {
        ArrayList<User> userList = new ArrayList<>();
        for(int i = 0; i < data.length; i++) {
            String firstName = data[i][0].trim();
            String lastName = data[i][1].trim();
            String email = data[i][2].trim();
            int age = Integer.parseInt(data[i][3].trim());
            String country = data[i][4].trim();
            String city = data[i][5].trim();
            String street = data[i][6].trim();
            String genre = data[i][7].trim();
            String song = data[i][8].trim();
            userList.add(new User(firstName, lastName, email, age, country, city, street, genre, song));
        }
        return userList;
    }

    public static void main(String[] args) {
        long beginning = System.nanoTime();
        App app = new App();

        String [][] data = fs.read(10000, 9);

        //insert
        ArrayList<User> userList = app.convertDataIntoUser(data);
        app.insertUsers(userList);

        //update
        app.updateAge();

        //select
        app.getUsers();

        //delete
        app.deleteUsers();

        long end = System.nanoTime();
        String duration = String.valueOf((end - beginning) / 1000000);
        fs.write("time", "on", duration);
    }
}
