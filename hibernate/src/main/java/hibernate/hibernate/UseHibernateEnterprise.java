package hibernate.hibernate;

import java.util.logging.Level;
import java.util.logging.LogManager;

/**
 * Hello world!
 *
 */
public class UseHibernateEnterprise 
{
    public static void main( String[] args )
    {
    	//this LogManager will prevent server logs from appearing
    	LogManager.getLogManager().getLogger("").setLevel(Level.SEVERE);
    	
    	//trying HibernateEnterprise to do various stuff in the database
        HibernateEnterprise he = new HibernateEnterprise();
        //he.addProduct("Kingston RAM", 170);
        //he.addProduct("Ultimate Graphics", 1059);
        //System.out.println("");
        he.showProducts();
        System.out.println("");
        he.showProductsByName("Kingston");
        System.out.println();
        he.showProductsOrderedByPrice();
        System.out.println();
        he.showPriceByName("Memoria Bonita");
        System.out.println();
        he.findProductById(3);
        System.out.println("");
        //he.deleteProductById(1);
        //System.out.println("");
        //he.updateProductById(5, "Not So Ultimate", 499);
        System.out.println("");
        he.showProducts();
        he.close();
    	
    	//using ClienteEnterprise
    	/**
    	ClientesEnterprise ce = new ClientesEnterprise();
    	ce.showCustomers();
    	System.out.println("");
    	ce.addCustomer("Gordon", "E.E.U.U.");
    	System.out.println("");
    	ce.deleteCustomertById(6);
    	System.out.println("");
    	ce.deleteCustomerByName("Chloe");
    	System.out.println("");
    	ce.updateCustomerById(1);
    	System.out.println("");
    	ce.showCustomersByCountry("Suecia");
    	System.out.println("");
    	ce.findCountryByName("Carlos");
    	System.out.println("");
    	ce.showCustomers();
    	ce.close();*/
    }
}
