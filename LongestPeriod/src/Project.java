import java.util.ArrayList;
import java.util.List;

public class Project { //class for Projects
	
	private int projectID;
	private List<Triplet<Employee,String,String>>projectInfo = new ArrayList<>();
	
	public int getPrId() {return projectID;}
	public void setPrId(int prID) {projectID = prID;}
	
	public List<Triplet<Employee,String,String>> getInfo() { return projectInfo;}
	
	public Project(int ID) { this.projectID = ID;}
	public Project() { }
	
	public void addElement(Triplet<Employee, String, String> a) { projectInfo.add(a);}

}
