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
        he.addProduct("Kingston RAM", 170);
        he.addProduct("Ultimate Graphics", 1059);
        System.out.println("");
        he.showProducts();
        System.out.println("");
        he.findProductById(3);
        System.out.println("");
        he.deleteProductBtId(1);
        System.out.println("");
        he.updateProductBtId(5, "Not So Ultimate", 499);
        System.out.println("");
        he.showProducts();
        he.close();
    }
}
