package data.entity;

import jakarta.persistence.*;
import lombok.*;
import sashkir7.grpc.Country;

import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "country", catalog = "geo")
public class CountryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "UUID default gen_random_uuid()")
    private UUID id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    public Country toGrpc() {
        return Country.newBuilder()
                .setId(id.toString())
                .setCode(code)
                .setName(name)
                .build();
    }

}
