/**Simple class to stock filename and path for a template**/
public class Template {
	String filename, filepath;

	public Template(String filename, String filepath) {
		this.filename = filename;
		this.filepath = filepath;
	}

	public String getFilename() {
		return filename;
	}

	public String getFilepath() {
		return filepath;
	}
	
	public String toString(){
		return "[Template : "+getFilename()+"]";
	}
}
