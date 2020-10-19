package tr.com.milia.resurgence.chat;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

class TopicTest {

	private static final Logger log = LoggerFactory.getLogger(TopicTest.class);

	@Test
	void shouldGenerateSameTopicToSamePeer() {
		Topic topic1 = Topic.p2p("john", "jane");
		Topic topic2 = Topic.p2p("jane", "john");

		log.info("\"{}\" and \"{}\" should be same", topic1.getName(), topic2.getName());

		assertEquals(topic1, topic2);
	}

	@Test
	void shouldGenerateDifferentTopicToSeemPeer() {
		System.out.println("abc".hashCode());
		System.out.println("abd".hashCode());
		System.out.println("abb".hashCode());
		System.out.println("abe".hashCode());
		Topic topic1 = Topic.p2p("abc", "abd");
		Topic topic2 = Topic.p2p("abb", "abe");

		log.info("\"{}\" and \"{}\" should not be same", topic1.getName(), topic2.getName());

		assertNotEquals(topic1, topic2);
	}

	@Test
	void shouldGenerateDifferentTopicForOtherPeers() {
		Topic topic1 = Topic.p2p("jane", "doe");
		Topic topic2 = Topic.p2p("mike", "jane");

		log.info("\"{}\" and \"{}\" should not be same", topic1.getName(), topic2.getName());

		assertNotEquals(topic1, topic2);
	}

	@Test
	void shouldGenerateRandomTopic() {
		Topic topic1 = Topic.group("general");
		Topic topic2 = Topic.group("crime");

		log.info("\"{}\" and \"{}\" should not be same", topic1.getName(), topic2.getName());
		assertNotEquals(topic1, topic2);
	}

	@Test
	void shouldThrowException() {
		assertThrows(IllegalArgumentException.class, () -> Topic.p2p("jane", "jane"));
	}

	@Test
	void palindrome1() {
		Topic one = Topic.p2p("ali", "veli");
		Topic actual = Topic.p2p("alive", "li");
		assertNotEquals(one, actual);
	}

	@Test
	void palindrome2() {
		Topic one = Topic.p2p("ali", "veli");
		Topic actual = Topic.p2p("i", "velial");
		assertNotEquals(one, actual);
	}
}
