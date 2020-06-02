package tr.com.milia.resurgence.bank;

import org.springframework.data.domain.AbstractAggregateRoot;
import tr.com.milia.resurgence.player.NotEnoughMoneyException;
import tr.com.milia.resurgence.player.Player;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

@Entity
public class BankAccount extends AbstractAggregateRoot<BankAccount> {

	@Id
	@Column(unique = true, nullable = false)
	private Long id;

	@MapsId
	@OneToOne
	private Player owner;

	private Long amount;

	public BankAccount() {
	}

	public BankAccount(Player owner, Long amount) {
		this.owner = owner;
		this.amount = amount;
	}

	public Player getOwner() {
		return owner;
	}

	public void increase(long value) {
		owner.decreaseBalance(value);
		if (value < 0) throw new IllegalStateException();
		amount += value;
		registerEvent(new BankAccountAmountIncreasedEvent(this.owner, value));
	}

	public void decrease(long value) {
		if (value < 0) throw new IllegalStateException();
		if (value > amount) throw new NotEnoughMoneyException();
		amount -= value;
		owner.increaseBalance(value);
		registerEvent(new BankAccountAmountDecreasedEvent(this.owner, value));
	}

	public Long getAmount() {
		return amount;
	}
}
