package hibernate.hibernate;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class ClientesEnterprise {
	
	private static SessionFactory sf; //created once and used for every connection
	
	ClientesEnterprise() {
		//sf = HibernateUtil.getSessionFactory(); //if we did it with a HibernateUtil class
		
		sf = new Configuration().configure().buildSessionFactory();
	}
	
	//method to close the SessionFactory object
	public void close() {
		sf.close();
	}
	
	public void showCustomers() {
		
		Session session = sf.openSession(); //will use the method createQuery from Session
		Transaction transaction = null;
		
		try {
			transaction = session.beginTransaction();
			//creates a list with all the customers via SQL query
			List all_customers = session.createQuery("FROM Clientes").list();
			
			Iterator iterator = all_customers.iterator();
			System.out.println("Looking for customers...");
			System.out.println("-------------------------------------------");
			
			while(iterator.hasNext()) { //showing customers row by row
				Clientes cliente = (Clientes) iterator.next();
				System.out.println("ID: " + cliente.getId() + "\tName: " + cliente.getNombre()
						+ "\tCountry: " + cliente.getPais());
				System.out.println("-------------------------------------------");
			}
			transaction.commit();
			System.out.println("Finished searching for customers.");
			System.out.println("-------------------------------------------");
		} catch (HibernateException ex) {
			if (transaction != null)
				transaction.rollback();
			ex.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	//method that adds a customer register into the table
	public void addCustomer(String name, String country) {
		
		Session session = sf.openSession(); //will use the method save from Session
		Transaction transaction = null;
			
		//creating the customers with the parameters given
		Clientes cliente = new Clientes();
		cliente.setNombre(name);
		cliente.setPais(country);
		//producto.setId(id); //the id autoincrements
			
		try {
			System.out.println("Adding a customer to the table: "
					+ name + ", " + country + ".");
			System.out.println("-------------------------------------------");
			transaction = session.beginTransaction();
			session.persist(cliente); //inserting cliente into table Clientes
			transaction.commit();
		} catch (Exception ex) {
			if (transaction != null)
				transaction.rollback(); //if there's an exception it rollbacks to safety
			ex.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	//method that deletes a customer given an ID
	public void deleteCustomertById(int id) {
		
		Session session = sf.openSession();
		Transaction transaction = null;
		Clientes cliente = new Clientes();
		
		try {
			System.out.println("Searching for customer with ID: " + id + "...");
			System.out.println("-------------------------------------------");
			
			transaction = session.beginTransaction();
			cliente = (Clientes) session.get(Clientes.class, id);
			
			if (cliente != null) { //deletes the customer if the ID is found
				System.out.println("Deleting the following customer:");
				System.out.println("Name: " + cliente.getNombre() + "\tCountry: " + cliente.getPais());
				
				session.delete(cliente); //removes the customer
				transaction.commit();
				
				System.out.println("-------------------------------------------");
				System.out.println("Customer deleted.");
				System.out.println("-------------------------------------------");
			}
			else {
				System.out.println("Could not find a customer with that ID.");
				System.out.println("-------------------------------------------");
			}
		} catch (Exception ex) {
			if (transaction != null)
				transaction.rollback();
			ex.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	//method to update customer values given an ID
	public void updateCustomerById(int id) {
		
		Scanner scanner = new Scanner(System.in);
		Session session = sf.openSession();
		Transaction transaction = null;
		Clientes cliente = new Clientes();
		boolean name_changed = false, country_changed = false; //variables to check if anything changed
		String newName = "not updated", newCountry = "not updated";
		
		try {
			System.out.println("Searching for customer with ID: " + id + "...");
			System.out.println("-------------------------------------------");
			
			transaction = session.beginTransaction();
			cliente = (Clientes) session.load(Clientes.class, id);
			
			if (cliente != null) { //updates the customer with the given parameters if the ID is found
				System.out.println("Do you want to assign a new name to the customer? (Y/N)");
				String yesOrNo = scanner.nextLine();
				if (yesOrNo == "Y") {
					System.out.println("Type in the new name: ");
					newName = scanner.nextLine();
					cliente.setNombre(newName);
					name_changed = true;
				}
				System.out.println("Do you want to assign a new country to the customer? (Y/N)");
				yesOrNo = scanner.nextLine();
				if (yesOrNo == "Y") {
					System.out.println("Type in the new country: ");
					newCountry = scanner.nextLine();
					cliente.setPais(newCountry);
					country_changed = true;
				}
				if (name_changed || country_changed) {
					System.out.println("New customer values:");
					System.out.println("ID:" + cliente.getId() + "\tName: " + newName + "\tCountry: " + newCountry);
				}
				else
					System.out.println("No new values introduced.");
				System.out.println("-------------------------------------------");

				session.merge(cliente); //updates the values
				transaction.commit();

			}
			else {
				System.out.println("Could not find a customer with that ID.");
				System.out.println("-------------------------------------------");
			}
		} catch (Exception ex) {
			if (transaction != null)
				transaction.rollback();
			ex.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	//method that deletes a customer or customers given their name
	public void deleteCustomerByName(String name) {
		
		Session session = sf.openSession();
		Transaction transaction = null;
		Clientes cliente = new Clientes();
		
		try {
			System.out.println("Searching for customers with name: " + name + "...");
			System.out.println("-------------------------------------------");
			
			transaction = session.beginTransaction();
			//creates a list with all the customers via SQL query
			List customers_with_name = session.createQuery("FROM Clientes WHERE nombre = '" + name + "'").list();
			
			Iterator iterator = customers_with_name.iterator();
			
			while(iterator.hasNext()) { //showing customers row by row
				cliente = (Clientes) iterator.next();
				System.out.println("ID: " + cliente.getId() + "\tName: " + cliente.getNombre()
						+ "\tCountry: " + cliente.getPais());
				System.out.println("-------------------------------------------");
			}
			//transaction.commit();
			//cliente = (Clientes) session.get(Clientes.class, name);
			
			if (cliente != null) { //deletes the customer/s if the name is found
				System.out.println("Deleting aforementioned customer/s...");
				//System.out.println("Name: " + cliente.getNombre() + "\tCountry: " + cliente.getPais());
				
				session.delete(cliente); //removes the customer/s
				transaction.commit();
				
				System.out.println("-------------------------------------------");
				System.out.println("Customer/s deleted.");
				System.out.println("-------------------------------------------");
			}
			else {
				System.out.println("Could not find a customer with that name.");
				System.out.println("-------------------------------------------");
			}
		} catch (Exception ex) {
			if (transaction != null)
				transaction.rollback();
			ex.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	//method that shows all customers from a specific country
	public void showCustomersByCountry(String pais) {
		
		Session session = sf.openSession();
		Transaction transaction = null;
		
		try {
			transaction = session.beginTransaction();
			//creates a list with all the customers via SQL query
			List all_customers = session.createQuery("FROM Clientes WHERE pais = '" + pais + "'").list();
			
			Iterator iterator = all_customers.iterator();
			System.out.println("Looking for customers...");
			System.out.println("-------------------------------------------");
			
			int country_count = 0;
			
			while(iterator.hasNext()) { //showing customers row by row
				Clientes cliente = (Clientes) iterator.next();
				System.out.println("ID: " + cliente.getId() + "\tName: " + cliente.getNombre()
						+ "\tCountry: " + cliente.getPais());
				System.out.println("-------------------------------------------");
				country_count++;
			}
			transaction.commit();
			System.out.println("No. of customers from " + pais + ": " + country_count);
			System.out.println("-------------------------------------------");
			System.out.println("Finished searching for customers from " + pais);
			System.out.println("-------------------------------------------");
		} catch (HibernateException ex) {
			if (transaction != null)
				transaction.rollback();
			ex.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	public void findCountryByName(String name) {
		
		Session session = sf.openSession();
		Transaction transaction = null;
		Clientes cliente = new Clientes();
		
		try {
			System.out.println("Searching for customer with name: " + name + "...");
			System.out.println("-------------------------------------------");
			
			transaction = session.beginTransaction();
			//creates a list with all the customers via SQL query
			List customer_with_name = session.createQuery("FROM Clientes WHERE nombre = '" + name + "'").list();
			
			Iterator iterator = customer_with_name.iterator();
			
			while(iterator.hasNext()) { //showing customers row by row
				cliente = (Clientes) iterator.next();
				System.out.println(cliente.getNombre() + " is from " + cliente.getPais());
				System.out.println("-------------------------------------------");
			}
			if(cliente != null) {
				System.out.println("Finished looking for customers.");
				System.out.println("-------------------------------------------");
			}
			else {
				System.out.println("Didn't find that name.");
				System.out.println("-------------------------------------------");
			}
		} catch (Exception ex) {
			if (transaction != null)
				transaction.rollback();
			ex.printStackTrace();
		} finally {
			session.close();
		}
	}
}
