package com.napier.sem;
import java.sql.*;
public class Country {

    private String country_code;

    private String country_name;

    private String region;

    private int population;

    private String continent;

    private String capital;
    public static void main(String[] args)
    {
        // Create new Application
        Country a = new Country();

        // Connect to database
        a.connect();

        // Get Country
        Country ct = a.getCountry(1);
        // Display results
        a.displayCountry(ct);


        // Disconnect from database
        a.disconnect();
    }

    /**
     * Connection to MySQL database.
     */
    private Connection con = null;



    /**
     * Connect to the MySQL database.
     */
    public void connect()
    {
        try
        {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        for (int i = 0; i < retries; ++i)
        {
            System.out.println("Connecting to database...");
            try
            {
                // Wait a bit for db to start
                Thread.sleep(30000);
                // Connect to database
                con = DriverManager.getConnection("jdbc:mysql://db:3306/world?useSSL=false", "root", "example");
                System.out.println("Successfully connected to world.sql");
                break;
            }
            catch (SQLException sqle)
            {
                System.out.println("Failed to connect to database attempt " + Integer.toString(i));
                System.out.println(sqle.getMessage());
            }
            catch (InterruptedException ie)
            {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    /**
     * Disconnect from the MySQL database.
     */
    public void disconnect()
    {
        if (con != null)
        {
            try
            {
                // Close connection
                con.close();
            }
            catch (Exception e)
            {
                System.out.println("Error closing connection to database");
            }
        }
    }

    public Country getCountry(int ct_capital)
    {
        try
        {   System.out.println("Country Information for Capital: "+ct_capital);
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT Code, Name, Continent, Region, Population, Capital "
                            + "FROM country "
                            + "WHERE Capital = " + ct_capital;
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            if (rset.next())
            {
                Country ct = new Country();
                ct.country_code = rset.getString("Code");
                ct.country_name= rset.getString("Name");
                ct.continent = rset.getString("Continent");
                ct.region= rset.getString("Region");
                ct.population= rset.getInt("Population");
                ct.capital=rset.getString("Capital");
                return ct;
            }
            else
                return null;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }

    public void displayCountry(Country ct)
    {
        if (ct != null)
        {
            System.out.println(
                    "Country code:" + ct.country_code + "\n"
                    +"Country name:" + ct.country_name + "\n "
                    +"Continent: "+ ct.continent + "\n"
                    +"Region: "+ ct.region + "\n"
                            + "Population:" + ct.population + "\n"
                    +"Capital:" + ct.capital + "\n"
                            );
        }
    }

}
