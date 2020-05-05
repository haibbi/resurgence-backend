package tr.com.milia.resurgence.family;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

class AnnouncementRequest {

	@NotEmpty
	@Size(min = 1, max = 50)
	String title;
	@Size(max = 200)
	String content;
	@NotNull
	Boolean general;

	public void setTitle(String title) {
		this.title = title.trim();
	}

	public void setContent(String content) {
		this.content = content.trim();
	}

	public void setGeneral(Boolean general) {
		this.general = general;
	}
}
