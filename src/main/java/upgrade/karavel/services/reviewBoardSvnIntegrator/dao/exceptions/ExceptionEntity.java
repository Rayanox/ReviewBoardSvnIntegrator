package upgrade.karavel.services.reviewBoardSvnIntegrator.dao.exceptions;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String exceptionClassName;
    @Column(columnDefinition="TEXT", length = 512)
    private String message;
    @Column(columnDefinition="TEXT", length = 2048)
    private String stackTrace;
    private LocalDateTime dateTime;

}
