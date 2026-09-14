package roomescape.page.controller.manager;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ManagerPageController {
    @GetMapping("/manager")
    public String index() {
        return "admin/index";
    }

    @GetMapping("/manager/reservation")
    public String reservation() {
        return "admin/reservation";
    }

    @GetMapping("/manager/theme")
    public String theme() {
        return "admin/theme";
    }

    @GetMapping("/manager/time")
    public String time() {
        return "admin/time";
    }
}
