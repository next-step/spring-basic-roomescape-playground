package roomescape.waiting;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.LoginMember;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@RestController
@RequestMapping("/waitings")
public class WaitingController {

    private final WaitingRepository waitingRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public WaitingController(WaitingRepository waitingRepository,
                             TimeRepository timeRepository, ThemeRepository themeRepository) {
        this.waitingRepository = waitingRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    @PostMapping
    public ResponseEntity<WaitingResponse> createWaiting(LoginMember loginMember, @RequestBody WaitingRequest request) {
        boolean exists = waitingRepository.existsByDateAndTimeIdAndThemeIdAndMemberId(
                request.getDate(), request.getTime(), request.getTheme(), loginMember.getId()
        );
        if (exists) {
            return ResponseEntity.badRequest().build();
        }

        Time time = timeRepository.findById(request.getTime()).orElseThrow();
        Theme theme = themeRepository.findById(request.getTheme()).orElseThrow();

        Waiting waiting = new Waiting(loginMember.getId(), request.getDate(), time, theme);
        Waiting saved = waitingRepository.save(waiting);

        return ResponseEntity.status(HttpStatus.CREATED).body(new WaitingResponse(saved.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWaiting(LoginMember loginMember, @PathVariable Long id) {
        Waiting waiting = waitingRepository.findByIdAndMemberId(id, loginMember.getId())
                .orElseThrow(() -> new IllegalArgumentException());

        waitingRepository.delete(waiting);
        return ResponseEntity.noContent().build();
    }
}
