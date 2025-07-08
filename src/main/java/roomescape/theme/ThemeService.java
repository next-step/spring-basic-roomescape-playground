package roomescape.theme;

import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) { this.themeRepository = themeRepository; }

    public List<Theme> findAll() { return themeRepository.findAll(); }

    public Theme save(Theme theme) { return themeRepository.save(theme); }

    public void deleteById(Long id) { themeRepository.deleteById(id); }

}
