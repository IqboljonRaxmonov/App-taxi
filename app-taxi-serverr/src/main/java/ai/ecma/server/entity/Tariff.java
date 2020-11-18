package ai.ecma.server.entity;

import ai.ecma.server.entity.template.AbsNameEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;


@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class Tariff extends AbsNameEntity {

    @Column(nullable = false)
    private Double initialPrice;

    @Column(columnDefinition = "text")
    private String description;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Attachment attachment;

    @Column(nullable = false)
    private Float waitingPricePerMinute;

    @Column(nullable = false)
    private Float pricePerKm;

    @Column(nullable = false)
    private Float initialFreeWaitingTime;

    @ManyToMany
    @JoinTable(name = "tariff_model",
            joinColumns = {@JoinColumn(name = "tariff_id")},
            inverseJoinColumns = {@JoinColumn(name = "model_id")})
    private Set<Model> models;

    @ManyToMany
    @JoinTable(name = "tariff_made_year",
            joinColumns = {@JoinColumn(name = "tariff_id")},
            inverseJoinColumns = {@JoinColumn(name = "made_year_id")})
    private Set<MadeYear> madeYears;
}
