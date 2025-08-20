package yes.shief.telegrambotspicesshop.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Table(name = "order")
@Data
@Entity
@Builder
@NoArgsConstructor
@RequiredArgsConstructor
public class Order {
    @Id
    private Long id;


}
