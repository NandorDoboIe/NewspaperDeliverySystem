package User
public class User {
	/**
	 * Verifies that user first name at least has one character
	 * 
	 * @param firstName User's first name
	 * @return true if valid, otherwise false
	 */
	public boolean verifyId(int id) {
		return id != 0 && id > 0;
	}
	
	public boolean verifyName(String name) {
		return name != null && !name.trim().isEmpty();
	}
	
	public boolean verifyPassword(String pw) {
		return pw != null && pw.length() >= 8;
	}
	
	public boolean verifyContact(String c) {
		return c != null && c.matches("\\d{10}");
	}
	
	public boolean verifyAccessLevel(String aL) {
		if (aL == null) {
	        return false;
	    }

	    return aL.equals("newspaper delivery person")
	        || aL.equals("news agent")
	        || aL.equals("admin");
	}
	
	public boolean createUserAccount(int id,String name, String pw, String c, String aL) {
		return verifyId(id) && verifyName(name) && verifyPassword(pw) && verifyContact(c) && verifyAccessLevel(aL);
	}
	
}
