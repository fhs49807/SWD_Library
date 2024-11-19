package at.ac.fhsalzburg.swd.spring.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "RESERVEMEDIATRANSACTION")
public class ReserveMediaTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Edition edition;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date reserveStartDate;

    public ReserveMediaTransaction(User user, Edition edition, Date reserveStartDate) {
        this.user = user;
        this.edition = edition;
        this.reserveStartDate = reserveStartDate;
    }
}
