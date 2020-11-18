package ai.ecma.server.entity;

import ai.ecma.server.bot.enums.LangEnums;
import ai.ecma.server.entity.template.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * BY SIROJIDDIN on 12.11.2020
 */

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BotUser extends AbsEntity {
    @Column(unique = true, nullable = false)
    private Long chatId;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private LangEnums lang;

    private String state;

}
