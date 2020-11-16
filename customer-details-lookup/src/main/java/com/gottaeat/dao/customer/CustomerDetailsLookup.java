package com.gottaeat.dao.customer;

import com.gottaeat.domain.order.FoodOrder;
import com.gottaeat.domain.geography.Address;
import com.gottaeat.domain.fraud.fraudlabs.PaymentType;
import com.gottaeat.domain.fraud.fraudlabs.Transaction;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;
import org.apache.pulsar.shade.org.apache.commons.lang.StringUtils;

public class CustomerDetailsLookup implements Function<FoodOrder, Transaction> {
	
	public static final String DB_DRIVER_KEY = "dbDriverClass";
	public static final String DB_PASS_KEY = "dbPass";
	public static final String DB_URL_KEY = "dbUrl";
	public static final String DB_USER_KEY = "dbUser";
			
	private Connection con;
	private PreparedStatement stmt;
	
	private String dbUrl;
	private String dbUser;
	private String dbPass;
	private String dbDriverClass;

	@Override
	public Transaction process(FoodOrder order, Context ctx) throws Exception {
		
		if (!isInitalized()) {
		  dbUrl = (String) ctx.getUserConfigValue("dbUrl").orElse(null);
		  dbUser = (String) ctx.getUserConfigValue("dbUser").orElse(null);
		  dbPass = (String) ctx.getUserConfigValue("dbPass").orElse(null);
		  dbPass = (String) ctx.getUserConfigValue("dbDriverClass").orElse(null);
		}
		
		Transaction tx = null;
		ResultSet rs = getSql(order.getMeta().getCustomerId()).executeQuery();
		if (rs != null && rs.first()) {
			Address addr = Address.newBuilder()
					.setStreet(rs.getString("a.address"))
					.setCity(rs.getString("c2.city"))
					.setCountry(rs.getString("c3.country"))
					.setZip(rs.getString("a.postal_code"))
					.setState(rs.getString("a.district"))
					.build();
			
			tx = Transaction.newBuilder()
					.setAmount(order.getPayment().getAmount().getTotal())
					.setBillingAddress(addr)
					.setEmail(rs.getString("ru.email"))
					.setFirstName(rs.getString("ru.first_name"))
					.setLastName(rs.getString("ru.last_name"))
					.setPaymentMode(PaymentType.CREDITCARD)
					.setPhoneNumber(rs.getString("a.phone"))
					.setShipToFirstName(rs.getString("ru.first_name"))
					.setShipToLastName(rs.getString("ru.last_name"))
					.setShippingAddress(addr)  // TODO Use the delivery address from the order
					.build();
		}
		return tx;
	}
	
	private PreparedStatement getSql(long customerId) throws SQLException, ClassNotFoundException {
		if (stmt == null) {
		  stmt = getDbConnection().prepareStatement("select ru.first_name, ru.last_name, ru.email, "
					+ "a.address, a.postal_code, a.phone, a.district,"
					+ "c2.city, c3.country from GottaEat.Customer c "
					+ "join GottaEat.RegisteredUser ru on c.user_id = ru.user_id "
					+ "join GottaEat.Address a on a.address_id = c.address_id "
					+ "join GottaEat.City c2 on a.city_id = c2.city_id "
					+ "join GottaEat.Country c3 on c2.country_id = c3.country_id "
					+ "where c.customer_id = ?");
		}
		return stmt;
	}
	
	private Connection getDbConnection() throws SQLException, ClassNotFoundException {
	  if (con == null) {
		  Class.forName(dbDriverClass);
		  con = DriverManager.getConnection(dbUrl, dbUser, dbPass);
	  }
	  return con;
	}
	
	private boolean isInitalized() {
		return StringUtils.isNotBlank(dbUrl) && StringUtils.isNotBlank(dbUser) 
				&& StringUtils.isNotBlank(dbPass) && StringUtils.isNotBlank(dbDriverClass);
	}

}
