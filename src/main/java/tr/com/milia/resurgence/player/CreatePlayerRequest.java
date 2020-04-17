package tr.com.milia.resurgence.player;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

class CreatePlayerRequest {
	@NotNull
	@Size(min = 3, max = 20)
	String name;

	public void setName(String name) {
		this.name = name;
	}
}
