package tr.com.milia.resurgence.i18n;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.context.MessageSourceResolvable;

/**
 * Enum lokalizasyonu sağlar.
 * <p>
 * Lokalizasyon istenilen enum'a implement edilmesi ve
 * {@code resource/enum/messages} bundle'ına {@link Class#getCanonicalName()} + "#" + {@link Enum#name()} eklenmelidir.<br>
 * Örnek olarak bundle'a eklenmesi gereken değer {@code tr.com.aselsan.enum.Status#RUNNING=Çalışıyor}
 * </p>
 * Eğer çeviri girilmemiş ise {@link #getDefaultMessage()} devreye girer.<br>
 */
@JsonIgnoreProperties({"declaringClass"})
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonSerialize(using = LocaleEnumJsonSerializer.class)
@JsonDeserialize(using = LocaleEnumJsonDeserializer.class)
public interface LocaleEnum extends MessageSourceResolvable {
    String NAME_PROPERTY = "key";
    String I18_PROPERTY = "value";

    @JsonIgnore
    @Override
    default String[] getCodes() {
        return new String[]{getClass().getCanonicalName() + "#" + name()};
    }

    @JsonIgnore
    @Override
    default Object[] getArguments() {
        return new Object[]{};
    }

    @JsonIgnore
    @Override
    default String getDefaultMessage() {
        return name();
    }

    @JsonProperty(NAME_PROPERTY)
    String name();

}
