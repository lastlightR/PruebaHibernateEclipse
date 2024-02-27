package hibernate.hibernate;

import java.util.Iterator;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

public class HibernateEnterprise {
	
	private static SessionFactory sf; //created once and used for every connection
	
	HibernateEnterprise() {
		//sf = HibernateUtil.getSessionFactory(); //if we did it with a HibernateUtil class
		
		sf = new Configuration().configure().buildSessionFactory();
	}
	
	//method to close the SessionFactory object
	public void close() {
		sf.close();
	}
	
	//method that adds a product register into the table
	public void addProduct(String name, double price) {
		Session session = sf.openSession(); //will use the method save from Session
		Transaction transaction = null;
		
		//creating the products with the parameters given
		Productos producto = new Productos();
		producto.setNombre(name);
		producto.setPrecio(price);
		//producto.setId(id); //the id autoincrements
		
		try {
			System.out.println("Inserting a product in the table: "
					+ name + ", " + price + ".");
			System.out.println("-------------------------------------------");
			transaction = session.beginTransaction();
			session.persist(producto); //inserting producto into table Productos
			transaction.commit();
		} catch (Exception ex) {
			if (transaction != null)
				transaction.rollback(); //if there's an exception it rollbacks to safety
			ex.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	public void showProducts() {
		Session session = sf.openSession();
		Transaction transaction = null;
		
		try {
			transaction = session.beginTransaction();
			//creates a list with all the products via SQL query
			List all_products = session.createQuery("FROM Productos").list();
			
			Iterator iterator = all_products.iterator();
			System.out.println("Looking for products...");
			System.out.println("-------------------------------------------");
			
			while(iterator.hasNext()) { //showing products row by row
				Productos producto = (Productos) iterator.next();
				System.out.println("ID: " + producto.getId() + "\tName: " + producto.getNombre()
						+ "\tPrice: " + producto.getPrecio());
				System.out.println("-------------------------------------------");
			}
			transaction.commit();
			System.out.println("Finished searching for products.");
			System.out.println("-------------------------------------------");
		} catch (HibernateException ex) {
			if (transaction != null)
				transaction.rollback();
			ex.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	public Productos findProductById(int id) {
		Session session = sf.openSession();
		Transaction transaction = null;
		Productos producto = new Productos();
		
		try {
			System.out.println("Finding product with ID: " + id + "...");
			System.out.println("-------------------------------------------");
			
			transaction = session.beginTransaction();
			
			//different way to establish a query
			Query<Productos> query = session.createQuery("FROM Productos WHERE id = " + id);
			producto = query.uniqueResult();
			
			transaction.commit();
			
			//showing product's info with the ID given
			if (producto != null) {
				System.out.println("ID: " + id + "\tName: " + producto.getNombre() + "\tPrice: " + producto.getPrecio());
				System.out.println("-------------------------------------------");
			}
			else {
				System.out.println("Product not found.");
				System.out.println("-------------------------------------------");
			}
		} catch (ObjectNotFoundException ex) {
			if (transaction != null) {
				System.out.println("Product not found. Error: " + ex);
				ex.printStackTrace();
			}
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
				System.out.println("Error: " + ex);
				ex.printStackTrace();
			}
		} finally {
			session.close();
		}
		
		return producto;
	}
	
	public void deleteProductById(int id) {
		Session session = sf.openSession();
		Transaction transaction = null;
		Productos producto = new Productos();
		
		try {
			System.out.println("Searching for product with ID: " + id + "...");
			System.out.println("-------------------------------------------");
			
			transaction = session.beginTransaction();
			producto = (Productos) session.get(Productos.class, id);
			
			if (producto != null) { //deletes the product if the ID is found
				System.out.println("Deleting the following product:");
				System.out.println("Name: " + producto.getNombre() + "\tPrice: " +producto.getPrecio());
				
				session.delete(producto); //deletes the product
				transaction.commit();
				
				System.out.println("-------------------------------------------");
				System.out.println("Product deleted.");
				System.out.println("-------------------------------------------");
			}
			else {
				System.out.println("Could not find a product with that ID.");
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
	
	public void updateProductById(int id, String newName, double newPrice) {
		Session session = sf.openSession();
		Transaction transaction = null;
		Productos producto = new Productos();
		
		try {
			System.out.println("Searching for product with ID: " + id + "...");
			System.out.println("-------------------------------------------");
			
			transaction = session.beginTransaction();
			producto = (Productos) session.load(Productos.class, id);
			
			if (producto != null) { //updates the product with the given parameters if the ID is found
				System.out.println("Updating the following product:");
				System.out.println("ID:" + producto.getId() + "\tName: " + producto.getNombre() + "\tPrice: " +producto.getPrecio());
				System.out.println("...with the following values:");
				System.out.println("Name: " + newName + "\tPrice: " + newPrice);
				System.out.println("-------------------------------------------");
				
				producto.setNombre(newName); //setting the new values for producto
				producto.setPrecio(newPrice);
				session.merge(producto); //updates the values
				transaction.commit();
				
				System.out.println("Product updated.");
				System.out.println("-------------------------------------------");
			}
			else {
				System.out.println("Could not find a product with that ID.");
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
	
	//method that shows all products with a text in the name
		public void showProductsByName(String text) {
			
			Session session = sf.openSession();
			Transaction transaction = null;
			boolean products_found = false;
			
			try {
				transaction = session.beginTransaction();
				//creates a list with all the products via SQL query
				List products = session.createQuery("FROM Productos WHERE nombre LIKE '%" + text + "%'").list();
				
				Iterator iterator = products.iterator();
				System.out.println("Looking for products...");
				System.out.println("-------------------------------------------");
				
				while(iterator.hasNext()) { //showing products row by row
					Productos producto = (Productos) iterator.next();
					System.out.println("ID: " + producto.getId() + "\tName: " + producto.getNombre()
							+ "\tPrice: " + producto.getPrecio());
					System.out.println("-------------------------------------------");
					products_found = true;
				}
				transaction.commit();
				if (products_found) {
					System.out.println("Finished searching for products with " + text + " in the name.");
					System.out.println("-------------------------------------------");
				}
				else {
					System.out.println("Found no products with " + text + " in the name.");
					System.out.println("-------------------------------------------");
				}
			} catch (HibernateException ex) {
				if (transaction != null)
					transaction.rollback();
				ex.printStackTrace();
			} finally {
				session.close();
			}
		}
	
		public void showProductsOrderedByPrice() {
			Session session = sf.openSession();
			Transaction transaction = null;
			
			try {
				transaction = session.beginTransaction();
				//creates a list with all the products via HQL query
				List all_products = session.createQuery("FROM Productos ORDER BY precio ASC").list();
				
				Iterator iterator = all_products.iterator();
				System.out.println("Showing products ordered by price...");
				System.out.println("-------------------------------------------");
				
				while(iterator.hasNext()) { //showing products row by row
					Productos producto = (Productos) iterator.next();
					System.out.println("ID: " + producto.getId() + "\tName: " + producto.getNombre()
							+ "\tPrice: " + producto.getPrecio());
					System.out.println("-------------------------------------------");
				}
				transaction.commit();
				System.out.println("Finished searching for products.");
				System.out.println("-------------------------------------------");
			} catch (HibernateException ex) {
				if (transaction != null)
					transaction.rollback();
				ex.printStackTrace();
			} finally {
				session.close();
			}
		}
	
		public void showPriceByName(String name) {
			Session session = sf.openSession();
			Transaction transaction = null;
			boolean products_found = false;
			
			try {
				transaction = session.beginTransaction();
				//creates a list with all the products via HQL query
				List all_products = session.createQuery("FROM Productos WHERE nombre = '" + name + "'").list();
				
				Iterator iterator = all_products.iterator();
				System.out.println("Showing price of products with name: " + name + "...");
				System.out.println("-------------------------------------------");
				
				while(iterator.hasNext()) { //showing products row by row
					Productos producto = (Productos) iterator.next();
					System.out.println("ID: " + producto.getId()
							+ "\tPrice: " + producto.getPrecio());
					System.out.println("-------------------------------------------");
					products_found = true;
				}
				transaction.commit();
				if (products_found) {
					System.out.println("Finished searching for products.");
					System.out.println("-------------------------------------------");
				}
				else {
					System.out.println("Found no products.");
					System.out.println("-------------------------------------------");
				}
			} catch (HibernateException ex) {
				if (transaction != null)
					transaction.rollback();
				ex.printStackTrace();
			} finally {
				session.close();
			}
		}

}
