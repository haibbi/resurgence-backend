package tr.com.milia.resurgence.player;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

class CreatePlayerRequest {
	@NotNull
	@Size(min = 2, max = 20)
	@Pattern(regexp = "[a-zA-Z0-9]+")
	String name;

	@NotNull
	Race race;

	public void setName(String name) {
		this.name = name;
	}

	public void setRace(Race race) {
		this.race = race;
	}
}
