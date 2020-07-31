package lk.nandanaMotors.asset.serviceTypeParameter.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.nandanaMotors.asset.serviceType.entity.ServiceType;
import lk.nandanaMotors.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter("ServiceTypeParameter")
public class ServiceTypeParameter extends AuditEntity {

    private String name;

    private BigDecimal price;

    @ManyToMany(mappedBy = "serviceTypeParameters")
    private List<ServiceType> serviceTypes;


}
