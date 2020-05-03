package tr.com.milia.resurgence.murder;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.milia.resurgence.security.TokenAuthentication;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/detective-agency")
public class AgencyController {

	private final DetectiveAgency agency;

	public AgencyController(DetectiveAgency agency) {
		this.agency = agency;
	}

	@GetMapping
	public List<AgencyStatusResponse> status(TokenAuthentication authentication) {
		String seeker = authentication.getPlayerName();
		return agency.status(seeker).stream().map(AgencyStatusResponse::new).collect(Collectors.toList());
	}

	@PostMapping("/{wanted}/{quantity}")
	public void hire(TokenAuthentication authentication,
					 @PathVariable("wanted") String wanted,
					 @PathVariable("quantity") int quantity) {
		String seeker = authentication.getPlayerName();
		agency.hireAgent(seeker, wanted, quantity);
	}

}
