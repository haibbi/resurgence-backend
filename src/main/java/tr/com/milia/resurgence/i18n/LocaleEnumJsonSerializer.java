package tr.com.milia.resurgence.i18n;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.io.IOException;

import static tr.com.milia.resurgence.i18n.LocaleEnum.I18_PROPERTY;

/**
 * {@link LocaleEnum} implement edilmiş {@link Enum}'ları serialize etmek için kullanılır.
 *
 * <p>Serialize ederken otomatik olarak {@code JSON} nesnesine iki field eklenir.
 *
 * <p>{@link LocaleEnum#NAME_PROPERTY key} field'ı enum'ın ismini barındırır.
 * <p>{@link LocaleEnum#I18_PROPERTY value} field'ı enum'ın {@link LocaleContextHolder#getLocale()}
 * bilgisine göre çevirisi yapılmış bilgisini barındıdır.
 */
public class LocaleEnumJsonSerializer extends JsonSerializer<LocaleEnum> {
	private final ResourceBundleMessageSource messageSource;

	public LocaleEnumJsonSerializer() {
		messageSource = new ResourceBundleMessageSource();
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setBasename("enum/messages");
	}

	@Override
	public void serialize(LocaleEnum value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeStartObject();
		JavaType javaType = serializers.constructType(value.getClass());
		BeanDescription beanDesc = serializers.getConfig().introspect(javaType);
		JsonSerializer<Object> serializer = BeanSerializerFactory.instance.findBeanSerializer(serializers,
			javaType,
			beanDesc);
		serializer.unwrappingSerializer(null).serialize(value, gen, serializers);

		String message = messageSource.getMessage(value, LocaleContextHolder.getLocale());

		gen.writeObjectField(I18_PROPERTY, message);
		gen.writeEndObject();
	}
}
