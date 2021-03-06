package tr.com.milia.resurgence.family;

import javax.persistence.*;
import java.time.Instant;

@Entity
public class FamilyBankLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, updatable = false)
	private String member;

	@ManyToOne(fetch = FetchType.LAZY)
	private Family family;

	@Column(nullable = false, updatable = false)
	private long amount;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = false, updatable = false)
	private Reason reason;

	@Column(nullable = false, updatable = false)
	private Instant date;

	public FamilyBankLog() {
	}

	public FamilyBankLog(String member, Family family, long amount, Reason reason) {
		this.member = member;
		this.family = family;
		this.amount = amount;
		this.reason = reason;
		date = Instant.now();
	}

	public Long getId() {
		return id;
	}

	public String getMember() {
		return member;
	}

	public Family getFamily() {
		return family;
	}

	public long getAmount() {
		return amount;
	}

	public Reason getReason() {
		return reason;
	}

	public Instant getDate() {
		return date;
	}
}
