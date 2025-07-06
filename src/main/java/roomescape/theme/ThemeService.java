package roomescape.theme;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;

import java.util.List;

@Service
@Transactional
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public ThemeResponse save(ThemeRequest themeRequest) {
        Theme saveTheme = themeRepository.save(themeRequest.toEntity());
        return ThemeResponse.from(saveTheme);
    }

    public List<ThemeResponse> findAll() {
        return themeRepository.findAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public void deleteById(Long id) {
        try {
            themeRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new RoomEscapeException(ErrorCode.DELETE_CONFLICT,"테마가 다른 자원에서 사용중입니다.");
        }
    }
}
