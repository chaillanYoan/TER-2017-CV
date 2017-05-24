/**Simple class to stock filename and path for a template**/
public class Template {
	String filename, filepath;
	Template linkedLM;

	public Template(String filename, String filepath) {
		this.filename = filename;
		this.filepath = filepath;
		this.linkedLM = null;
	}
	
	

	public void setLinkedLM(Template lm) {
		this.linkedLM = new Template(lm.getFilename(),lm.getFilepath());
	}
	

	public void setFilename(String filename) {
		this.filename = filename;
	}



	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}



	public String getFilename() {
		return filename;
	}

	public String getFilepath() {
		return filepath;
	}
	
	public Template getLinkedLM() {
		return linkedLM;
	}
	
	public String toString(){
		return "[Template : "+getFilename()+"]";
	}
	
	
}
