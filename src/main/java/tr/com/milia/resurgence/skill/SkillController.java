package tr.com.milia.resurgence.skill;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.milia.resurgence.security.TokenAuthentication;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/skill")
public class SkillController {

	private final PlayerSkillRepository repository;
	private final MessageSource messageSource;

	public SkillController(
		PlayerSkillRepository repository,
		@Qualifier("defaultMessageSource") MessageSource messageSource
	) {
		this.repository = repository;
		this.messageSource = messageSource;
	}

	@GetMapping
	public List<SkillResponse> skills(TokenAuthentication tokenAuthentication, Locale locale) {
		var playerSkills = repository
			.findAllById_Player_Name(tokenAuthentication.getPlayerName()).stream()
			.collect(Collectors.toMap(PlayerSkill::getSkill, PlayerSkill::getExpertise));

		return Arrays.stream(Skill.values())
			.map(skill -> new SkillResponse(
				skill,
				messageSource.getMessage("skill.description." + skill.name(), null, locale),
				playerSkills.getOrDefault(skill, BigDecimal.ZERO).doubleValue()
			))
			.sorted(Comparator.comparing(SkillResponse::name))
			.collect(Collectors.toList());
	}

}
