package DelArea;


public class DelArea {
	public boolean verifyId(int id) {
		return id != 0 && id > 0;
	}
	
	public boolean verifyLocation(String location) {
		return location != null && !location.trim().isEmpty();
	}
	
	public boolean createUserAccount(int id,String location) {
		return verifyId(id) && verifyLocation(location);
	}
}

