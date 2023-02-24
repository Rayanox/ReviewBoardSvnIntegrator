package upgrade.karavel.services.ReviewBoardSvnIntegrator.dao.exceptions;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Exceptions")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionEntity {
    @Id
    private Long id;
    private String message;
    private String stackTrace;
    private LocalDateTime dateTime;

}
