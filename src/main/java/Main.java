import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;
        Customer customer1 = new Customer("Marcin", "Nowak", 45);
        Customer customer2 = new Customer("Stefan", "Raś", 32);
        Customer customer3 = new Customer("Brajan", "Piotrowski", 16);

        try {
            connection = MySqlConnection.getConnection();
            statement = connection.createStatement();

            statement.execute("DROP TABLE customers");
            statement.execute("CREATE TABLE IF NOT EXISTS customers (" +
                    "ID INT AUTO_INCREMENT PRIMARY KEY NOT NULL, " +
                    "NAME VARCHAR(50) NOT NULL, " +
                    "SURNAME VARCHAR(50) NOT NULL, " +
                    "AGE INT NOT NULL);"
            );
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO customers (NAME, SURNAME, AGE) " + "VALUES ('Jan', 'Kowalski', 23)");
            ps.executeUpdate();

            insertUser();
            insertUser(customer1);
            insertUsers(Arrays.asList(customer2, customer3));

            getCustomer(2);
            getAllCustomers();
            System.out.println("*******");

            deleteUser(1);
            getAllCustomers();
            System.out.println("*******");

            deleteUserByAge(27);
            getAllCustomers();
            System.out.println("*******");

            updateUser(customer3,4); //pobiera dane z customer3 i zamienia dla id=4
            getAllCustomers();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    static boolean insertUser() {
        Connection connection = null;
        try {
            connection = MySqlConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO customers (NAME, SURNAME, AGE) VALUES ('Mateusz', 'Pawelec', 27)");
            int i = ps.executeUpdate();
            //odpowiada, ile rekordow zostalo zapisanych
            if (i == 1) {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    static boolean insertUser(Customer customer) {
        Connection connection = null;
        try {
            connection = MySqlConnection.getConnection();
            //? oznacza, ze jesli ponizej zostanie wstawiona jakas wartosc to 1? -> getName, 2? ->getSurname itd
            PreparedStatement ps = connection.prepareStatement("INSERT INTO customers VALUES (NULL, ?, ?, ?)");
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getSurname());
            ps.setInt(3, customer.getAge());
            int i = ps.executeUpdate();
            if (i == 1) {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    static void insertUsers(List<Customer> customers) {
        customers.forEach(customer -> {
            Connection connection = null;

            try {
                connection = MySqlConnection.getConnection();

                PreparedStatement ps = connection.prepareStatement("INSERT INTO customers VALUES (NULL, ?, ?, ?)");
                ps.setString(1, customer.getName());
                ps.setString(2, customer.getSurname());
                ps.setInt(3, customer.getAge());
                ps.executeUpdate();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }

    static Customer getCustomer(int id) {
        Connection connection = null;
        try {
            connection = MySqlConnection.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM customers WHERE id=" + id);
            if (rs.next()) {
                Customer customer = new Customer();
                customer.setName(rs.getString("name"));
                customer.setSurname(rs.getString("surname"));
                customer.setAge(rs.getInt(4));
                System.out.println(customer);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    static List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        Connection connection = null;
        try {
            connection = MySqlConnection.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM customers");
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setName(rs.getString("name"));
                customer.setSurname(rs.getString("surname"));
                customer.setAge(rs.getInt(4)); //dalem index kolumny
                customers.add(customer);
            }
            System.out.println(customers);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return customers;
    }

    static boolean deleteUser(int id) {
        Connection connection = null;
        try {
            connection = MySqlConnection.getConnection();
            Statement stmt = connection.createStatement();
            int i = stmt.executeUpdate("DELETE FROM customers WHERE id=" + id);
            if (i == 1) {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    static boolean deleteUserByAge(int age) {
        Connection connection = null;
        try {
            connection = MySqlConnection.getConnection();
            Statement stmt = connection.createStatement();
            int i = stmt.executeUpdate("DELETE FROM customers WHERE age=" + age);
            if (i == 1) {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    static boolean updateUser(Customer customer, int id) {
        Connection connection = null;
        try {
//            Customer customer = new Customer();
            connection = MySqlConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement("UPDATE customers SET name=?, surname=?, age=? WHERE id=?");
            ps.setString(1, customer.getName());
            ps.setString(2, "Y");
            ps.setInt(3, 99);
            ps.setInt(4, id);
            int i = ps.executeUpdate();
            if (i == 1) {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}


