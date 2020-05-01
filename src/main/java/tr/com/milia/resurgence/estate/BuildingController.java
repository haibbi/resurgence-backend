package tr.com.milia.resurgence.estate;

import org.springframework.web.bind.annotation.*;
import tr.com.milia.resurgence.security.TokenAuthentication;
import tr.com.milia.resurgence.task.PlayerNotFound;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/real-estate")
public class BuildingController {

	private final BuildingService service;

	public BuildingController(BuildingService service) {
		this.service = service;
	}

	@GetMapping
	public List<BuildingOwnerResponse> owners() {
		List<Deed> owners = service.owners();
		Set<Building> ownedBuilding = owners.stream().map(Deed::getBuilding).collect(Collectors.toSet());
		Set<Building> notOwned = Arrays.stream(Building.values())
			.filter(building -> !ownedBuilding.contains(building))
			.collect(Collectors.toSet());

		List<BuildingOwnerResponse> result = owners.stream()
			.map(BuildingOwnerResponse::new)
			.collect(Collectors.toCollection(LinkedList::new));
		notOwned.forEach(building -> result.add(new BuildingOwnerResponse(building)));

		return result;
	}

	@PostMapping("/{building}")
	public void buy(TokenAuthentication authentication, @PathVariable("building") Building building) {
		String player = authentication.getPlayerName().orElseThrow(PlayerNotFound::new);
		service.buy(player, building);
	}

	@DeleteMapping("/{building}")
	public void sell(TokenAuthentication authentication, @PathVariable("building") Building building) {
		String player = authentication.getPlayerName().orElseThrow(PlayerNotFound::new);
		service.sell(player, building);
	}

}
