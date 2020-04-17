package tr.com.milia.resurgence.i18n;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;

import java.io.IOException;
import java.util.Optional;

import static tr.com.milia.resurgence.i18n.LocaleEnum.NAME_PROPERTY;

/**
 * {@link LocaleEnum} implement edilmiş {@link Enum}'ları deserialize etmek için kullanılır.
 *
 * <p>Deserialize edilecek enumlar {@link String} veya {@code JSON} objesi şeklinde gelebilir.
 * {@link String} olarak geldiği durumda Enum değeri, gelen bilgiye göre aranır.
 * {@code JSON} objesi gelmesi durumunda, {@code name} alanı bilgisine göre Enum değeri bulunur.
 */
public class LocaleEnumJsonDeserializer extends JsonDeserializer<LocaleEnum> implements ContextualDeserializer {
	private JavaType type;

	public LocaleEnumJsonDeserializer() {
	}

	public LocaleEnumJsonDeserializer(JavaType type) {
		this.type = type;
	}

	@Override
	@SuppressWarnings("unchecked")
	public LocaleEnum deserialize(JsonParser parser, DeserializationContext context) throws IOException {
		if (!type.isEnumType()) {
			throw new UnsupportedOperationException("Only enum deserialization supported! " + type.getRawClass().getCanonicalName());
		}

		JsonNode treeNode = parser.readValueAsTree();

		final Optional<String> name = getEnumName(treeNode);
		if (!name.isPresent()) return null;

		final Class<? extends Enum> enumType = (Class<? extends Enum>) type.getRawClass();
		final Enum localeEnum = Enum.valueOf(enumType, name.get());

		if (!LocaleEnum.class.isAssignableFrom(localeEnum.getClass()))
			throw new UnsupportedOperationException("Enum must implements " + LocaleEnum.class.getCanonicalName());

		return (LocaleEnum) localeEnum;
	}

	@Override
	public JsonDeserializer<LocaleEnum> createContextual(DeserializationContext deserializationContext, BeanProperty beanProperty) {
		JavaType type = deserializationContext.getContextualType() != null
			? deserializationContext.getContextualType()
			: beanProperty.getMember().getType();
		return new LocaleEnumJsonDeserializer(type);
	}

	private Optional<String> getEnumName(JsonNode node) {
		JsonToken token = node.asToken();
		if (token == JsonToken.START_OBJECT) {
			if (!node.has(NAME_PROPERTY)) {
				throw new UnsupportedOperationException("Object must contains " + NAME_PROPERTY + " field");
			}
			JsonNode nameNode = node.get(NAME_PROPERTY);
			if (nameNode.isNull()) return Optional.empty();
			if (nameNode.isTextual()) return Optional.ofNullable(nameNode.textValue());

			throw new UnsupportedOperationException("Name field must null or string");
		} else if (token == JsonToken.VALUE_STRING) {
			return Optional.ofNullable(node.asText());
		}
		throw new UnsupportedOperationException("Object must contains " + NAME_PROPERTY + " field");
	}
}
