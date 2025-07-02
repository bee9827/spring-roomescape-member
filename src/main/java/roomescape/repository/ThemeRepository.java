package roomescape.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;
import roomescape.service.dto.result.PopularThemeResult;

import java.util.List;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {
    @Query("""
            SELECT PopularThemeResult(t, COUNT(r))
            FROM Theme as t
            LEFT JOIN Reservation r
            ON t = r.theme
            GROUP BY t
            ORDER BY COUNT(r) DESC
            """)
    List<PopularThemeResult> findPopularThemes();
}
