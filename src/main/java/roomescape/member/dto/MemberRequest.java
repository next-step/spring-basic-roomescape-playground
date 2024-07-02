package roomescape.member.dto;

public class MemberRequest {
    public static class Create {
        private String name;
        private String email;
        private String password;

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }
    }

    public static class Login {
        private String email;
        private String password;

        public Login(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }
    }
}
