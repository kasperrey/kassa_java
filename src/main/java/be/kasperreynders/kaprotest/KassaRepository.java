package be.kasperreynders.kaprotest;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class KassaRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public KassaRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }


    public Optional<Product> getProductByName(String name) {
        return jdbc.query(
                "select * from producten where naam = :naam",
                Map.of("naam", name),
                (rs, rowNum) -> new Product(
                        rs.getInt("id"),
                        rs.getString("naam"),
                        rs.getDouble("prijs"),
                        rs.getLong("barcode")
                )).stream().findFirst();
    }

    public List<Product> getAllProducts() {
        return jdbc.query(
                "select * from producten",
                (rs, rowNum) -> new Product(
                        rs.getInt("id"),
                        rs.getString("naam"),
                        rs.getDouble("prijs"),
                        rs.getLong("barcode")
                )
        );
    }

    public List<Bankkaart> getAlleBankkaarten() {
        return jdbc.query(
                "select * from bankkaarten",
                (rs, rowNum) -> new Bankkaart(
                        rs.getLong("kaartId"),
                        rs.getString("code"),
                        rs.getDouble("geld")
                )
        );
    }

    public void createProduct(Product product) {
        String sqlQuery = "INSERT INTO producten(naam, prijs, barcode) VALUES(:naam, :prijs, :barcode)";
        Map<String, Object> map = new HashMap<>();
        map.put("naam", product.naam());
        map.put("prijs", product.prijs());
        map.put("barcode", product.barcode());
        jdbc.execute(sqlQuery, map, PreparedStatement::executeUpdate);
    }
}
